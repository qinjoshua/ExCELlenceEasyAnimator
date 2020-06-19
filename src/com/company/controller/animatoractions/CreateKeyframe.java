package com.company.controller.animatoractions;

import com.company.model.AnimatorModel;
import com.company.model.shape.Shape;

/**
 * Creates a keyframe in the model.
 */
public class CreateKeyframe extends AShapeModifier implements AnimatorAction {

  /**
   * Constructor that sets the shape name and tick for any action that modifies shapes.
   *
   * @param shapeName the name of the shape to be modified
   * @param tick      the tick at which the shape to be modified should be modified
   */
  public CreateKeyframe(String shapeName, int tick) {
    super(shapeName, tick);
  }

  @Override
  public boolean checkValidKeyframe(AnimatorModel model) {
    return true;
  }

  @Override
  protected void actOnUnchecked(AnimatorModel model) throws IllegalArgumentException {
    Shape newShape = model.shapesAt(this.tick).get(this.shapeName).copy();
    if (newShape == null) {
      // before first keyframe, initialize to first keyframe instead
      newShape = model.getKeyframes().get(shapeName).first().getShape().copy();
    }
    model.createKeyframe(this.shapeName, newShape, this.tick);
  }
}
