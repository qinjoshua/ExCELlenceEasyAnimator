package com.company.controller;

import com.company.model.AnimatorModel;

/**
 * Interface representing a controller for an animation.
 */
public interface AnimatorController {
  /**
   * Plays the animation.
   *
   * @param m An animator model that this controller is controlling.
   */
  void playAnimation(AnimatorModel m);
}
