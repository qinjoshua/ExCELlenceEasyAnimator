package com.company.model;
import com.company.model.shape.Shape;

/**
 * Implementation of {@link Frame} that uses a single shape and timestamp.
 */
public class FrameImpl implements Frame {
  private final double time;
  private final Shape shape;

  /**
   * Initializes all the fields in frame.
   *
   * @param time  the time the frame occurs at, in seconds
   * @param shape the shape in the frame
   * @throws IllegalArgumentException if the time is negative
   */
  public FrameImpl(double time, Shape shape) {
    if (time < 0) {
      throw new IllegalArgumentException("Time cannot be negative");
    }

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
  
  @Override
  public String toString() {
    return String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
            (int)this.getTime(),
            (int)this.getShape().getPosition().getX(),
            (int)this.getShape().getPosition().getY(),
            (int)this.getShape().getWidth(),
            (int)this.getShape().getHeight(),
            this.getShape().getColor().getRed(),
            this.getShape().getColor().getGreen(),
            this.getShape().getColor().getBlue());
  }
}
