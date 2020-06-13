package com.company.view.swing.SwingShape;

import java.awt.*;

/**
 * Represents a specific way of creating Swing graphics modeling a specific kind of shape.
 */
public interface SwingShape {
  /**
   * Given the requisite parameters, generates the shape of this type with the given data.
   *
   * @param x the x coordinate of the shape
   * @param y the y coordinate of the shape
   * @param width the width of the shape
   * @param height the height of the shape
   */
  Shape createShape(double x, double y, double width, double height);
}
