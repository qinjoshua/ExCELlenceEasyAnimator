package com.company.view.swing.player;

import com.company.controller.viewactions.playeractions.DecreaseSpeed;
import com.company.controller.viewactions.playeractions.IncreaseSpeed;
import com.company.controller.viewactions.playeractions.PlayerAction;
import com.company.controller.viewactions.playeractions.Restart;
import com.company.controller.viewactions.playeractions.ToggleLoop;
import com.company.controller.viewactions.playeractions.TogglePlay;
import com.company.model.ReadOnlyAnimatorModel;
import com.company.view.VisualView;
import com.company.view.swing.AnimationPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/**
 * Provides a view for a window that plays through an animation in its entirety. The view will
 * provide an interface for users to play, pause, speed up or slow down.
 */
public class PlayerViewImpl extends JFrame implements VisualView, PlayerView {
  private final AnimationPanel playArea;
  private final Map<String, AbstractButton> buttons;
  private final Timer mainLoop;
  private int fps;
  private boolean isLooping;
  private boolean isPlaying;
  private Consumer<PlayerAction> callback;

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

    this.setLayout(new BorderLayout());

    // Sets the play area to an instance of the animation panel
    playArea = new AnimationPanel(model);
    playArea.setBackground(Color.WHITE);
    playArea.setPreferredSize(new Dimension(model.getCanvasWidth(), model.getCanvasHeight()));
    this.getContentPane().add(playArea, BorderLayout.NORTH);

    // Creates the button controls on the bottom of the screen
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout());

    JButton restartButton = new JButton("Restart");
    JToggleButton playButton = new JToggleButton("❚❚");
    JToggleButton loopButton = new JToggleButton("\uD83D\uDD02");
    JButton slowDownButton = new JButton("Slow Down");
    JButton speedUpButton = new JButton("Speed Up");

    buttons = new HashMap<>();
    buttons.put("restart", restartButton);
    buttons.put("play", playButton);
    buttons.put("loop", loopButton);
    buttons.put("slowDown", slowDownButton);
    buttons.put("speedUp", speedUpButton);

    buttonPanel.add(restartButton);
    buttonPanel.add(playButton);
    buttonPanel.add(loopButton);
    buttonPanel.add(slowDownButton);
    buttonPanel.add(speedUpButton);


    this.callback = null;

    final Action restart = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null) {
          callback.accept(new Restart());
        }
      }
    };

    final Action togglePlay = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null) {
          callback.accept(new TogglePlay());
        }
      }
    };

    final Action toggleLoop = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null) {
          callback.accept(new ToggleLoop());
        }
      }
    };

    final Action slowDown = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null) {
          callback.accept(new DecreaseSpeed());
        }
      }
    };

    final Action speedUp = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null) {
          callback.accept(new IncreaseSpeed());
        }
      }
    };


    KeyComponent keyComponent = new KeyComponent();

    keyComponent.setCommand(KeyStroke.getKeyStroke('r'), "restart", restart);
    keyComponent.setCommand(KeyStroke.getKeyStroke('p'), "togglePlay", togglePlay);
    keyComponent.setCommand(KeyStroke.getKeyStroke('l'), "toggleLoop", toggleLoop);
    keyComponent.setCommand(KeyStroke.getKeyStroke("LEFT"), "slowDown", slowDown);
    keyComponent.setCommand(KeyStroke.getKeyStroke("RIGHT"), "speedUp", speedUp);

    restartButton.addActionListener(restart);
    playButton.addActionListener(togglePlay);
    loopButton.addActionListener(toggleLoop);
    slowDownButton.addActionListener(slowDown);
    speedUpButton.addActionListener(speedUp);

    this.add(keyComponent);
    this.add(buttonPanel, BorderLayout.SOUTH);

    ActionListener painter = evt -> {
      this.playArea.repaint();
      if (isLooping && playArea.onLastFrame()) {
        playArea.setTick(1);
      }

      if (isPlaying) {
        this.playArea.nextTick();
      }
    };
    mainLoop = new Timer(PlayerViewImpl.getDelay(fps), painter);

    this.pack();
  }

  // Given a frames-per-second, gets the delay in milliseconds between draws needed to produce
  // that frame rate.
  private static int getDelay(int fps) {
    return (int) Math.round(1000.0 / fps);
  }

  @Override
  public void renderVisual() {
    this.setVisible(true);
    mainLoop.start();
  }

  @Override
  public void togglePlay() {
    isPlaying = !isPlaying;
    JToggleButton playButton = (JToggleButton) buttons.get("play");

    playButton.setSelected(!isPlaying);

    if (playButton.getText().equals("❚❚")) {
      playButton.setText("▶");
    } else {
      playButton.setText("❚❚");
    }
  }

  @Override
  public void setTick(int tick) {
    this.playArea.setTick(tick);
  }

  @Override
  public int getSpeed() {
    return this.fps;
  }

  @Override
  public void setSpeed(int speed) {
    if (speed < 0) {
      throw new IllegalArgumentException("Speed cannot be negative");
    } else {
      this.fps = speed;
      mainLoop.setDelay(PlayerViewImpl.getDelay(fps));
    }
  }

  @Override
  public void toggleLoop() {
    isLooping = !isLooping;
    JToggleButton loopButton = (JToggleButton) buttons.get("loop");
    loopButton.setSelected(isLooping);
  }

  @Override
  public void setCallback(Consumer<PlayerAction> callback) {
    this.callback = callback;
  }

  private static class KeyComponent extends JPanel {
    /**
     * Sets the given keystroke to the given action with the given name.
     *
     * @param key    the key used to start the action
     * @param name   the name used for the action
     * @param action the action itself
     */
    public void setCommand(KeyStroke key, String name, Action action) {
      this.getInputMap().put(key, name);
      this.getActionMap().put(name, action);
    }
  }
}
