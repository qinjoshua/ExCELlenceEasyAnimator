package com.company.controller.animatoractions;

import com.company.model.AnimatorModel;
import com.company.model.shape.Shape;

import java.awt.Color;

/**
 * An action to modify the color of a particular shape at a particular time.
 */
public class ChangeColor extends AShapeModifier {
  Color newColor;

  /**
   * Constructs a new action to change the color of the given shape at the given tick to the given
   * color.
   *
   * @param shapeName the name of the shape to edit
   * @param tick      the time to edit the shape at
   * @param newColor  the new color to use
   */
  public ChangeColor(String shapeName, int tick, Color newColor) {
    super(shapeName, tick);
    this.newColor = newColor;
  }

  /**
   * Changes the color of the specific shape.
   *
   * @param model the model to change the shape for
   * @throws IllegalArgumentException if the shape-tick combo isn't a keyframe
   */
  @Override
  protected void actOnUnchecked(AnimatorModel model) throws IllegalArgumentException {
    Shape shape = model.shapesAt(this.tick).get(this.shapeName);
    shape.setColor(newColor);
    model.createKeyframe(this.shapeName, shape, tick);
  }
}
