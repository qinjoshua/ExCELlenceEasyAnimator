package com.company.controller.animatoractions;

import com.company.model.AnimatorModel;
import com.company.model.shape.Shape;

/**
 * Action to change the angle of a given shape.
 */
public class ChangeAngle extends AShapeModifier {
  private final double angle;

  /**
   * Constructor that sets the shape name and tick for any action that modifies shapes.
   *
   * @param shapeName the name of the shape to be modified
   * @param tick      the tick at which the shape to be modified should be modified
   */
  public ChangeAngle(String shapeName, int tick, double angle) {
    super(shapeName, tick);
    this.angle = angle;
  }

  @Override
  public void actOnUnchecked(AnimatorModel model) throws IllegalArgumentException {
    Shape shape = model.shapesAt(this.tick).get(this.shapeName);
    shape.setShapeAngle(shape.getShapeAngle() + angle);
    model.createKeyframe(this.shapeName, shape, tick);
  }
}
