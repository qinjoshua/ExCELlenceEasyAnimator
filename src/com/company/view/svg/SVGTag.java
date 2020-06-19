package com.company.view.svg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a tag in an SVG file.
 */
public class SVGTag {
  final String name;
  final List<SVGTagAttribute> attributes;
  final List<SVGTag> innerSVGTags;

  /**
   * Creates a new SVG tag with the given name and list of attributes.
   *
   * @param name       name of the tag
   * @param attributes the attributes that go into the tag
   */
  public SVGTag(String name, SVGTagAttribute... attributes) {
    this.name = name;
    this.attributes = new ArrayList<>();

    this.attributes.addAll(Arrays.asList(attributes));

    this.innerSVGTags = new ArrayList<>();
  }

  public void addTag(SVGTag tag) {
    this.innerSVGTags.add(tag);
  }

  public void addAttribute(SVGTagAttribute attribute) {
    this.attributes.add(attribute);
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
    builder.append(">\n");

    for (SVGTag tag : this.innerSVGTags) {
      builder.append(tag.toString());
    }

    builder.append("</");
    builder.append(this.name);
    builder.append(">\n");
    return builder.toString();
  }
}