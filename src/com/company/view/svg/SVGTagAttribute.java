package com.company.view.svg;

/**
 * Represents an attribute of a tag in an SVG file.
 */
public class SVGTagAttribute {
  String attribute;
  String value;

  /**
   * Creates a new SVG attribute with the given attribute name and value for that attribute.
   *
   * @param attribute name of the attribute
   * @param value     attribute value
   */
  public SVGTagAttribute(String attribute, String value) {
    this.attribute = attribute;
    this.value = value;
  }

  @Override
  public String toString() {
    return this.attribute + "=" + "\"" + this.value + "\"";
  }
}