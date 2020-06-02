package com.company.model.shape;

import java.awt.*;

/**
 * Abstract class that represents a shape, tracking position, width, height, and color.
 */
public abstract class AShape implements Shape {
  Posn posn;
  int width;
  int height;
  Color color;
  ShapeType shapeType;

  /**
   * Constructor that initializes all the fields for this shape.
   *
   * @param posn this shape's position
   * @param width this shape's width
   * @param height this shape's height
   * @param color this shape's color
   * @param shapeType this shape's type
   */
  public AShape(Posn posn, int width, int height, Color color, ShapeType shapeType) {
    this.posn = posn;
    this.width = width;
    this.height = height;
    this.color = color;
    this.shapeType = shapeType;
  }

  @Override
  public Posn getPosition() {
    return this.posn;
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
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
}
