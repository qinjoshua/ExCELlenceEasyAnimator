package com.company.controller.animatoractions;

import com.company.model.AnimatorModel;

/**
 * Represents an action that can be performed on a model.
 */
public interface AnimatorAction {

  /**
   * Runs the action on the given model.
   *
   * @param model the model for an action to be run on
   * @throws IllegalStateException if the command was unable to execute
   */
  void actOn(AnimatorModel model) throws IllegalStateException;

}
