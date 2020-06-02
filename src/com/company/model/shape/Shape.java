package com.company.model.shape;

import java.awt.*;

/**
 * Represents a shape
 */
public interface Shape {
  public Posn getPosition();

  public int getWidth();

  public int getHeight();

  public Color getColor();

  public ShapeType getShapeType();
}
