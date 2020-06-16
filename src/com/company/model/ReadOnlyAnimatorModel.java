package com.company.model;

import com.company.model.shape.Shape;

import java.util.Map;
import java.util.SortedSet;

/**
 * This interface represents a read-only version of a model that represents an animation as the
 * interpolation of objects between key time frames.
 */
public interface ReadOnlyAnimatorModel {
  /**
   * Gets the state of each shape at a given tick, excluding the ones that have not been drawn yet.
   *
   * @param tick tick of the desired scene
   * @return A map of the names and shapes representing the contents of a scene
   * @throws IllegalArgumentException if the tick is negative
   */
  Map<String, Shape> shapesAt(int tick) throws IllegalArgumentException;

  /**
   * Returns a sorted map of all the keyframes of every shape in order, representing the overall
   * timeline for the animation.
   *
   * @return Sorted map that maps shape names and their keyframes, in order as they appear on screen
   */
  Map<String, SortedSet<Frame>> getKeyframes();

  /**
   * Gets the height of the canvas.
   *
   * @return the canvas height as an integer
   */
  int getCanvasHeight();

  /**
   * Gets the width of the canvas.
   *
   * @return the canvas width as an integer
   */
  int getCanvasWidth();

  /**
   * Gets the x coordinate of the canvas origin.
   *
   * @return the x coodinate of the canvas origin.
   */
  int getCanvasX();

  /**
   * Gets the y coordinate of the canvas origin.
   *
   * @return the y coodinate of the canvas origin.
   */
  int getCanvasY();
}
