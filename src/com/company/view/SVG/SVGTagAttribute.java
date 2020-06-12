package com.company.view.SVG;

public class SVGTagAttribute {
  String attribute;
  String value;

  public SVGTagAttribute(String attribute, String value) {
    this.attribute = attribute;
    this.value = value;
  }

  @Override
  public String toString() {
    return this.attribute + "=" + "\"" + this.value + "\"";
  }
}