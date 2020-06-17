package com.company.controller.animatoractions;

import com.company.model.AnimatorModel;
import com.company.model.shape.PosnImpl;
import com.company.model.shape.Shape;

public class ChangeY extends AShapeModifier {
  private final int y;

  /**
   * Constructor that sets the shape name and tick for any action that modifies shapes.
   *
   * @param shapeName the name of the shape to be modified
   * @param tick      the tick at which the shape to be modified should be modified
   */
  public ChangeY(String shapeName, int tick, int y) {
    super(shapeName, tick);
    this.y = y;
  }

  @Override
  public void actOnUnchecked(AnimatorModel model) throws IllegalArgumentException {
    Shape shape = model.shapesAt(this.tick).get(this.shapeName);
    shape.setPosn(new PosnImpl(shape.getPosition().getX(), shape.getPosition().getY() + y));
    model.createKeyframe(this.shapeName, shape, tick);
  }
}
