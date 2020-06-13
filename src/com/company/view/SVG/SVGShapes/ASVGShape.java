package com.company.view.SVG.SVGShapes;

import com.company.model.Frame;
import com.company.model.shape.Shape;
import com.company.view.SVG.SVGSingleTag;
import com.company.view.SVG.SVGTag;
import com.company.view.SVG.SVGTagAttribute;

import java.awt.*;

public abstract class ASVGShape implements SVGShape {

  @Override
  public SVGTag getShapeTag(String shapeName, Shape shape) {

    return new SVGTag(shape.getShapeType().SVGname(),
        new SVGTagAttribute("id", shapeName),
        new SVGTagAttribute("fill", colorToRGBString(shape.getColor())),
        new SVGTagAttribute("visibility", "visible"));
  }

  @Override
  public abstract void addMotionTags(Frame frame1, Frame frame2, SVGTag shapeTag, int fps);

  /**
   * Helper method for creating the animate tag.
   *
   * @param frame1        first frame to be animated
   * @param frame2        second frame to animate to
   * @param attributeName the name of the attribute that would like to be changed
   * @param from          the value of the attribute to come from
   * @param to            the value of the attribute to go to
   * @param fps           the frames per second this animation runs at
   * @return a tag representing the SVG animate tag that goes from one frame to the other, changing
   *     the given attribute from the given value to the other
   */
  protected SVGTag getAnimateTag(Frame frame1, Frame frame2, String attributeName,
                                 String from, String to, int fps) {

    return new SVGSingleTag("animate",
        new SVGTagAttribute("attributeType", "xml"),
        new SVGTagAttribute("begin", frame1.getTime() / fps * 1000 + "ms"),
        new SVGTagAttribute("dur", frame2.getTime() / fps * 1000 + "ms"),
        new SVGTagAttribute("attributeName", attributeName),
        new SVGTagAttribute("from", from),
        new SVGTagAttribute("to", to),
        new SVGTagAttribute("fill", "freeze"));
  }

  /**
   * Formats the given color into an rgb string.
   *
   * @param color color to be converted into an RGB string
   * @return output formatted as rgb(value, value, value)
   */
  protected static String colorToRGBString(Color color) {
    return String.format("rgb(%o, %o, %o)", color.getRed(), color.getGreen(), color.getBlue());
  }
}
