package com.company.view.SVG.SVGShapes;

import com.company.model.Frame;
import com.company.model.shape.Shape;
import com.company.view.SVG.SVGTag;
import com.company.view.SVG.SVGTagAttribute;

public class SVGRectangle extends ASVGShape implements SVGShape {

  @Override
  public SVGTag getShapeTag(String shapeName, Shape shape) {
    SVGTag shapeTag = super.getShapeTag(shapeName, shape);

    shapeTag.addAttribute(new SVGTagAttribute("x",
        Double.toString(shape.getPosition().getX())));
    shapeTag.addAttribute(new SVGTagAttribute("y",
        Double.toString(shape.getPosition().getY())));

    shapeTag.addAttribute(new SVGTagAttribute("width", Double.toString(shape.getWidth())));
    shapeTag.addAttribute(new SVGTagAttribute("height", Double.toString(shape.getHeight())));

    return shapeTag;
  }

  @Override
  public void addMotionTags(Frame frame1, Frame frame2, SVGTag shapeTag, int fps) {
    Shape frame1Shape = frame1.getShape();
    Shape frame2Shape = frame2.getShape();

    if (frame1Shape.getPosition().getX() != frame2Shape.getPosition().getX()) {
      shapeTag.addTag(this.getAnimateTag(frame1, frame2, "x",
          Double.toString(frame1.getShape().getPosition().getX()),
          Double.toString(frame2.getShape().getPosition().getX()), fps));
    }
    if (frame1Shape.getPosition().getY() != frame2Shape.getPosition().getY()) {
      shapeTag.addTag(this.getAnimateTag(frame1, frame2, "y",
          Double.toString(frame1.getShape().getPosition().getY()),
          Double.toString(frame2.getShape().getPosition().getY()), fps));
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
