package com.company.view.swing.editor;

import com.company.controller.animatoractions.AnimatorAction;
import com.company.controller.animatoractions.ChangeColor;
import com.company.controller.animatoractions.ChangeHeight;
import com.company.controller.animatoractions.ChangeWidth;
import com.company.controller.animatoractions.ChangeX;
import com.company.controller.animatoractions.ChangeY;
import com.company.controller.viewactions.editoractions.EditorAction;
import com.company.controller.viewactions.editoractions.RefreshView;
import com.company.model.ReadOnlyAnimatorModel;
import com.company.model.shape.Shape;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;
import javax.swing.colorchooser.AbstractColorChooserPanel;

/**
 * A panel to edit the properties of a specific shape in an animation at a specific time.
 */
public class PropertyPanel extends JPanel {
  private PropertiesPanel properties;
  private Consumer<EditorAction> viewCallback;

  /**
   * Initializes a new property panel initially with no values, leading to an empty panel.
   */
  public PropertyPanel() {
    super();
    this.properties = null;
    this.viewCallback = null;
    this.setPreferredSize(new Dimension(0, 0));
  }

  /**
   * Hides the properties from this property panel.
   */
  void hideProperties() {
    if (this.properties != null) {
      this.remove(properties);
      this.properties = null;
      this.setPreferredSize(new Dimension(0, 0));
      this.repaint();
    }
  }

  /**
   * Constructs a property panel to edit a specific shape at a specific time on a given model.
   *
   * @param shapeName     the name of the shape being edited
   * @param tick          the tick at which the shape is being edited*
   * @param modelCallback the callback to use for editing the model
   * @param model         the read-only view model
   */
  void addProperties(String shapeName, int tick, Consumer<AnimatorAction> modelCallback,
                     ReadOnlyAnimatorModel model) {
    this.hideProperties();
    this.properties = new PropertiesPanel(shapeName, tick, modelCallback, model, viewCallback);
    this.add(properties);
    this.setPreferredSize(new Dimension(400, 600));
    this.repaint();
  }

  /**
   * Sets the editor view callback.
   *
   * @param viewCallback the callback for editor view modification
   */
  void setViewCallback(Consumer<EditorAction> viewCallback) {
    this.viewCallback = viewCallback;
  }

  private class PropertiesPanel extends JPanel {
    // The tick that is being edited.
    int tick;
    // The name of the shape being edited.
    private final String shapeName;
    // The callback for model editing.
    private final Consumer<AnimatorAction> modelCallback;
    // The callback for editor view modification.
    private final Consumer<EditorAction> viewCallback;
    // The read-only view model.
    private final ReadOnlyAnimatorModel model;

