package com.company.controller.animatoractions;

import com.company.model.AnimatorModel;

/**
 * Action to remove a keyframe from the animation.
 */
public class RemoveKeyframe extends AShapeModifier {
  /**
   * Constructor that sets the shape name and tick for any action that modifies shapes.
   *
   * @param shapeName the name of the shape to be modified
   * @param tick      the tick at which the shape to be modified should be modified
   */
  public RemoveKeyframe(String shapeName, int tick) {
    super(shapeName, tick);
  }

  @Override
  protected void actOnUnchecked(AnimatorModel model) throws IllegalArgumentException {
    model.removeKeyframe(shapeName, tick);
  }
}
