package com.company.view.swing;

import com.company.model.ReadOnlyAnimatorModel;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Implementation of {@link VisualView} that renders the animation to the Java Swing GUI framework.
 */
public class SwingView implements VisualView {
  // The animator model to use for the animation. Doesn't support mutation because that's not in
  // the view's purview.
  private final ReadOnlyAnimatorModel model;
  // The frames per second of the animation.
  private final int fps;

  /**
   * Initializes a SwingView with the given animation and a default FPS of 30. Note that the frame
   * border and title bar aren't part of the canvas, and so the actual window will be larger than
   * the width and height that are given.
   *
   * @param model the model representing the animation
   * @param fps   the FPS of the animation
   * @throws IllegalArgumentException if the input model is null or either width and height are
   *                                  negative
   */
  public SwingView(ReadOnlyAnimatorModel model, int fps) {
    if (model == null) {
      throw new IllegalArgumentException("Null model is invalid");
    }
    this.model = model;
    this.fps = fps;
  }

  // Given a frames-per-second, gets the delay in milliseconds between draws needed to produce
  // that frame rate.
  private static int getDelay(int fps) {
    return (int) Math.round(1000.0 / fps);
  }

  @Override
  public void renderVisual() {
    JFrame frame = new JFrame("Excellence");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    AnimationPanel panel = new AnimationPanel(this.model);
    panel.setPreferredSize(new Dimension(model.getCanvasWidth(), model.getCanvasHeight()));
    frame.getContentPane().add(panel, BorderLayout.CENTER);
    frame.pack();
    final int delay = SwingView.getDelay(this.fps);
    ActionListener painter = evt -> {
      panel.repaint();
      panel.addTimeDelta(1);
    };
    frame.setVisible(true);
    new Timer(delay, painter).start();
  }
}
