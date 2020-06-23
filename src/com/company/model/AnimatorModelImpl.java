package com.company.model;

import com.company.model.shape.PosnImpl;
import com.company.model.shape.Shape;
import com.company.model.shape.ShapeType;
import com.company.util.AnimationBuilder;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Represents an animator model that computes the frames at a given time based on linear
 * interpolation of keyframes.
 */
public class AnimatorModelImpl implements AnimatorModel {
  private final Map<String, NavigableSet<Frame>> timelines;
  private final Map<String, Layer> layers;

  private int canvasWidth;
  private int canvasHeight;

  private int canvasX;
  private int canvasY;

  /**
   * Default constructor that does not initialize any shapes.
   */
  public AnimatorModelImpl() {
    this.timelines = new LinkedHashMap<>();
    this.layers = new LinkedHashMap<>();

    this.canvasWidth = 640;
    this.canvasHeight = 400;

    this.canvasX = 0;
    this.canvasY = 0;
  }

  @Override
  public Map<String, Shape> shapesAt(int tick) {
    if (tick < 0) {
      throw new IllegalArgumentException("Time cannot be negative");
    }

    Map<String, Shape> shapes = new LinkedHashMap<>();

    for (Map.Entry<String, NavigableSet<Frame>> frame : timelines.entrySet()) {
      Frame prevFrame = frame.getValue().floor(new FrameImpl(tick, null));
      Frame nextFrame = frame.getValue().higher(new FrameImpl(tick, null));
      if (prevFrame != null && nextFrame == null) {
        // no frame after this time, use latest frame
        shapes.put(frame.getKey(), prevFrame.getShape());
      } else if (prevFrame != null) {
        // both frames exist, interpolate
        shapes.put(frame.getKey(), prevFrame.interpolateShape(
            nextFrame, (tick - prevFrame.getTime()) / (nextFrame.getTime() - prevFrame.getTime())));
      }
      // If the time is before the first keyframe for this shape, don't draw the shape
    }

    return shapes;
  }

  @Override
  public Map<String, SortedSet<Frame>> getKeyframes() {
    return new LinkedHashMap<>(this.timelines);
  }

  /**
   * Adds a new keyframe at the given time, with the given shape. This will overwrite any existing
   * keyframes at the existing time, which is defined with nanosecond precision: any keyframe
   * nanosecond or closer to the given time will be replaced with this new one.
   *
   * @param shapeName The name of the shape for which the keyframe is being created for
   * @param shape     The shape in the keyframe
   * @param tick      The time the keyframe is at
   */
  @Override
  public void createKeyframe(String shapeName, Shape shape, int tick) {
    if (tick < 0) {
      throw new IllegalArgumentException("Time cannot be negative");
    }

    if (this.timelines.containsKey(shapeName)) {

      SortedSet<Frame> timeline = timelines.get(shapeName);

      if (timeline.first().getShape().getShapeType() != shape.getShapeType()) {
        throw new IllegalArgumentException("Shape is not the same type as other keyframes.");
      }

      timeline.removeIf(frame -> Math.abs(frame.getTime() - tick) < 1e-9);

      this.timelines.get(shapeName).add(new FrameImpl(tick, shape));
    } else {
      TreeSet<Frame> newFrames = new TreeSet<>();
      newFrames.add(new FrameImpl(tick, shape));

      this.timelines.put(shapeName, newFrames);
    }
  }

  @Override
  public int getCanvasWidth() {
    return canvasWidth;
  }

  @Override
  public void setCanvasWidth(int canvasWidth) {
    this.canvasWidth = canvasWidth;
  }

  @Override
  public int getCanvasHeight() {
    return canvasHeight;
  }

  @Override
  public void setCanvasHeight(int canvasHeight) {
    this.canvasHeight = canvasHeight;
  }

  @Override
  public int getCanvasX() {
    return canvasX;
  }

  @Override
  public void setCanvasX(int canvasX) {
    this.canvasX = canvasX;
  }

  @Override
  public int getCanvasY() {
    return canvasY;
  }

  @Override
  public void setCanvasY(int canvasY) {
    this.canvasY = canvasY;
  }

