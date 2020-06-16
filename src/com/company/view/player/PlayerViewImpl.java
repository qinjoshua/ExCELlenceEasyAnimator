package com.company.view.player;

import com.company.controller.viewactions.playeractions.PlayerAction;
import com.company.controller.viewactions.playeractions.Restart;
import com.company.model.ReadOnlyAnimatorModel;
import com.company.view.VisualView;
import com.company.view.swing.AnimationPanel;
import com.company.view.swing.SwingView;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/**
 * Provides a view for a window that plays through an animation in its entirety. The view will
 * provide an interface for users to play, pause, speed up or slow down.
 */
public class PlayerViewImpl extends JFrame implements VisualView, PlayerView {
  private final AnimationPanel playArea;
  private int fps;
  private Consumer<PlayerAction> callback;
  KeyComponent keyComponent;

  /**
   * Initializes the player to take in a read only model that can be played.
   *
   * @param model the read only model of the animation to be played
   * @throws IllegalArgumentException if speed is less than zero
   */
  public PlayerViewImpl(ReadOnlyAnimatorModel model, int fps) {
    super("Excellence Player");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    playArea = new AnimationPanel(model);
    playArea.setPreferredSize(new Dimension(model.getCanvasWidth(), model.getCanvasHeight()));
    this.getContentPane().add(playArea, BorderLayout.CENTER);
    this.pack();

    if (fps > 0) {
      this.fps = fps;
    } else {
      throw new IllegalArgumentException("Speed cannot be less than zero");
    }

    this.callback = null;

    this.keyComponent = new KeyComponent();
    this.keyComponent.setHotkey(KeyStroke.getKeyStroke('r'), "restart");
    this.keyComponent.getActionMap().put("restart", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null) {
          callback.accept(new Restart());
        }
      }
    });
    this.add(keyComponent);
  }

  // Given a frames-per-second, gets the delay in milliseconds between draws needed to produce
  // that frame rate.
  private static int getDelay(int fps) {
    return (int) Math.round(1000.0 / fps);
  }

  @Override
  public void renderVisual() {
    final int delay = PlayerViewImpl.getDelay(fps);

    ActionListener painter = evt -> {
      this.playArea.repaint();
      this.playArea.addTimeDelta(1); //TODO nicholas refactor this
    };

    this.setVisible(true);
    new Timer(delay, painter).start();
  }

  @Override
  public void togglePlay() {

  }

  @Override
  public void setTick(int tick) {
    this.playArea.setTick(tick);
  }

  @Override
  public void setSpeed(int speed) {

  }

  @Override
  public int getSpeed() {
    return 0;
  }

  @Override
  public void toggleLooping() {

  }

  @Override
  public void setCallback(Consumer<PlayerAction> callback) {
    this.callback = callback;
  }

  private class KeyComponent extends JPanel {
    public void setHotkey(KeyStroke key, String name) {
      this.getInputMap().put(key, name);
    }
  }
}
