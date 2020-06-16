package com.company.view.player;

import com.company.controller.viewactions.playeractions.PlayerAction;
import com.company.controller.viewactions.playeractions.Restart;
import com.company.controller.viewactions.playeractions.ToggleLoop;
import com.company.controller.viewactions.playeractions.TogglePlay;
import com.company.model.ReadOnlyAnimatorModel;
import com.company.view.VisualView;
import com.company.view.swing.AnimationPanel;
import com.company.view.swing.SwingView;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import java.util.function.Function;

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
  private boolean isLooping;
  private boolean isPlaying;
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

    if (fps < 0) {
      throw new IllegalArgumentException("Speed cannot be less than zero");
    }
    this.fps = fps;
    this.isLooping = false;
    this.isPlaying = true;

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    playArea = new AnimationPanel(model);
    playArea.setPreferredSize(new Dimension(model.getCanvasWidth(), model.getCanvasHeight()));
    this.getContentPane().add(playArea, BorderLayout.CENTER);
    this.pack();

    this.callback = null;

    final Action RESTART = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null) {
          callback.accept(new Restart());
        }
      }
    };

    final Action TOGGLE_PLAY = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null) {
          callback.accept(new TogglePlay());
        }
      }
    };

    final Action TOGGLE_LOOP = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null) {
          callback.accept(new ToggleLoop());
        }
      }
    };


    this.keyComponent = new KeyComponent();

    this.keyComponent.setCommand(KeyStroke.getKeyStroke('r'), "restart", RESTART);
    this.keyComponent.setCommand(KeyStroke.getKeyStroke('p'), "togglePlay", TOGGLE_PLAY);
    this.keyComponent.setCommand(KeyStroke.getKeyStroke('l'), "toggleLoop", TOGGLE_LOOP);

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
      if (isLooping && playArea.onLastFrame()) {
        // TODO is this OK? do we need a controller? some other kind of ActionListener?
        playArea.setTick(0);
      }

      if (isPlaying) {
        this.playArea.nextTick();
      }
    };

    this.setVisible(true);
    new Timer(delay, painter).start();
  }

  @Override
  public void togglePlay() {
    isPlaying = !isPlaying;
  }

  @Override
  public void setTick(int tick) {
    this.playArea.setTick(tick);
  }

  @Override
  public void setSpeed(int speed) {
    if (speed < 0) {
      throw new IllegalArgumentException("Speed cannot be negative");
    }
  }

  @Override
  public int getSpeed() {
    return this.fps;
  }

  @Override
  public void toggleLoop() {
    isLooping = !isLooping;
  }

  @Override
  public void setCallback(Consumer<PlayerAction> callback) {
    this.callback = callback;
  }

  private class KeyComponent extends JPanel {
    /**
     * Sets the given keystroke to the given action with the given name.
     *
     * @param key the key used to start the action
     * @param name the name used for the action
     * @param action the action itself
     */
    public void setCommand(KeyStroke key, String name, Action action) {
      this.getInputMap().put(key, name);
      this.getActionMap().put(name, action);
    }
  }
}
