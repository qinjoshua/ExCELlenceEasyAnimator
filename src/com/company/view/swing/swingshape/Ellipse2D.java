package com.company.view.swing.swingshape;

import java.awt.Shape;

/**
 * Represents a 2D ellipse using the Swing {@link java.awt.geom.Ellipse2D.Double} class.
 */
public class Ellipse2D implements SwingShape {
  private final int x;
  private final int y;

  /**
   * Initializes a new ellipse swing shape at the given x and y coordinates.
   *
   * @param x x-coordinate of the ellipse
   * @param y y-coordinate of the ellipse
   */
  public Ellipse2D(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public Shape createShape(double x, double y, double width, double height) {
    return new java.awt.geom.Ellipse2D.Double(
        x - this.x, y - this.y, width, height);
  }
}