  @Override
  public void removeKeyframe(String shapeName, int tick) {
    SortedSet<Frame> frames = this.timelines.get(shapeName);
    for (Frame frame : frames) {
      if (frame.getTime() == tick) {
        frames.remove(frame);
        if (frames.size() == 0) {
          this.deleteShape(shapeName);
        }
        return;
      }
    }
    throw new IllegalArgumentException("A keyframe did not exist for the given shape at the given" +
        " tick.");
  }

  @Override
  public void deleteShape(String shapeName) {
    if (timelines.containsKey(shapeName)) {
      timelines.remove(shapeName);
    } else {
      throw new IllegalArgumentException("Cannot remove nonexistent shape " + shapeName);
    }
  }

  @Override
  public void moveLayerUp(String layerName) {

  }

  @Override
  public void moveLayerDown(String layerName) {

  }

  @Override
  public void addLayer(String layerName) {

  }

  @Override
  public void deleteLayer(String layerName) {

  }

  @Override
  public double lastTick() {
    Frame maxFrame = new FrameImpl(1, null);
    for (SortedSet<Frame> allFrames : this.timelines.values()) {
      Frame newLastFrame = Collections.max(allFrames);
      if (maxFrame.compareTo(newLastFrame) < 0) {
        maxFrame = newLastFrame;
      }
    }
    return maxFrame.getTime();
  }

  @Override
  public List<String> getShapesInLayer(String layerName) {
    return null;
  }

  /**
   * Inner class that allows you to build an animator model implementation.
   */
  public static final class Builder implements AnimationBuilder<AnimatorModel> {
    private final AnimatorModel model;
    private final Map<String, ShapeType> shapeTypes;

    /**
     * Creates a new builder with a default empty model.
     */
    public Builder() {
      this.model = new AnimatorModelImpl();
      this.shapeTypes = new HashMap<>();
    }

    @Override
    public AnimatorModel build() {
      return this.model;
    }

    @Override
    public AnimationBuilder<AnimatorModel> setBounds(int x, int y, int width, int height) {
      model.setCanvasX(x);
      model.setCanvasY(y);
      model.setCanvasWidth(width);
      model.setCanvasHeight(height);

      return this;
    }

    @Override
    public AnimationBuilder<AnimatorModel> declareShape(String name, String type) {
      this.shapeTypes.put(name, ShapeType.getShapeTypeFromString(type));
      return this;
    }

    @Override
    public AnimationBuilder<AnimatorModel> addMotion(
        String name,
        int t1, int x1, int y1, int w1, int h1, int r1, int g1, int b1, double a1,
        int t2, int x2, int y2, int w2, int h2, int r2, int g2, int b2, double a2) {
      return addKeyframe(name, t1, x1, y1, w1, h1, r1, g1, b1, a1)
          .addKeyframe(name, t2, x2, y2, w2, h2, r2, g2, b2, a2);
    }

    @Override
    public AnimationBuilder<AnimatorModel> addKeyframe(
        String name, int t, int x, int y, int w, int h, int r, int g, int b, double a) {
      Shape newShape = shapeTypes.get(name).getShape(new PosnImpl(x, y), w, h, new Color(r, g, b)
              , a);
      this.model.createKeyframe(name, newShape, t);
      return this;
    }
  }

  /**
   * A class to hold both depth information and the shapes information layers have: what order
   * this layer is drawn in compared to other layers and what shapes comprise this layer.
   */
  private static class Layer implements Comparable<Layer> {
    // The drawing order: layers with earlier orders are drawn first and are occluded by later
    // layers.
    private int order;
    // The names of each shape in this layer.
    private final List<String> names;

    /**
     * Constructs a layer.
     * @param order the order this layer is drawn in compared to others
     */
    public Layer(int order) {
      this.order = order;
      this.names = new ArrayList<String>();
    }

    /**
     * Gets the drawing order.
     * @return the drawing order
     */
    public int getOrder() {
      return order;
    }

    /**
     * Sets the drawing order.
     * @param order the drawing order to set
     */
    public void setOrder(int order) {
      this.order = order;
    }

    /**
     * Gets the names in this shape.
     * @return the names in this shape
     */
    public List<String> getNames() {
      return names;
    }

    /**

    /**
     * Removes the shape with the given name if that shape is in this layer.
     * @param shapeName the shape name to possibly remove
     */
    public void removeShapeIfPresent(String shapeName) {
      names.remove(shapeName);
    }

    @Override
    public int compareTo(Layer o) {
      return 0;
    }
  }
}