    /**
     * Constructs a property panel to edit a specific shape at a specific time on a given model,
     * sending any edits to a specific callback.
     *
     * @param shapeName     the name of the shape being edited
     * @param tick          the tick at which the shape is being edited*
     * @param modelCallback the callback to use for editing the model
     * @param model         the read-only view model
     * @param viewCallback  the callback to use for editing the view
     */
    public PropertiesPanel(String shapeName, int tick, Consumer<AnimatorAction> modelCallback,
                           ReadOnlyAnimatorModel model, Consumer<EditorAction> viewCallback) {
      super();
      this.shapeName = shapeName;
      this.tick = tick;
      this.modelCallback = modelCallback;
      this.model = model;
      this.viewCallback = viewCallback;

      JPanel nonColor = new JPanel(new SpringLayout());

      Border border = BorderFactory.createLineBorder(Color.lightGray);
      this.setBorder(border);

      Shape origShape = model.shapesAt(tick).get(shapeName);

      SpinnerModel xField = new SpinnerNumberModel(
          origShape.getPosition().getX(),
          Integer.MIN_VALUE,
          Integer.MAX_VALUE,
          1);

      SpinnerModel yField = new SpinnerNumberModel(
          origShape.getPosition().getY(),
          Integer.MIN_VALUE,
          Integer.MAX_VALUE,
          1);

      SpinnerModel widthField = new SpinnerNumberModel(
          origShape.getWidth(),
          0,
          Integer.MAX_VALUE,
          1);

      SpinnerModel heightField = new SpinnerNumberModel(
          origShape.getHeight(),
          0,
          Integer.MAX_VALUE,
          1);

      xField.addChangeListener(e -> {
        Shape currShape = model.shapesAt(tick).get(shapeName);
        SpinnerNumberModel source = (SpinnerNumberModel) e.getSource();
        modelCallback.accept(new ChangeX(shapeName, tick,
            (double) source.getNumber() - currShape.getPosition().getX()));
        this.getViewCallback().accept(new RefreshView());
      });

      yField.addChangeListener(e -> {
        Shape currShape = model.shapesAt(tick).get(shapeName);
        SpinnerNumberModel source = (SpinnerNumberModel) e.getSource();
        modelCallback.accept(new ChangeY(shapeName, tick,
            (double) source.getNumber() - currShape.getPosition().getY()));
        this.getViewCallback().accept(new RefreshView());
      });

      widthField.addChangeListener(e -> {
        Shape currShape = model.shapesAt(tick).get(shapeName);
        SpinnerNumberModel source = (SpinnerNumberModel) e.getSource();
        modelCallback.accept(new ChangeWidth(shapeName, tick,
            (double) source.getNumber() - currShape.getWidth()));
        this.getViewCallback().accept(new RefreshView());
      });

      heightField.addChangeListener(e -> {
        Shape currShape = model.shapesAt(tick).get(shapeName);
        SpinnerNumberModel source = (SpinnerNumberModel) e.getSource();
        modelCallback.accept(new ChangeHeight(shapeName, tick,
            (double) source.getNumber() - currShape.getHeight()));
        this.getViewCallback().accept(new RefreshView());
      });

      String[] labels = {"x", "y", "width", "height"};
      SpinnerModel[] spinners = {xField, yField, widthField, heightField};

      for (int i = 0; i < labels.length; i++) {
        JLabel label = new JLabel(labels[i], JLabel.TRAILING);
        label.setPreferredSize(new Dimension(100, 20));
        nonColor.add(label);
        JSpinner spinner = new JSpinner(spinners[i]);
        spinner.setPreferredSize(new Dimension(100, 20));
        spinner.setMaximumSize(new Dimension(100, 20));
        label.setLabelFor(spinner);
        nonColor.add(spinner);
      }

      JColorChooser colorField = new JColorChooser(origShape.getColor());
      AbstractColorChooserPanel lchPanel = new LCHColorChooser(origShape.getColor());
      colorField.setChooserPanels(new AbstractColorChooserPanel[]{lchPanel});
      LCHColorChooser.LCHPreviewPanel previewPanel = new LCHColorChooser.LCHPreviewPanel(
          origShape.getColor(), colorField.getSelectionModel());

      colorField.getSelectionModel().addChangeListener(e -> {
        Color newColor = colorField.getColor();
        previewPanel.updateRGB(newColor);
        modelCallback.accept(new ChangeColor(shapeName, tick, newColor));
        this.getViewCallback().accept(new RefreshView());
      });
      colorField.setPreviewPanel(previewPanel);

      JPanel color = new JPanel(new FlowLayout());
      JLabel colorLabel = new JLabel("Color", JLabel.TRAILING);
      colorLabel.setLabelFor(colorField);
      // color.add(colorLabel);
      color.add(colorField);

      // from the Java tutorial
      layout.SpringUtilities.makeGrid(nonColor,
          4, 2,
          0, 0,
          10, 10
      );

      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      nonColor.setPreferredSize(new Dimension(250, 120));
      color.setPreferredSize(new Dimension(400, 500));
      this.add(Box.createVerticalStrut(10));
      this.add(nonColor);
      this.add(color);
    }

    /**
     * Gets the view callback.
     *
     * @return the view callback
     */
    private Consumer<EditorAction> getViewCallback() {
      return this.viewCallback;
    }
  }
}
