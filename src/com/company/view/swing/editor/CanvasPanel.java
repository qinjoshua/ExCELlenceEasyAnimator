package com.company.view.swing.editor;

import com.company.controller.animatoractions.AnimatorAction;
import com.company.controller.animatoractions.ChangeHeight;
import com.company.controller.animatoractions.ChangeWidth;
import com.company.controller.animatoractions.ChangeX;
import com.company.controller.animatoractions.ChangeY;
import com.company.controller.animatoractions.CreateNewShape;
import com.company.controller.viewactions.editoractions.EditorAction;
import com.company.controller.viewactions.editoractions.HighlightShape;
import com.company.controller.viewactions.editoractions.RefreshView;
import com.company.model.ReadOnlyAnimatorModel;
import com.company.model.shape.ShapeType;
import com.company.view.swing.AShapesPanel;
import com.company.view.swing.DecoratedShape;
import com.company.view.swing.editor.boundingbox.Anchor;
import com.company.view.swing.editor.boundingbox.BoundingBoxImpl;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.KeyStroke;
import javax.swing.border.Border;

import static com.company.view.swing.editor.MousePointUtils.transformPoint;

/**
 * Represents the canvas portion of the editor, the main area where the animation can be modified
 * and worked on. This canvas panel allows shapes to be dragged on top of it, repositioned, and
 * resized, and displays what any given keyframe looks like.
 */
public class CanvasPanel extends AShapesPanel {
  private final Consumer<AnimatorAction> modelCallback;
  private DecoratedShape highlightedShape;
  private String highlightedShapeName;
  private Consumer<EditorAction> callback;
  private BoundingBoxImpl boundingBoxImpl;
  private ShapeType toBeCreatedShape;
  private String toBeCreatedName;
  private Shape beingCreatedShape;

