package com.company.view.swing;

import com.company.model.Frame;
import com.company.model.ReadOnlyAnimatorModel;

import java.util.SortedSet;

/**
 * Represents a class that extends JPanel to draw the shapes from a given animation model to the
 * screen. This class doesn't keep track of timing information: its only job is drawing to the
 * screen at a time t that it is given.
 */
public class AnimationPanel extends AShapesPanel {

  /**
   * Initializes the panel given a model. Sets the time t to 0 to it starts at the beginning.
   *
   * @param model the animator model to use
   * @throws IllegalArgumentException if the model is null
   */
  public AnimationPanel(ReadOnlyAnimatorModel model) {
    super(model);
  }

  /**
   * Increments the tick rendered to the screen by 1.
   */
  public void nextTick() {
    this.setTick(this.t += 1);
  }

  /**
   * Returns whether the current animation tick is past the last keyframe in the animation: i.e.,
   * the animation is over.
   *
   * @return whether the animation is on its last frame
   */
  public boolean onLastFrame() {
    for (SortedSet<Frame> frames : model.getKeyframes().values()) {
      for (Frame frame : frames) {
        if (frame.getTime() > t) {
          return false;
        }
      }
    }
    return true;
  }
}
