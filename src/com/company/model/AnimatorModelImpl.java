package com.company.model;

import com.company.model.shape.Shape;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Represents an animator model that computes the frames at a given time based on linear
 * interpolation of keyframes.
 */
public class AnimatorModelImpl implements AnimatorModel {
  private final SortedMap<String, List<Frame>> timelines;

  /**
   * Creates a new animator model implementation using the given sorted map of timelines.
   *
   * @param timelines a sorted map representing the timeline of the animation, mapping shape
   *                  names to lists of frames.
   */
  public AnimatorModelImpl(SortedMap<String, List<Frame>> timelines) {
    this.timelines = timelines;
  }

  /**
   * Default constructor that does not initialize any shapes.
   */
  public AnimatorModelImpl() {
    this.timelines = new TreeMap<String, List<Frame>>();
  }

  @Override
  public SortedMap<String, Shape> shapesAt(double time) {

    SortedMap<String, Shape> shapes = new TreeMap<String, Shape>();

    for (Map.Entry<String, List<Frame>> frame : timelines.entrySet()) {
      // TODO does this maintain order? IDK


    }

    return null;
  }

  // Returns the nearest previous keyframe to
  private double previousKeyframeTime(double time, List<Frame> frames) {
    double closestTime = time;

    for (Frame frame : frames) {
      if (frame.getTime() - time < 0 && Math.abs(frame.getTime() - time) < closestTime) {
        return frame.getTime();
      }
    }

    return 0.0;

  }

  @Override
  public void createKeyframe(String shapeName, Shape shape, double time) {

  }
}
