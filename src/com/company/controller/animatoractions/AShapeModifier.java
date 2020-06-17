package com.company.controller.animatoractions;

import com.company.model.AnimatorModel;
import com.company.model.Frame;

import java.util.SortedSet;

/**
 * Abstract class that represents a command that modifies a shape at a given tick.
 */
public abstract class AShapeModifier implements AnimatorAction {
  protected String shapeName;
  protected int tick;

  /**
   * Constructor that sets the shape name and tick for any action that modifies shapes.
   *
   * @param shapeName the name of the shape to be modified
   * @param tick      the tick at which the shape to be modified should be modified
   */
  public AShapeModifier(String shapeName, int tick) {
    this.shapeName = shapeName;
    this.tick = tick;
  }

  @Override
  public void actOn(AnimatorModel model) throws IllegalStateException {
//    if(!this.checkValidKeyframe(model)) {
//      throw new IllegalStateException("Attempted to modify invalid keyframe.");
//    }
    try {
      this.actOnUnchecked(model);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Editor action failed: " + e.getMessage());
    }
  }

  protected abstract void actOnUnchecked(AnimatorModel model) throws IllegalArgumentException;

  /**
   * Checks to see if this action's shape name and tick contain a valid keyframe.
   *
   * @param model the model to check for the given shape
   * @return true if the shape has a keyframe at that tick in the model, false otherwise
   */
  protected boolean checkValidKeyframe(AnimatorModel model) {
    if (model.getKeyframes().containsKey(shapeName)) {
      SortedSet<Frame> frames = model.getKeyframes().get(shapeName);
      for (Frame frame : frames) {
        if (frame.getTime() == this.tick) {
          return true;
        }
      }
    }
    return false;
  }
}
