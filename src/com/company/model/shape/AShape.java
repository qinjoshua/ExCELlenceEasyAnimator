package com.company.model.shape;

import java.awt.Color;
import java.util.Objects;

/**
 * Abstract class that represents a shape, tracking position, width, height, and color.
 */
public abstract class AShape implements Shape {
  private Posn posn;
  private double width;
  private double height;
  private Color color;
  private ShapeType shapeType;
  private double angle;

  /**
   * Constructor that initializes all the fields for this shape.
   *
   * @param posn      this shape's position
   * @param width     this shape's width
   * @param height    this shape's height
   * @param color     this shape's color
   * @param shapeType this shape's type
   * @param angle     this shape's angle, in radians
   */
  public AShape(Posn posn, double width, double height, Color color, ShapeType shapeType,
                double angle) {
    this.posn = posn;
    this.width = width;
    this.height = height;
    this.color = color;
    this.shapeType = shapeType;
    this.angle = angle;
  }

  /**
   * Constructor that initializes all the fields for this shape except angle, which is set to
   * zero.
   *
   * @param posn      this shape's position
   * @param width     this shape's width
   * @param height    this shape's height
   * @param color     this shape's color
   * @param shapeType this shape's type
   */
  public AShape(Posn posn, double width, double height, Color color, ShapeType shapeType) {
    this(posn, width, height, color, shapeType, 0);
  }

  /**
   * Interpolates between the given from and to numbers, with the given progress.
   *
   * @param from     the start number
   * @param to       the end number
   * @param progress the weight to assign to the end number in the weighted average
   * @return a weighted average, {@code (1 - progress) * from + progress * to}
   */
  private static double interpolateNum(double from, double to, double progress) {
    return from + progress * (to - from);
  }

  @Override
  public Shape copy() {
    return this.newShape(this.posn, this.width, this.height, this.color, angle);
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
  public void setWidth(double width) {
    this.width = width;
  }

  @Override
  public double getHeight() {
    return this.height;
  }

  @Override
  public void setHeight(double height) {
    this.height = height;
  }

  @Override
  public Color getColor() {
    return this.color;
  }

  @Override
  public void setColor(Color color) {
    this.color = color;
  }

  @Override
  public ShapeType getShapeType() {
    return this.shapeType;
  }

  @Override
  public void setShapeType(ShapeType shapeType) {
    this.shapeType = shapeType;
  }

  @Override
  public void setPosn(Posn posn) {
    this.posn = posn;
  }

  @Override
  public double getShapeAngle() {
    return this.angle;
  }

  @Override
  public void setShapeAngle(double angle) {
    this.angle = angle;
  }

  @Override
  public Shape interpolate(Shape to, double progress) {
    if (progress < 0 || progress > 1) {
      throw new IllegalArgumentException("Progress must be between 0 and 1");
    } else if (to.getShapeType() != this.getShapeType()) {
      throw new IllegalArgumentException("To shape is not the same type as this shape");
    }

    return newShape(this.posn.interpolate(to.getPosition(), progress),
            interpolateNum(this.width, to.getWidth(), progress),
            interpolateNum(this.height, to.getHeight(), progress),
            new Color(
                    (int) Math.round(interpolateNum(this.color.getRed(), to.getColor().getRed(),
                            progress)),
                    (int) Math.round(
                            interpolateNum(
                                    this.color.getGreen(), to.getColor().getGreen(), progress)),
                    (int) Math.round(interpolateNum(this.color.getBlue(), to.getColor().getBlue(),
                            progress))),
            interpolateNum(this.angle, to.getShapeAngle(), progress));
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
  protected abstract AShape newShape(Posn posn, double width, double height, Color color,
                                     double angle);

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
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

  @Override
  public String toString() {
    return "AShape{" +
        "posn=" + posn +
        ", width=" + width +
        ", height=" + height +
        ", color=" + color +
        ", shapeType=" + shapeType +
        '}';
  }
}