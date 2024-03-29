package com.company.controller.animatoractions;

import com.company.model.AnimatorModel;
import com.company.model.shape.Shape;

/**
 * Action to change the height of a given shape.
 */
public class ChangeHeight extends AShapeModifier {
  private final double height;

  /**
   * Constructor that sets the shape name and tick for any action that modifies shapes.
   *
   * @param shapeName the name of the shape to be modified
   * @param tick      the tick at which the shape to be modified should be modified
   */
  public ChangeHeight(String shapeName, int tick, double height) {
    super(shapeName, tick);
    this.height = height;
  }

  @Override
  public void actOnUnchecked(AnimatorModel model) throws IllegalArgumentException {
    Shape shape = model.shapesAt(this.tick).get(this.shapeName);
    shape.setHeight(shape.getHeight() + height);
    model.createKeyframe(this.shapeName, shape, tick);
  }
}
