package com.company.model;

import com.company.model.shape.Shape;

import java.util.SortedMap;

/**
 * This interface represents the model for an animation that allows adding keyframes for specific
 * types of objects, and getting the state of the animation at a specific time.
 */
public interface AnimatorModel {
  /**
   * Gets the state of each shape at a given time, excluding the ones that have not been drawn yet.
   *
   * @param time time in seconds of the desired scene
   * @return A map of the names and shapes representing the contents of a scene
   * @throws IllegalArgumentException if the time is negative
   */
  SortedMap<String, Shape> shapesAt(double time) throws IllegalArgumentException;

  /**
   * Adds a new keyframe at the given time, with the given shape. This will overwrite any existing
   * keyframes at the existing position.
   *
   * @param shapeName The name of the shape for which the keyframe is being created for.
   * @param shape     The shape in the keyframe.
   * @param time      The time the keyframe is at.
   * @throws IllegalArgumentException if the time is negative, or if the shape is not the same type
   *                                  as the other keyframes for this shape name
   */
  void createKeyframe(String shapeName, Shape shape, double time) throws IllegalArgumentException;
}
