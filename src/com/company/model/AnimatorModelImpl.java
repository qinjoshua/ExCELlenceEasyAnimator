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
   * @param timelines
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
  public Map<String, Shape> shapesAt(double time) {
    return null;
  }

  @Override
  public void createKeyframe(String shapeName, Shape shape, double time) {

  }
}
