package com.company.controller.animatoractions;

import com.company.model.AnimatorModel;

import java.util.function.Consumer;

/**
 * Implementation of an action handler that runs all the given actions on a model.
 */
public class AnimatorActionConsumerImpl implements Consumer<AnimatorAction> {
  AnimatorModel model;

  /**
   * Creates an action handler with the given model.
   *
   * @param model the model that future actions will act on
   */
  public AnimatorActionConsumerImpl(AnimatorModel model) {
    this.model = model;
  }

  @Override
  public void accept(AnimatorAction animatorAction) {
    animatorAction.actOn(this.model);
  }
}