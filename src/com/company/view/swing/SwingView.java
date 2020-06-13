package com.company.view.swing;

import com.company.model.ReadOnlyAnimatorModel;
import com.company.view.VisualView;

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
  // The width of the canvas.
  private final int canvasWidth;
  // The height of the canvas.
  private final int canvasHeight;
  // The x-coordinate of the origin of the canvas.
  private final int canvasX;
  // the y-coordinate of the origin of the canvas.
  private final int canvasY;
  // The frames per second of the animation.
  private final int fps;

  /**
   * Initializes a SwingView with the given animation and a default FPS of 30. Note that the frame
   * border and title bar aren't part of the canvas, and so the actual window will be larger than
   * the width and height that are given.
   *
   * @param model  the model representing the animation
   * @param width  the width of the canvas in pixels
   * @param height the height of the canvas in pixels
   * @param x      the x-coordinate of the origin of the canvas, in Cartesian coordinates
   * @throws IllegalArgumentException if the input model is null or either width and height are
   *                                  negative
   */
  public SwingView(ReadOnlyAnimatorModel model, int width, int height, int x, int y, int fps) {
    if (model == null) {
      throw new IllegalArgumentException("Null model is invalid");
    } else if (width < 0 || height < 0) {
      throw new IllegalArgumentException("Canvas width and/or height are negative");
    }
    this.model = model;
    this.canvasWidth = width;
    this.canvasHeight = height;
    this.canvasX = x;
    this.canvasY = y;
    this.fps = fps;
  }

  // Given a frames-per-second, gets the delay in milliseconds between draws needed to produce
  // that frame rate.
  // TODO keep static? Use in constructor?
  private static int getDelay(int fps) {
    return (int) Math.round(1000.0 / fps);
  }

  @Override
  public void renderVisual() {
    JFrame frame = new JFrame("Excellence");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    AnimationPanel panel = new AnimationPanel(this.model);
    panel.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
    frame.getContentPane().add(panel, BorderLayout.CENTER);
    frame.pack();
    final int delay = SwingView.getDelay(this.fps);
    // the current time of the animation
    final double timeDelta = delay / 1000.0;
    ActionListener painter = evt -> {
      panel.repaint();
      panel.addTimeDelta(timeDelta);
    };
    frame.setVisible(true);
    new Timer(delay, painter).start();
  }
}