  /**
   * Initializes the panel given a model and a callback consumer. Sets the time t to 0 to it starts
   * at the beginning.
   *
   * @param model         the animator model to use
   * @param modelCallback the callback used to edit the model
   * @throws IllegalArgumentException if the model is null
   */
  public CanvasPanel(ReadOnlyAnimatorModel model, Consumer<AnimatorAction> modelCallback) {
    super(model);

    this.modelCallback = modelCallback;
    this.highlightedShape = null;
    this.highlightedShapeName = null;

    this.toBeCreatedShape = null;
    this.beingCreatedShape = null;
    this.toBeCreatedName = null;

    MouseAdapter resizer = new ResizeMouseAdapter();
    this.addMouseListener(resizer);
    this.addMouseMotionListener(resizer);
    boundingBoxImpl = null;

    Border grayLine = BorderFactory.createLineBorder(Color.GRAY);
    this.setBorder(grayLine);

    MouseAdapter mover = new MoveMouseAdapter();
    this.addMouseListener(mover);
    this.addMouseMotionListener(mover);

    MouseAdapter creator = new CreateShapeMouseAdapter();
    this.addMouseListener(creator);
    this.addMouseMotionListener(creator);

    MouseAdapter cursor = new CursorMouseAdapter();
    this.addMouseMotionListener(cursor);

    this.addMouseListener(new ClickMouseAdapter());

    final Action moveUp = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null && boundingBoxImpl != null) {
          modelCallback.accept(new ChangeY(highlightedShapeName, t, -1));
          boundingBoxImpl.translate(0, -1);
          repaint();
        }
      }
    };
    final Action moveDown = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null && boundingBoxImpl != null) {
          modelCallback.accept(new ChangeY(highlightedShapeName, t, 1));
          boundingBoxImpl.translate(0, 1);
          repaint();
        }
      }
    };
    final Action moveLeft = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null && boundingBoxImpl != null) {
          modelCallback.accept(new ChangeX(highlightedShapeName, t, -1));
          boundingBoxImpl.translate(-1, 0);
          repaint();
        }
      }
    };
    final Action moveRight = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null && boundingBoxImpl != null) {
          modelCallback.accept(new ChangeX(highlightedShapeName, t, 1));
          boundingBoxImpl.translate(1, 0);
          repaint();
        }
      }
    };

    this.setCommand(KeyStroke.getKeyStroke("UP"), "moveUp", moveUp);
    this.setCommand(KeyStroke.getKeyStroke("DOWN"), "moveDown", moveDown);
    this.setCommand(KeyStroke.getKeyStroke("LEFT"), "moveLeft", moveLeft);
    this.setCommand(KeyStroke.getKeyStroke("RIGHT"), "moveRight", moveRight);
  }

  /**
   * Sets the given keystroke to the given action with the given name.
   *
   * @param key    the key used to start the action
   * @param name   the name used for the action
   * @param action the action itself
   */
  private void setCommand(KeyStroke key, String name, Action action) {
    this.getInputMap().put(key, name);
    this.getActionMap().put(name, action);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (boundingBoxImpl != null) {
      boundingBoxImpl.renderTo((Graphics2D) g);
    }

    this.drawBeingCreatedShape((Graphics2D) g);
  }

  @Override
  protected void drawCanvas(Graphics g) {
    Color oldColor = g.getColor();
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, this.model.getCanvasWidth(),
        this.model.getCanvasHeight());
    g.setColor(oldColor);
  }

  /**
   * Gets the currently highlighted shape.
   *
   * @return the highlighted shape
   */
  DecoratedShape getHighlightedShape() {
    if (this.highlightedShape == null) {
      return null;
    } else {
      return this.highlightedShape;
    }
  }

  /**
   * Gets the name of the currently highlighted shape.
   *
   * @return the name of the currently highlighted shape
   */
  public String getHighlightedShapeName() {
    return highlightedShapeName;
  }

  /**
   * Highlights the given shape.
   *
   * @throws IllegalArgumentException if the given shapw does not exist in the map of shapes
   *                                  currently on the canvas
   */
  void highlightShape(String toBeHighlighted) {
    if (toBeHighlighted == null) {
      this.deselectAll();
      return;
    }
    if (!shapes.containsKey(toBeHighlighted)) {
      throw new IllegalArgumentException("Attempted to highlight a non-existent shape");
    } else {
      this.highlightedShapeName = toBeHighlighted;
      this.highlightedShape = shapes.get(toBeHighlighted);
    }
  }

  /**
   * Updates the bounding box to where it's supposed to be.
   */
  void updateBoundingBox() {
    if (highlightedShape != null) {
      this.boundingBoxImpl = new BoundingBoxImpl(this.highlightedShape);
    }
    this.repaint();
  }

  /**
   * Sets the callback for this panel.
   *
   * @param callback the callback to set to
   */
  void setCallback(Consumer<EditorAction> callback) {
    this.callback = callback;
  }

  /**
   * Tells the EditorView to prepare to create a shape of the given type.
   *
   * @param type type of shape to be created
   * @param name name of the shape being created
   */
  void createShape(ShapeType type, String name) {
    this.toBeCreatedShape = type;
    this.toBeCreatedName = name;
  }

  /**
   * Clears any selections that are on the page, removing the bounding box.
   */
  private void deselectAll() {
    this.highlightedShape = null;
    this.boundingBoxImpl = null;
    this.highlightedShapeName = null;
    this.updateBoundingBox();
  }

  private void drawBeingCreatedShape(Graphics2D g) {
    if (this.beingCreatedShape != null) {
      Color originalColor = g.getColor();
      g.setColor(new Color(0, 0, 0, 80));
      g.fill(this.beingCreatedShape);
      g.setColor(originalColor);
    }
  }

  /**
   * Represents the mouse adapter that will allow users to highlight shapes.
   */
  private class ClickMouseAdapter extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      String toBeHighlighted = null;

      for (Map.Entry<String, DecoratedShape> coloredShape : shapes.entrySet()) {
        if (coloredShape.getValue().shape.contains(transformPoint(e.getPoint(),
                coloredShape.getValue()))) {
          toBeHighlighted = coloredShape.getKey();
        }
      }

      callback.accept(new HighlightShape(toBeHighlighted));
    }
  }

  /**
   * Represents a mouse adapter that will allow the user to resize shapes.
   */
  private class ResizeMouseAdapter extends MouseAdapter {
    private Anchor anchor;
    private int lastX;
    private int lastY;

    private int oldX;
    private int oldY;
    private double oldWidth;
    private double oldHeight;

    public ResizeMouseAdapter() {
      anchor = null;
    }

    @Override
    public void mousePressed(MouseEvent e) {
      // Checks that bounding box exists, and that we are not in the process of making a new shape
      if (boundingBoxImpl != null && toBeCreatedShape == null) {
        this.anchor = boundingBoxImpl.getAnchorAtPoint(e.getX(), e.getY());
        this.lastX = e.getX();
        this.lastY = e.getY();

        this.oldX = boundingBoxImpl.getX();
        this.oldY = boundingBoxImpl.getY();
        this.oldWidth = boundingBoxImpl.getWidth();
        this.oldHeight = boundingBoxImpl.getHeight();
      }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      if (this.anchor != null) {
        boundingBoxImpl.growFromAnchor(anchor.getType(), e.getX() - lastX, e.getY() - lastY);

        this.lastX = e.getX();
        this.lastY = e.getY();

        repaint();
      }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      if (this.anchor != null) {
        modelCallback.accept(new ChangeX(highlightedShapeName, t, boundingBoxImpl.getX() - oldX));
        modelCallback.accept(new ChangeY(highlightedShapeName, t, boundingBoxImpl.getY() - oldY));
        modelCallback.accept(new ChangeWidth(highlightedShapeName, t,
            boundingBoxImpl.getWidth() - oldWidth));
        modelCallback.accept(new ChangeHeight(highlightedShapeName, t,
            boundingBoxImpl.getHeight() - oldHeight));

        callback.accept(new RefreshView());
        this.anchor = null;
      }
    }
  }

  /**
   * Represents a mouse adapter that will allow the user to move shapes across the canvas.
   */
  private class MoveMouseAdapter extends MouseAdapter {
    private int lastX;
    private int lastY;
    private boolean editing;

    private int oldX;
    private int oldY;

    @Override
    public void mousePressed(MouseEvent e) {
      // Checks that bounding box exists, that we're not trying to resize a shape, and that
      if (boundingBoxImpl != null && boundingBoxImpl.getAnchorAtPoint(e.getX(), e.getY()) == null
          && boundingBoxImpl.contains(e.getX(), e.getY()) && toBeCreatedShape == null) {
        this.oldX = boundingBoxImpl.getX();
        this.oldY = boundingBoxImpl.getY();

        lastX = e.getX();
        lastY = e.getY();
        editing = true;
      }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      if (boundingBoxImpl != null && editing) {
        boundingBoxImpl.translate(e.getX() - lastX, e.getY() - lastY);
        lastX = e.getX();
        lastY = e.getY();
        repaint();
      }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      if (this.editing) {
        modelCallback.accept(
            new ChangeX(highlightedShapeName, t, boundingBoxImpl.getX() - this.oldX));
        modelCallback.accept(
            new ChangeY(highlightedShapeName, t, boundingBoxImpl.getY() - this.oldY));
        this.editing = false;
        callback.accept(new RefreshView());
      }
    }
  }

  private class CreateShapeMouseAdapter extends MouseAdapter {
    private int startX;
    private int startY;
    private boolean editing;

    @Override
    public void mousePressed(MouseEvent e) {
      if (toBeCreatedShape != null) {
        deselectAll();
        this.editing = true;
        this.startX = e.getX();
        this.startY = e.getY();
        beingCreatedShape =
            swingShapeMap.get(toBeCreatedShape).createShape(startX + model.getCanvasX(),
                startY + model.getCanvasY(), 0, 0);
      }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      if (editing) {
        beingCreatedShape =
            swingShapeMap.get(toBeCreatedShape).createShape(
                startX + model.getCanvasX(),
                startY + model.getCanvasY(),
                e.getX() - startX, e.getY() - startY);
        repaint();
      }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      if (editing) {
        editing = false;

        modelCallback.accept(new CreateNewShape(
            toBeCreatedName, startX + model.getCanvasX(), startY + model.getCanvasY(),
            e.getX() - startX, e.getY() - startY,
            Color.GRAY, toBeCreatedShape, t));

        updateShapes();
        toBeCreatedShape = null;
        beingCreatedShape = null;
        toBeCreatedName = null;
        callback.accept(new RefreshView());
      }
    }
  }

  /**
   * Mouse adapter that changes the cursor depending on the context.
   */
  private class CursorMouseAdapter extends MouseAdapter {
    @Override
    public void mouseMoved(MouseEvent e) {
      if (boundingBoxImpl != null) {
        Anchor anchor = boundingBoxImpl.getAnchorAtPoint(e.getX(), e.getY());
        if (anchor != null) {
          setCursor(new Cursor(anchor.getType().getCursor()));
        } else if (boundingBoxImpl.contains(e.getX(), e.getY())) {
          setCursor(new Cursor(Cursor.MOVE_CURSOR));
        } else {
          setCursor(Cursor.getDefaultCursor());
        }
      }
    }
  }
}
