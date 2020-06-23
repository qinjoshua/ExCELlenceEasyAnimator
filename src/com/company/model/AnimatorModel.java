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
   * @param tick      The tick the keyframe is at
   * @throws IllegalArgumentException if the tick is negative, or if the shape is not the same type
   *                                  as the other keyframes for this shape name
   */
  void createKeyframe(String shapeName, Shape shape, int tick) throws IllegalArgumentException;

  /**
   * Sets the width of the canvas.
   *
   * @param canvasWidth width of the canvas to be set
   */
  void setCanvasWidth(int canvasWidth);

  /**
   * Sets the height of the canvas.
   *
   * @param canvasHeight height of the canvas to be set
   */
  void setCanvasHeight(int canvasHeight);

  /**
   * Sets the x coordinate of the origin.
   *
   * @param canvasX x coordinate of the origin to be set
   */
  void setCanvasX(int canvasX);

  /**
   * Sets the y coordinate of the origin.
   *
   * @param canvasY y coordinate of the origin to be set
   */
  void setCanvasY(int canvasY);

  /**
   * Removes the keyframe for the given shape at the given point in time.
   *
   * @param shapeName name of the shape whose keyframe is to be removed
   * @param tick      tick at which to remove the keyframe
   * @throws IllegalArgumentException if the keyframe does not exist for the given shape at the
   *                                  given tick
   */
  void removeKeyframe(String shapeName, int tick);

  /**
   * Deletes the shape from the model entirely.
   *
   * @param shapeName the name of the shape to delete
   * @throws IllegalArgumentException if the shape doesn't exist
   */
  void deleteShape(String shapeName);

  /**
   * Moves the layer with the given name up one level.
   * @param layerName the name of the layer to move up
   * @throws IllegalArgumentException if the layer name does not exist
   */
  void moveLayerUp(String layerName);

  /**
   * Moves the layer with the given name down one level.
   * @param layerName the name of the layer to move down
   * @throws IllegalArgumentException if the layer name does not exist
   */
  void moveLayerDown(String layerName);

  /**
   * Adds an empty later with the given name.
   * @param layerName the name of the layer to add
   * @throws IllegalArgumentException if the name already exists
   */
  void addLayer(String layerName);

  /**
   * Deletes the layer with the given name and all of its shapes.
   * @param layerName the name of the layer to add
   * @throws IllegalArgumentException if the name already exists
   */
  void deleteLayer(String layerName);
}