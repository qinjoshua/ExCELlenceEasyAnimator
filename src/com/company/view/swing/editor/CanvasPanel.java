package com.company.view.swing.editor;

import com.company.controller.animatoractions.AnimatorAction;
import com.company.controller.animatoractions.ChangeHeight;
import com.company.controller.animatoractions.ChangeWidth;
import com.company.controller.animatoractions.ChangeX;
import com.company.controller.animatoractions.ChangeY;
import com.company.controller.viewactions.editoractions.EditorAction;
import com.company.controller.viewactions.editoractions.HighlightShape;
import com.company.model.ReadOnlyAnimatorModel;
import com.company.view.swing.AShapesPanel;
import com.company.view.swing.editor.boundingbox.Anchor;
import com.company.view.swing.editor.boundingbox.BoundingBox;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

/**
 * Represents the canvas portion of the editor, the main area where the animation can be modified
 * and worked on. This canvas panel allows shapes to be dragged on top of it, repositioned, and
 * resized, and displays what any given keyframe looks like.
 */
public class CanvasPanel extends AShapesPanel {
  private ColoredShape highlightedShape;
  private String highlightedShapeName;

  private Consumer<EditorAction> callback;
  private BoundingBox boundingBox;
  private final Consumer<AnimatorAction> modelCallback;

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

    MouseAdapter resizer = new ResizeMouseAdapter();
    this.addMouseListener(resizer);
    this.addMouseMotionListener(resizer);
    boundingBox = null;

    MouseAdapter mover = new MoveMouseAdapter();
    this.addMouseListener(mover);
    this.addMouseMotionListener(mover);

    MouseAdapter cursor = new CursorMouseAdapter();
    this.addMouseMotionListener(cursor);

    this.addMouseListener(new ClickMouseAdapter());

    final Action MOVE_UP = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null && boundingBox != null) {
          modelCallback.accept(new ChangeY(highlightedShapeName, t, -1));
          boundingBox.translate(0, -1);
          repaint();
        }
      }
    };
    final Action MOVE_DOWN = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null && boundingBox != null) {
          modelCallback.accept(new ChangeY(highlightedShapeName, t, 1));
          boundingBox.translate(0, 1);
          repaint();
        }
      }
    };
    final Action MOVE_LEFT = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null && boundingBox != null) {
          modelCallback.accept(new ChangeX(highlightedShapeName, t, -1));
          boundingBox.translate(-1, 0);
          repaint();
        }
      }
    };
    final Action MOVE_RIGHT = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null && boundingBox != null) {
          modelCallback.accept(new ChangeX(highlightedShapeName, t, 1));
          boundingBox.translate(1, 0);
          repaint();
        }
      }
    };

    this.setCommand(KeyStroke.getKeyStroke("UP"), "moveUp", MOVE_UP);
    this.setCommand(KeyStroke.getKeyStroke("DOWN"), "moveDown", MOVE_DOWN);
    this.setCommand(KeyStroke.getKeyStroke("LEFT"), "moveLeft", MOVE_LEFT);
    this.setCommand(KeyStroke.getKeyStroke("RIGHT"), "moveRight", MOVE_RIGHT);

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

    if (boundingBox != null) {
      boundingBox.renderTo((Graphics2D) g);
    }
  }

  /**
   * Gets the currently highlighted shape.
   *
   * @return the highlighted shape
   */
  Shape getHighlightedShape() {
    return this.highlightedShape.shape;
  }

  /**
   * Highlights the given shape.
   *
   * @throws IllegalArgumentException if the given shapw does not exist in the map of shapes
   *                                  currently on the canvas
   */
  void highlightShape(Shape toBeHighlighted) {
    if (toBeHighlighted == null) {
      this.highlightedShape = null;
      this.boundingBox = null;
      this.highlightedShapeName = null;
      return;
    }
    for (Map.Entry<String, ColoredShape> coloredShape : shapes.entrySet()) {
      // We use reference equality to determine that the shape is the same shape in memory as the
      // one on screen
      if (coloredShape.getValue().shape == toBeHighlighted) {
        this.highlightedShape = coloredShape.getValue();
        this.highlightedShapeName = coloredShape.getKey();
        return;
      }
    }
    throw new IllegalArgumentException("Attempted to highlight a non-existent shape");
  }

  /**
   * Updates the bounding box to where it's supposed to be.
   */
  void updateBoundingBox() {
    if (highlightedShape != null) {
      this.boundingBox = new BoundingBox(this.highlightedShape.shape);
      this.repaint();
    }
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
   * Represents the mouse adapter that will allow users to highlight shapes.
   */
  private class ClickMouseAdapter extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      Shape toBeHighlighted = null;

      for (AShapesPanel.ColoredShape coloredShape : shapes.values()) {
        if (coloredShape.shape.contains(e.getPoint())) {
          toBeHighlighted = coloredShape.shape;
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
      if (boundingBox != null) {
        this.anchor = boundingBox.getAnchorAtPoint(e.getX(), e.getY());
        this.lastX = e.getX();
        this.lastY = e.getY();

        this.oldX = boundingBox.getX();
        this.oldY = boundingBox.getY();
        this.oldWidth = boundingBox.getWidth();
        this.oldHeight = boundingBox.getHeight();
      }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      if (this.anchor != null) {
        boundingBox.growFromAnchor(anchor.getType(), e.getX() - lastX, e.getY() - lastY);

        this.lastX = e.getX();
        this.lastY = e.getY();

        repaint();
      }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      if (this.anchor != null) {
        modelCallback.accept(new ChangeX(highlightedShapeName, t, boundingBox.getX() - oldX));
        modelCallback.accept(new ChangeY(highlightedShapeName, t, boundingBox.getY() - oldY));
        modelCallback.accept(new ChangeWidth(highlightedShapeName, t,
                boundingBox.getWidth() - oldWidth));
        modelCallback.accept(new ChangeHeight(highlightedShapeName, t,
                boundingBox.getHeight() - oldHeight));

        repaint();
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
      if (boundingBox != null && boundingBox.getAnchorAtPoint(e.getX(), e.getY()) == null
              && boundingBox.contains(e.getX(), e.getY())) {
        this.oldX = boundingBox.getX();
        this.oldY = boundingBox.getY();

        lastX = e.getX();
        lastY = e.getY();
        editing = true;
      }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
      if (boundingBox != null && editing) {
        boundingBox.translate(e.getX() - lastX, e.getY() - lastY);
        lastX = e.getX();
        lastY = e.getY();
        repaint();
      }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
      if (this.editing) {
        modelCallback.accept(new ChangeX(highlightedShapeName, t, boundingBox.getX() - this.oldX));
        modelCallback.accept(new ChangeY(highlightedShapeName, t, boundingBox.getY() - this.oldY));
        this.editing = false;
        repaint();
      }
    }
  }

  private class CursorMouseAdapter extends MouseAdapter {
    @Override
    public void mouseMoved(MouseEvent e) {
      if (boundingBox != null) {
        Anchor anchor = boundingBox.getAnchorAtPoint(e.getX(), e.getY());
        if (anchor != null) {
          setCursor(new Cursor(anchor.getType().getCursor()));
        } else if (boundingBox.contains(e.getX(), e.getY())) {
          setCursor(new Cursor(Cursor.MOVE_CURSOR));
        } else {
          setCursor(Cursor.getDefaultCursor());
        }
      }
    }
  }
}
