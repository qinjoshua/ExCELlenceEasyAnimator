package com.company.model.shape;

import com.company.model.shape.shapes.Ellipse;
import com.company.model.shape.shapes.Rectangle;

import java.awt.Color;

/**
 * An enumeration representing all the different types of shapes, along with associated data
 * necessary to specify them.
 */
public enum ShapeType {
  Rectangle("rectangle", "rect"),
  Ellipse("ellipse", "ellipse");

  private final String disp;
  private final String svgName;

  // CHANGELOG: added the svgID field to the constructor
  ShapeType(String disp, String svgName) {
    this.disp = disp;
    this.svgName = svgName;
  }

  @Override
  public String toString() {
    return this.disp;
  }

  /**
   * Returns the name of this shape when represented by an SVG.
   *
   * @return string of the name of this shape when represented in an SVG.
   */
  // CHANGELOG: Added public method SVGname in order to obtain the SVG name name for this shape
  public String SVGname() {
    return this.svgName;
  }

  /**
   * Gets a shapeType from its name.
   *
   * @param shapeTypeName the name of the shape type
   * @return the shape type
   * @throws IllegalArgumentException when the shape type name does not match any known shape types
   */
  public static ShapeType getShapeTypeFromString(String shapeTypeName) {
    if (shapeTypeName.equalsIgnoreCase("rectangle")) {
      return Rectangle;
    } else if (shapeTypeName.equalsIgnoreCase("ellipse")) {
      return Ellipse;
    } else {
      throw new IllegalArgumentException("Not a recognized shape name.");
    }
  }

  /**
   * Factory method that returns a shape of this type with the given parameters.
   *
   * @param posn   the shape's position
   * @param width  the shape's width
   * @param height the shape's height
   * @param color  the shape's color
   * @return shape of this type with the given parameters
   */
  public Shape getShape(
      Posn posn, double width, double height, Color color) {
    switch (this) {
      case Rectangle:
        return new Rectangle(posn, width, height, color);
      case Ellipse:
        return new Ellipse(posn, width, height, color);
      default:
        throw new IllegalStateException("Unreachable code");
    }
  }
}
