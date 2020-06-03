package com.company.model;

import com.company.model.shape.Shape;

/**
 * Interface representing a keyframe in an animation. It keeps track of the time the frame is on
 * and the state of the shape at that time.
 */
interface Frame extends Comparable<Frame> {

  /**
   * Interpolates a frame between this frame and the given one.
   *
   * @param to the frame that this frame is transitioning to; its time should be after this one.
   * @param progress a number between 0 and 1 that represents the progress between this frame and
   *                the to frame
   * @return a shape representing the contents of a scene
   * @throws IllegalArgumentException if progress is outside of the interval 0 and 1 or if the to
   * frame is before this frame.
   */
  Shape interpolateFrame (Frame to, double progress) throws IllegalArgumentException;

  /**
   * Gets the shape for this frame.
   *
   * @return the shape for this frame
   */
  Shape getShape();

  /*
   * Gets the time for this frame.
   *
   * @return the time for this frame
   */
  double getTime();
}
