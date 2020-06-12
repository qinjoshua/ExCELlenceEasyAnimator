package com.company.view.SVG.SVGShapes;

import com.company.model.Frame;
import com.company.model.shape.Shape;
import com.company.view.SVG.SVGTag;

/**
 * An SVGShape is a class that supports operations for converting shapes and their behaviors to
 * work with SVGs. For every shape that the SVGView intends to support, there should be a
 * corresponding SVG shape indicating to the view how that shape should be processed and handled.
 */
public interface SVGShape {

  /**
   * Gets the SVG tag of this SVGShape.
   *
   * @param shapeName the name of the shape to create the tag for.
   * @param shape the shape to get the shape tag from.
   * @return an SVG tag that draws the shape with its position, color, and size.
   */
  public SVGTag getShapeTag(String shapeName, Shape shape);

  /**
   * Adds all the relevant animate tags to the given shape in order to move between frame1 and
   * frame2.
   *
   * @param frame1 starting frame
   * @param frame2 ending frame
   * @param fps frames per second, the speed at which the animation is running
   * @param shapeTag the tag for the shape that is being animated on.
   */
  public void addMotionTags(Frame frame1, Frame frame2, SVGTag shapeTag, int fps);
}
