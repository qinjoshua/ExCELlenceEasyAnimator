package com.company.model;

import com.company.model.shape.Shape;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

/**
 * Represents an animator model that computes the frames at a given time based on linear
 * interpolation of keyframes.
 */
public class AnimatorModelImpl implements AnimatorModel {
  private final SortedMap<String, SortedSet<Frame>> timelines;

  /**
   * Creates a new animator model implementation using the given sorted map of timelines.
   *
   * @param timelines a sorted map representing the timeline of the animation, mapping shape
   *                  names to lists of frames.
   */
  public AnimatorModelImpl(SortedMap<String, SortedSet<Frame>> timelines) {
    this.timelines = timelines;
  }

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
      else if (time <= frame.getValue().first().getTime()) {
        // If the time is before the first keyframe for this shape, don't draw the shape
        continue;
      }
      else {
        Frame prev = this.previousKeyframe(time, frame.getValue());
        Frame next = this.nextKeyframe(time, frame.getValue());
        double progress = (time - prev.getTime()) / (next.getTime() - prev.getTime());

        shapes.put(frame.getKey(), prev.interpolateFrame(next, progress));
      }
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

  @Override
  public void createKeyframe(String shapeName, Shape shape, double time) {

  }
}
