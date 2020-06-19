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
  protected void actOnUnchecked(AnimatorModel model) throws IllegalArgumentException {
    Shape newShape;
    if (!model.shapesAt(this.tick).containsKey(shapeName)) {
      // before first keyframe, initialize to first keyframe instead
      newShape = model.getKeyframes().get(shapeName).first().getShape().copy();
    } else {
      newShape = model.shapesAt(this.tick).get(this.shapeName).copy();
    }
    model.createKeyframe(this.shapeName, newShape, this.tick);
  }
}
