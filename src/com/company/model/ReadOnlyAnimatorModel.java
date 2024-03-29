package com.company.model;

import com.company.model.shape.Shape;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

/**
 * This interface represents a read-only version of a model that represents an animation as the
 * interpolation of objects between key time frames.
 */
public interface ReadOnlyAnimatorModel {
  /**
   * Gets the state of each shape at a given tick, excluding the ones that have not been drawn
   * yet. The map iteration order is the same as the correct drawing order.
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

  /**
   * Returns the tick of the last current keyframe: after this time, the animation doesn't change.
   *
   * @return the last tick of the animation, or 1 if no keyframes exist
   */
  double lastTick();

  /**
   * Returns the list of shape names in the given layer name, in correct drawing order (so shapes
   * at the start will be obscured by shapes at the end).
   *
   * @param layerName the name of the layer
   * @return the list of shapes in that layer
   * @throws IllegalArgumentException if the layer name does not exist
   */
  List<String> getShapesInLayer(String layerName);

  /**
   * Returns an ordered collection of layer names in the model.
   *
   * @return the list of layers in the model
   */
  Collection<String> getLayers();
}
