package com.company.model;

import com.company.model.shape.Shape;

import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Represents an animator model that computes the frames at a given time based on linear
 * interpolation of keyframes.
 */
public class AnimatorModelImpl implements AnimatorModel {
  private final SortedMap<String, SortedSet<Frame>> timelines;

  /**
   * Default constructor that does not initialize any shapes.
   */
  public AnimatorModelImpl() {
    this.timelines = new TreeMap<>();
  }

  @Override
  public SortedMap<String, Shape> shapesAt(double time) {

    SortedMap<String, Shape> shapes = new TreeMap<>();

    for (Map.Entry<String, SortedSet<Frame>> frame : timelines.entrySet()) {
      if (time >= frame.getValue().last().getTime()) {
        shapes.put(frame.getKey(), frame.getValue().last().getShape());
      }
      else if (!(time <= frame.getValue().first().getTime())) {
        Frame prev = previousKeyframe(time, frame.getValue());
        Frame next = nextKeyframe(time, frame.getValue());
        double progress = (time - prev.getTime()) / (next.getTime() - prev.getTime());

        shapes.put(frame.getKey(), prev.interpolateFrame(next, progress));
      }
      // If the time is before the first keyframe for this shape, don't draw the shape
    }

    return shapes;
  }

  // Returns the latest keyframe whose time is strictly less than the given time
  private static Frame previousKeyframe(double time, SortedSet<Frame> frames) {
    Frame closestFrame = frames.first();

    for (Frame frame : frames) {
      if (frame.getTime() >= time) {
        return closestFrame;
      }
      closestFrame = frame;
    }

    return frames.last();
  }

  // Returns the earliest keyframe whose time is greater or equal to the given time
  private static Frame nextKeyframe(double time, SortedSet<Frame> frames) {
    for (Frame frame : frames) {
      if (frame.getTime() >= time) {
        return frame;
      }
    }

    return frames.last();
  }

  /**
   * This implementation of createKeyframe will overwrite any existing keyframes that are within
   * 1e-9 seconds apart in time, otherwise behaves like createKeyframe does.
   *
   * @param shapeName The name of the shape for which the keyframe is being created for.
   * @param shape     The shape in the keyframe.
   * @param time      The time the keyframe is at.
   */
  @Override
  public void createKeyframe(String shapeName, Shape shape, double time) {
    if (time < 0) {
      throw new IllegalArgumentException("Time cannot be negative");
    }

    if (this.timelines.containsKey(shapeName)) {

      SortedSet<Frame> timeline = timelines.get(shapeName);

      if (timeline.first().getShape().getShapeType() != shape.getShapeType()) {
        throw new IllegalArgumentException("Shape is not the same type as other keyframes.");
      }

      timeline.removeIf(frame -> Math.abs(frame.getTime() - time) < 1e-9);

      this.timelines.get(shapeName).add(new FrameImpl(time, shape));
    } else {
      TreeSet<Frame> newFrames = new TreeSet<>();
      newFrames.add(new FrameImpl(time, shape));

      this.timelines.put(shapeName, newFrames);
    }
  }
}
