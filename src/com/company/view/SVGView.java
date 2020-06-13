package com.company.view;


import java.io.IOException;

/**
 * A view that renders the animation as an SVG, a plaintext XML-based format for vector graphics.
 */
public interface SVGView {
  /**
   * Outputs the animation in SVG format to the given Appendable output stream.
   *
   * @param out an appendable representing the output stream that the view is written to
   * @throws IllegalArgumentException if the given output stream is {@code null}
   * @throws IOException              if the given output stream cannot be written to
   */
  void outputSVG(Appendable out) throws IOException;

}
