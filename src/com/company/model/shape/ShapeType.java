package com.company.model.shape;

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
  public String SVGname() { return this.svgName; }
}
