package com.company.view;

import java.io.IOException;

/**
 * A view for animator, that will display the animations and movements from key intervals.
 */
public interface AnimatorView {

  /**
   * Sets the speed of the animation.
   *
   * @param fps the speed in frames per second that you would like the animation to run at.
   */
  void setSpeed(int fps);

  /**
   * Outputs the animation to the view.
   *
   * @param out an appendable representing the output stream that the view could be written to.
   */
  void output(Appendable out) throws IOException;
}
