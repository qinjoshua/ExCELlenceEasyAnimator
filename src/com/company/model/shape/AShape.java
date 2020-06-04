package com.company.model.shape;

import java.awt.*;
import java.util.Objects;

/**
 * Abstract class that represents a shape, tracking position, width, height, and color.
 */
public abstract class AShape implements Shape {
  private final Posn posn;
  private final double width;
  private final double height;
  private final Color color;
  private final ShapeType shapeType;

  /**
   * Constructor that initializes all the fields for this shape.
   *
   * @param posn      this shape's position
   * @param width     this shape's width
   * @param height    this shape's height
   * @param color     this shape's color
   * @param shapeType this shape's type
   */
  public AShape(Posn posn, double width, double height, Color color, ShapeType shapeType) {
    this.posn = posn;
    this.width = width;
    this.height = height;
    this.color = color;
    this.shapeType = shapeType;
  }

  // Interpolates between from
  private static double interpolate(double from, double to, double progress) {
    return from + progress * (to - from);
  }

  @Override
  public Posn getPosition() {
    return this.posn;
  }

  @Override
  public double getWidth() {
    return this.width;
  }

  @Override
  public double getHeight() {
    return this.height;
  }

  @Override
  public Color getColor() {
    return this.color;
  }

  @Override
  public ShapeType getShapeType() {
    return this.shapeType;
  }

  @Override
  public Shape interpolate(Shape to, double progress) {
    if (progress < 0 || progress > 1) {
      throw new IllegalArgumentException("Progress must be between 0 and 1");
    } else if (to.getShapeType() != this.getShapeType()) {
      throw new IllegalArgumentException("To shape is not the same type as this shape");
    }

    return newShape(this.posn.interpolate(to.getPosition(), progress),
        interpolate(this.width, to.getWidth(), progress),
        interpolate(this.height, to.getHeight(), progress),
        new Color((int) interpolate(this.color.getRed(), to.getColor().getRed(), progress),
            (int) interpolate(this.color.getGreen(), to.getColor().getGreen(), progress),
            (int) interpolate(this.color.getBlue(), to.getColor().getBlue(), progress)));
  }

  /**
   * Creates a new shape with the given parameters and of the same type as this shape.
   *
   * @param posn   this shape's position
   * @param width  this shape's width
   * @param height this shape's height
   * @param color  this shape's color
   * @return the new shape
   */
  protected abstract AShape newShape(Posn posn, double width, double height, Color color);

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AShape aShape = (AShape) o;
    return Double.compare(aShape.width, width) == 0 &&
        Double.compare(aShape.height, height) == 0 &&
        posn.equals(aShape.posn) &&
        color.equals(aShape.color) &&
        shapeType == aShape.shapeType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(posn, width, height, color, shapeType);
  }

}