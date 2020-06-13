package com.company.view.SVG;

import com.company.model.Frame;
import com.company.model.ReadOnlyAnimatorModel;
import com.company.model.shape.ShapeType;
import com.company.view.SVG.SVGShapes.SVGEllipse;
import com.company.view.SVG.SVGShapes.SVGRectangle;
import com.company.view.SVG.SVGShapes.SVGShape;
import com.company.view.SVGView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

/**
 * A view that produces an SVG-formatted output of the given animation.
 */
public class SVGViewImpl implements SVGView {
  private final Map<ShapeType, SVGShape> svgShapes;

  private final ReadOnlyAnimatorModel model;
  private final int speed;
  private final int width;
  private final int height;

  /**
   * Creates a new SVG animator view with a default of one frame per second, and with the given read
   * only model, width, and height of the animation.
   *
   * @param model a read-only animator model for the view to read from.
   */
  public SVGViewImpl(ReadOnlyAnimatorModel model, int width, int height) {
    this.model = model;
    this.width = width;
    this.height = height;
    this.speed = 1;

    svgShapes = new HashMap<>();

    svgShapes.put(ShapeType.Rectangle, new SVGRectangle());
    svgShapes.put(ShapeType.Ellipse, new SVGEllipse());
  }

  @Override
  public void outputSVG(Appendable out) throws IOException {
    out.append(this.createSVGDocument());
  }

  /**
   * Creates a new SVGDocument as a string.
   *
   * @return the SVGDocument detailing the entire animation, as a string.
   */
  private String createSVGDocument() {
    SVGTag svg = new SVGTag("svg",
        new SVGTagAttribute("width", Integer.toString(this.width)),
        new SVGTagAttribute("height", Integer.toString(this.height)),
        new SVGTagAttribute("version", "1.1"),
        new SVGTagAttribute("xmlns", "http://www.w3.org/2000/svg"));

    Map<String, SortedSet<Frame>> keyframes = model.getKeyframes();

    for (Map.Entry<String, SortedSet<Frame>> keyframe : keyframes.entrySet()) {
      svg.addTag(this.shapeToSVG(keyframe.getKey(), keyframe.getValue()));
    }

    return svg.toString();
  }

  /**
   * Creates a shape tag and the associated animations.
   *
   * @param shapeName name of the shape
   * @param frames    the set of frames defining this shape's animation
   * @return an SVGTag representing the animated shape
   */
  private SVGTag shapeToSVG(String shapeName, SortedSet<Frame> frames) {

    // Gets the kind of svgShape that this shape is
    SVGShape svgShape = this.svgShapes.get(frames.first().getShape().getShapeType());

    // Creates the shape tag to be animated
    SVGTag shapeTag = svgShape.getShapeTag(shapeName, frames.first().getShape());

    // Add the internal animation tags
    Frame prevFrame = null;
    if (frames.size() == 1) {
      return shapeTag;
    }
    for (Frame frame : frames) {
      if (prevFrame != null) {
        svgShape.addMotionTags(prevFrame, frame, shapeTag, this.speed);
      }
      prevFrame = frame;
    }

    return shapeTag;
  }
}
