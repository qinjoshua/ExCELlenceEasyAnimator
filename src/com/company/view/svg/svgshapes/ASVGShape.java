package com.company.view.svg.svgshapes;

import com.company.model.Frame;
import com.company.model.shape.Shape;
import com.company.view.svg.SVGSingleTag;
import com.company.view.svg.SVGTag;
import com.company.view.svg.SVGTagAttribute;

import java.awt.Color;

/**
 * Abstract class representing some of the most common operations for an SVG shape.
 */
public abstract class ASVGShape implements SVGShape {

  private static double convertUsingFPS(double time, int fps) {
    return time / fps * 1000;
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

  @Override
  public SVGTag getShapeTag(String shapeName, Shape shape, int canvasX, int canvasY) {

    return new SVGTag(shape.getShapeType().svgName(),
            new SVGTagAttribute("id", shapeName),
            new SVGTagAttribute("fill", colorToRGBString(shape.getColor())),
            new SVGTagAttribute("visibility", "visible"),
            new SVGTagAttribute("transform", getRotateValue(shape, canvasX, canvasY)));
  }

  @Override
  public void addMotionTags(Frame frame1, Frame frame2, SVGTag shapeTag,
                                     int fps, int canvasX, int canvasY) {
    Shape frame1Shape = frame1.getShape();
    Shape frame2Shape = frame2.getShape();

    if (frame1Shape.getShapeAngle() != frame2Shape.getShapeAngle()) {
      shapeTag.addTag(this.getAnimateTag(frame1, frame2, "transform", "rotate",
              (Math.toDegrees(frame1.getShape().getShapeAngle()) - canvasX) + " " +
                      getShapeCenterX(frame1.getShape(), canvasX) + " " +
                      getShapeCenterY(frame1.getShape(), canvasY),
              (Math.toDegrees(frame2.getShape().getShapeAngle()) - canvasX) + " " +
                      getShapeCenterX(frame2.getShape(), canvasX) + " " +
                      getShapeCenterY(frame2.getShape(), canvasY),
              fps));
    }
  }

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
            new SVGTagAttribute("begin",
                    convertUsingFPS(frame1.getTime(), fps) + "ms"),
            new SVGTagAttribute("dur",
                    convertUsingFPS(frame2.getTime() - frame1.getTime(), fps) + "ms"),
            new SVGTagAttribute("attributeName", attributeName),
            new SVGTagAttribute("from", from),
            new SVGTagAttribute("to", to),
            new SVGTagAttribute("fill", "freeze"));
  }

  /**
   * Helper method for creating the animate tag.
   *
   * @param frame1        first frame to be animated
   * @param frame2        second frame to animate to
   * @param attributeName the name of the attribute that would like to be changed
   * @param type          the type of the attribute that we would to be changed
   * @param from          the value of the attribute to come from
   * @param to            the value of the attribute to go to
   * @param fps           the frames per second this animation runs at
   * @return a tag representing the SVG animate tag that goes from one frame to the other, changing
   * the given attribute from the given value to the other
   */
  protected SVGTag getAnimateTag(Frame frame1, Frame frame2, String attributeName, String type,
                                 String from, String to, int fps) {

    return new SVGSingleTag("animateTransform",
            new SVGTagAttribute("attributeType", "xml"),
            new SVGTagAttribute("begin",
                    convertUsingFPS(frame1.getTime(), fps) + "ms"),
            new SVGTagAttribute("dur",
                    convertUsingFPS(frame2.getTime() - frame1.getTime(), fps) + "ms"),
            new SVGTagAttribute("attributeName", attributeName),
            new SVGTagAttribute("type", type),
            new SVGTagAttribute("from", from),
            new SVGTagAttribute("to", to),
            new SVGTagAttribute("fill", "freeze"));
  }

  protected String getRotateValue(Shape shape, int canvasX, int canvasY) {
    return "rotate(" + Math.toDegrees(shape.getShapeAngle()) + " " +
            getShapeCenterX(shape, canvasX) + " " +
            getShapeCenterY(shape, canvasY) + ")";
  }

  protected double getShapeCenterX(Shape shape, int canvasX) {
    return (shape.getPosition().getX() - canvasX + shape.getWidth()) / 2.;
  }

  protected double getShapeCenterY(Shape shape, int canvasY) {
    return (shape.getPosition().getY() - canvasY + shape.getHeight()) / 2.;
  }
}
