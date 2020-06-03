package com.company.model;

import com.company.model.shape.Shape;

public class FrameImpl implements Frame {
  private final double time;
  private final Shape shape;

  /**
   * Initializes all the fields in frame.
   *
   * @param time  the time the frame occurs at, in seconds
   * @param shape the shape in the frame
   */
  public FrameImpl(double time, Shape shape) {
    this.time = time;
    this.shape = shape;
  }

  @Override
  public Shape interpolateFrame(Frame to, double progress) throws IllegalArgumentException {
    if (progress < 0 || progress > 1) {
      throw new IllegalArgumentException("Progress must be between 0 and 1");
    } else if (this.compareTo(to) > 0) {
      throw new IllegalArgumentException("This frame must come before the to frame");
    }

    return this.shape.interpolate(to.getShape(), progress);
  }

  @Override
  public Shape getShape() {
    return this.shape;
  }

  @Override
  public double getTime() {
    return this.time;
  }

  @Override
  public int compareTo(Frame o) {
    return Double.compare(this.time, o.getTime());
  }
}
