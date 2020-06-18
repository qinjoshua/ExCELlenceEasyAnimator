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

import java.awt.Color;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.colorchooser.AbstractColorChooserPanel;

/**
 * A panel to edit the properties of a specific shape in an animation at a specific time.
 */
public class PropertyPanel extends JPanel {
  // The name of the shape being edited.
  String shapeName;
  // The tick that is being edited.
  int tick;
  // The callback for model editing.
  Consumer<AnimatorAction> modelCallback;
  // The callback for editor view modification.
  Consumer<EditorAction> viewCallback;
  // The read-only view model.
  ReadOnlyAnimatorModel model;

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
  public PropertyPanel(String shapeName, int tick, Consumer<AnimatorAction> modelCallback,
                       ReadOnlyAnimatorModel model, Consumer<EditorAction> viewCallback) {
    super();
    this.shapeName = shapeName;
    this.tick = tick;
    this.modelCallback = modelCallback;
    this.model = model;
    this.viewCallback = viewCallback;

    Border border = BorderFactory.createLineBorder(Color.lightGray);
    this.setBorder(border);

    Shape origShape = model.shapesAt(tick).get(shapeName);

    SpinnerModel xField = new SpinnerNumberModel(
        origShape.getPosition().getX(),
        0,
        model.getCanvasWidth(),
        1);

    SpinnerModel yField = new SpinnerNumberModel(
        origShape.getPosition().getY(),
        0,
        model.getCanvasHeight(),
        1);

    SpinnerModel widthField = new SpinnerNumberModel(
        origShape.getWidth(),
        0,
        model.getCanvasWidth(),
        1);

    SpinnerModel heightField = new SpinnerNumberModel(
        origShape.getHeight(),
        0,
        model.getCanvasHeight(),
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

    this.addSpinner(xField, "x");
    this.addSpinner(yField, "y");
    this.addSpinner(widthField, "width");
    this.addSpinner(heightField, "height");

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

    this.add(colorField);
  }

  /**
   * Gets the view callback.
   *
   * @return the view callback
   */
  private Consumer<EditorAction> getViewCallback() {
    return this.viewCallback;
  }

  /**
   * Adds the spinner to the model, giving it a label to go along with it.
   *
   * @param spinner the spinner to add
   * @param label   the name of the parameter to add
   */
  private void addSpinner(SpinnerModel spinner, String label) {
    JLabel spinLabel = new JLabel(label);
    this.add(spinLabel);

    JSpinner spinComp = new JSpinner(spinner);
    spinLabel.setLabelFor(spinComp);
    this.add(spinComp);
  }

  /**
   * Sets the editor view callback.
   *
   * @param viewCallback the callback for editor view modification
   */
  public void setViewCallback(Consumer<EditorAction> viewCallback) {
    this.viewCallback = viewCallback;
  }
}
