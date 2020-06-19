package com.company.controller.animatoractions;

import com.company.model.AnimatorModel;
import com.company.model.shape.PosnImpl;
import com.company.model.shape.Shape;

/**
 * Action to change the x-coordinates of a given shape.
 */
public class ChangeX extends AShapeModifier {
  private final double x;

  /**
   * Constructor that sets the shape name and tick for any action that modifies shapes.
   *
   * @param shapeName the name of the shape to be modified
   * @param tick      the tick at which the shape to be modified should be modified
   */
  public ChangeX(String shapeName, int tick, double x) {
    super(shapeName, tick);
    this.x = x;
  }

  @Override
  public void actOnUnchecked(AnimatorModel model) throws IllegalArgumentException {
    Shape shape = model.shapesAt(this.tick).get(this.shapeName);
    shape.setPosn(new PosnImpl(shape.getPosition().getX() + x, shape.getPosition().getY()));
    model.createKeyframe(this.shapeName, shape, tick);
  }
}
