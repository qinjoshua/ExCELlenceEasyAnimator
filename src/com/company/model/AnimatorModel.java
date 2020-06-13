package com.company.model;

import com.company.model.shape.Shape;

/**
 * This interface represents the model for an animation that allows adding keyframes for specific
 * types of objects, and getting the state of the animation at a specific time.
 */
public interface AnimatorModel extends ReadOnlyAnimatorModel {
  /**
   * keyframes at the existing position.
   *
   * @param shapeName The name of the shape for which the keyframe is being created for
   * @param shape     The shape in the keyframe
   * @param time      The time the keyframe is at
   * @throws IllegalArgumentException if the time is negative, or if the shape is not the same type
   *                                  as the other keyframes for this shape name
   */
  void createKeyframe(String shapeName, Shape shape, double time) throws IllegalArgumentException;
}