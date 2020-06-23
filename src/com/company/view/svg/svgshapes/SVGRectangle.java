package com.company.view.svg.svgshapes;

import com.company.model.Frame;
import com.company.model.shape.Shape;
import com.company.view.svg.SVGTag;
import com.company.view.svg.SVGTagAttribute;

/**
 * Represents a series of behaviors that show how rectangles should be converted into SVGs.
 */
public class SVGRectangle extends ASVGShape implements SVGShape {

  @Override
  public SVGTag getShapeTag(String shapeName, Shape shape, int canvasX, int canvasY) {
    SVGTag shapeTag = super.getShapeTag(shapeName, shape, canvasX, canvasY);

    shapeTag.addAttribute(new SVGTagAttribute("x",
            Double.toString(shape.getPosition().getX() - canvasX)));
    shapeTag.addAttribute(new SVGTagAttribute("y",
            Double.toString(shape.getPosition().getY() - canvasY)));

    shapeTag.addAttribute(new SVGTagAttribute("width", Double.toString(shape.getWidth())));
    shapeTag.addAttribute(new SVGTagAttribute("height", Double.toString(shape.getHeight())));

    return shapeTag;
  }

  @Override
  public void addMotionTags(
      Frame frame1, Frame frame2, SVGTag shapeTag, int fps, int canvasX, int canvasY) {
    super.addMotionTags(frame1, frame2, shapeTag, fps, canvasX, canvasY);

    Shape frame1Shape = frame1.getShape();
    Shape frame2Shape = frame2.getShape();

    if (frame1Shape.getPosition().getX() != frame2Shape.getPosition().getX()) {
      shapeTag.addTag(this.getAnimateTag(frame1, frame2, "x",
          Double.toString(frame1.getShape().getPosition().getX() - canvasX),
          Double.toString(frame2.getShape().getPosition().getX() - canvasX), fps));
    }
    if (frame1Shape.getPosition().getY() != frame2Shape.getPosition().getY()) {
      shapeTag.addTag(this.getAnimateTag(frame1, frame2, "y",
          Double.toString(frame1.getShape().getPosition().getY() - canvasY),
          Double.toString(frame2.getShape().getPosition().getY() - canvasY), fps));
    }
    if (frame1Shape.getWidth() != frame2Shape.getWidth()) {
      shapeTag.addTag(this.getAnimateTag(frame1, frame2, "width",
          Double.toString(frame1.getShape().getWidth()),
          Double.toString(frame2.getShape().getWidth()), fps));
    }
    if (frame1Shape.getHeight() != frame2Shape.getHeight()) {
      shapeTag.addTag(this.getAnimateTag(frame1, frame2, "height",
              Double.toString(frame1.getShape().getHeight()),
              Double.toString(frame2.getShape().getHeight()), fps));
    }
    if (frame1Shape.getColor() != frame2Shape.getColor()) {
      shapeTag.addTag(this.getAnimateTag(frame1, frame2, "fill",
              colorToRGBString(frame1.getShape().getColor()),
              colorToRGBString(frame2.getShape().getColor()), fps));
    }
  }
}
