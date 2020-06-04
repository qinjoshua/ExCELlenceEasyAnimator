package com.company.model.shape;

/**
 * An enumeration representing all the different types of shapes, along with associated data
 * necessary to specify them.
 */
public enum ShapeType {
  Rectangle("rectangle"),
  Ellipse("ellipse");

  private final String disp;

  ShapeType(String disp) {
    this.disp = disp;
  }

  @Override
  public String toString() {
    return this.disp;
  }
}
