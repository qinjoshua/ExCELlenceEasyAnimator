package com.company.controller.animatoractions;

import com.company.model.AnimatorModel;

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
    model.createKeyframe(this.shapeName, model.shapesAt(this.tick).get(this.shapeName),
            this.tick);
  }
}
