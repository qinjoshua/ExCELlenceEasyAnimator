package com.company.controller.animatoractions;

import com.company.model.AnimatorModel;

/**
 * Action that deletes a given shape from the animation entirely. Cannot be undone!
 */
public class DeleteShape implements AnimatorAction {
  final String shapeName;

  /**
   * Initializes an action to delete the given shape from the model.
   *
   * @param shapeName the name of the shape to delete.
   */
  public DeleteShape(String shapeName) {
    this.shapeName = shapeName;
  }

  @Override
  public void actOn(AnimatorModel model) throws IllegalStateException {
    try {
      model.deleteShape(shapeName);
    } catch (IllegalArgumentException e) {
      throw new IllegalStateException("Deleting shape failed: " + e.getMessage());
    }
  }
}
