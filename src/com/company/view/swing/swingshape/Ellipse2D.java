package com.company.view.swing.swingshape;

import java.awt.Shape;

/**
 * Represents a 2D ellipse using the Swing {@link java.awt.geom.Ellipse2D.Double} class.
 */
public class Ellipse2D implements SwingShape {
  @Override
  public Shape createShape(double x, double y, double width, double height) {
    return new java.awt.geom.Ellipse2D.Double(x, y, width, height);
  }
}