package com.company.model;

import com.company.model.shape.Shape;

import java.util.SortedMap;
import java.util.SortedSet;

/**
 * This interface represents a read-only version of a model that represents an animation as the
 * interpolation of objects between key time frames.
 */
public interface ReadOnlyAnimatorModel {
  /**
   * Gets the state of each shape at a given time, excluding the ones that have not been drawn yet.
   *
   * @param time time in seconds of the desired scene
   * @return A map of the names and shapes representing the contents of a scene
   * @throws IllegalArgumentException if the time is negative
   */
  SortedMap<String, Shape> shapesAt(double time) throws IllegalArgumentException;

  /**
   * Returns a sorted map of all the keyframes of every shape in order, representing the overall
   * timeline for the animation.
   *
   * @return Sorted map that maps shape names and their keyframes, in order as they appear on screen
   */
  SortedMap<String, SortedSet<Frame>> getKeyframes();
}
