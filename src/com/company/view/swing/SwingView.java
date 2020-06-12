package com.company.view.swing;

import com.company.model.ReadOnlyAnimatorModel;
import com.company.view.AnimatorView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

/**
 * Implementation of {@link AnimatorView} that renders the animation to the Java Swing GUI
 * framework.
 */
public class SwingView implements AnimatorView {
  // The animator model to use for the animation. Doesn't support mutation because that's not in
  // the view's purview.
  private final ReadOnlyAnimatorModel model;
  // The width of the canvas.
  private final int canvasWidth;
  // The height of the canvas.
  private final int canvasHeight;
  // The frames per second of the animation.
  private int fps;

  /**
   * Initializes a SwingView with the given animation and a default FPS of 30. Note that the
   * frame border and title bar aren't part of the canvas, and so the actual window will be larger
   * than the width and height that are given.
   *
   * @param model  the model representing the animation
   * @param width  the width of the canvas in pixels
   * @param height the height of the canvas in pixels
   * @throws IllegalArgumentException if the input model is null or either width and height are
   *                                  negative
   */
  public SwingView(ReadOnlyAnimatorModel model, int width, int height) {
    if (model == null) {
      throw new IllegalArgumentException("Null model is invalid");
    } else if (width < 0 || height < 0) {
      throw new IllegalArgumentException("Canvas width and/or height are negative");
    }
    this.model = model;
    this.canvasWidth = width;
    this.canvasHeight = height;
    this.fps = 30;
  }

  @Override
  public void setSpeed(int fps) {
    this.fps = fps;
  }

  // Given a frames-per-second, gets the delay in milliseconds between draws needed to produce
  // that frame rate.
  // TODO keep static? Use in constructor?
  private static int getDelay(int fps) {
    return (int)Math.round(1000.0 / fps);
  }

  /**
   * Does not write to the appendable, although an un-writable Appendable will still trigger an
   * IOException for forwards compatibility reasons. Instead, displays a visual rendering of the
   * animation to the screen using Swing. TODO really do this?
   *
   * @param out an appendable representing the output stream that the view could be written to.
   * @throws IOException if the Appendable cannot be written to
   */
  @Override
  public void output(Appendable out) throws IOException {
    JFrame frame = new JFrame("Excellence");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    AnimationPanel panel = new AnimationPanel(this.model);
    panel.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
    frame.getContentPane().add(panel, BorderLayout.CENTER);
    frame.pack();
    final int delay = SwingView.getDelay(this.fps);
    // the current time of the animation
    // TODO compiler doesn't like local variables being used in setters, is this OK?
    final double timeDelta = delay / 1000.0;
    ActionListener painter = e -> {
      panel.repaint();
      panel.addTimeDelta(timeDelta);
    };
    frame.setVisible(true);
    new Timer(delay, painter).start();
  }
}
