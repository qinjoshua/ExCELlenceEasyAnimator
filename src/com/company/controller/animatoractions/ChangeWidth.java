package com.company.controller.animatoractions;

import com.company.model.AnimatorModel;
import com.company.model.shape.Shape;

/**
 * Action to change the width of a given shape.
 */
public class ChangeWidth extends AShapeModifier {
  private final double width;

  /**
   * Constructor that sets the shape name and tick for any action that modifies shapes.
   *
   * @param shapeName the name of the shape to be modified
   * @param tick      the tick at which the shape to be modified should be modified
   */
  public ChangeWidth(String shapeName, int tick, double width) {
    super(shapeName, tick);
    this.width = width;
  }

  @Override
  public void actOnUnchecked(AnimatorModel model) throws IllegalArgumentException {
    Shape shape = model.shapesAt(this.tick).get(this.shapeName);
    shape.setWidth(shape.getWidth() + width);
    model.createKeyframe(this.shapeName, shape, tick);
  }
}
