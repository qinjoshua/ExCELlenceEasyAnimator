package com.company.controller.animatoractions;

import com.company.model.AnimatorModel;

/**
 * Abstract class that represents a command that modifies a shape at a given tick.
 */
public abstract class AShapeModifier implements AnimatorAction {
  protected final String shapeName;
  protected final int tick;

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
    try {
      this.actOnUnchecked(model);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Editor action failed: " + e.getMessage());
    }
  }

  protected abstract void actOnUnchecked(AnimatorModel model) throws IllegalArgumentException;

}
