package com.company.view.swing;

import java.awt.Color;

/**
 * Struct-like class that stores a color associated with a shape, since Java's awt shapes don't
 * have a color.
 */
public class DecoratedShape {
  public Color color;
  public java.awt.Shape shape;
  public double angle;

  public DecoratedShape(Color color, java.awt.Shape shape, double angle) {
    this.color = color;
    this.shape = shape;
    this.angle = angle;
  }
}