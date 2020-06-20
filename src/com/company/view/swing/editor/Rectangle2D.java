package com.company.view.swing.editor;

import java.awt.Shape;

/**
 * Represents a 2D Rectangle using the Swing {@link java.awt.geom.Rectangle2D.Double} class.
 */
public class Rectangle2D implements SwingShape {
  private final int x;
  private final int y;

  /**
   * Initializes a new rectangle swing shape at the given x and y coordinates.
   *
   * @param x x-coordinate of the swing shape
   * @param y y-coordinate of the swing shape
   */
  public Rectangle2D(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Creates a new rectangle with the given values in Cartesian coordinates.
   *
   * @param x      the x coordinate of the shape
   * @param y      the y coordinate of the shape
   * @param width  the width of the shape
   * @param height the height of the shape
   * @return a new rectangle with the given parameters
   */
  @Override
  public Shape createShape(double x, double y, double width, double height) {
    // subtract the y given by the height to account for the fact that it's given in Cartesian
    // coordinates
    return new java.awt.geom.Rectangle2D.Double(
        x - this.x, y - this.y, width, height);
  }
}
