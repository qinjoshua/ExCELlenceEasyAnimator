package com.company.view.SVG;

// TODO refactor with composition rather than inheritance
public class SVGSingleTag extends SVGTag {

  /**
   * Represents a single tag
   *
   * @param name
   * @param attributes
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
