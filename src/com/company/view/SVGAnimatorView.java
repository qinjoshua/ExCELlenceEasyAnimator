package com.company.view;

import com.company.model.ReadOnlyAnimatorModel;

import java.io.IOException;

/**
 * A view that produces an SVG-formatted output of the given animation.
 */
public class SVGAnimatorView implements AnimatorView {
  private final ReadOnlyAnimatorModel model;
  int speed;

  /**
   * Creates a new SVG animator view with a default of one frame per second.
   *
   * @param model a read-only animator model for the view to read from.
   */
  public SVGAnimatorView(ReadOnlyAnimatorModel model) {
    this.model = model;
    this.speed = 1;
  }

  @Override
  public void setSpeed(int fps) {
    this.speed = fps;
  }

  @Override
  public void output(Appendable out) throws IOException {

  }
}
