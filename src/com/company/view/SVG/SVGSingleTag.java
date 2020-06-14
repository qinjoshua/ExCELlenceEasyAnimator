package com.company.view.SVG;

/**
 * Represents a single svg tag, with no closing tag.
 */
public class SVGSingleTag extends SVGTag {

  /**
   * Creates the svg tag with the given name and attributes.
   *
   * @param name       name of the svg tag
   * @param attributes list of attributes for this svg tag
   */
  public SVGSingleTag(String name, SVGTagAttribute... attributes) {
    super(name, attributes);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("<");
    builder.append(this.name);

    for (SVGTagAttribute attribute : attributes) {
      builder.append(" ");
      builder.append(attribute.toString());
    }
    builder.append(" />\n");
    return builder.toString();
  }
}
