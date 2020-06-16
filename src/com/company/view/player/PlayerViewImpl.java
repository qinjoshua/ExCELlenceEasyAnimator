package com.company.view.player;

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
  private final JPanel buttonPanel;
  private final Map<String, AbstractButton> buttons;
  private int fps;
  private boolean isLooping;
  private boolean isPlaying;
  private Consumer<PlayerAction> callback;
  private final KeyComponent keyComponent;
  private final Timer mainLoop;

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
    this.setLayout(new BorderLayout());

    playArea = new AnimationPanel(model);
    playArea.setPreferredSize(new Dimension(model.getCanvasWidth(), model.getCanvasHeight()));
    this.getContentPane().add(playArea, BorderLayout.NORTH);

    buttonPanel = new JPanel();
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

    final Action SLOW_DOWN = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null) {
          callback.accept(new DecreaseSpeed());
        }
      }
    };

    final Action SPEED_UP = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (callback != null) {
          callback.accept(new IncreaseSpeed());
        }
      }
    };


    this.keyComponent = new KeyComponent();

    this.keyComponent.setCommand(KeyStroke.getKeyStroke('r'), "restart", RESTART);
    this.keyComponent.setCommand(KeyStroke.getKeyStroke('p'), "togglePlay", TOGGLE_PLAY);
    this.keyComponent.setCommand(KeyStroke.getKeyStroke('l'), "toggleLoop", TOGGLE_LOOP);
    this.keyComponent.setCommand(KeyStroke.getKeyStroke("LEFT"), "slowDown", SLOW_DOWN);
    this.keyComponent.setCommand(KeyStroke.getKeyStroke("RIGHT"), "speedUp", SPEED_UP);

    restartButton.addActionListener(RESTART);
    playButton.addActionListener(TOGGLE_PLAY);
    loopButton.addActionListener(TOGGLE_LOOP);
    slowDownButton.addActionListener(SLOW_DOWN);
    speedUpButton.addActionListener(SPEED_UP);


    this.add(keyComponent);
    this.add(buttonPanel, BorderLayout.SOUTH);

    ActionListener painter = evt -> {
      this.playArea.repaint();
      if (isLooping && playArea.onLastFrame()) {
        playArea.setTick(0);
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
    AbstractButton playButton = buttons.get("play");
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
  public void setSpeed(int speed) {
    if (speed < 0) {
      throw new IllegalArgumentException("Speed cannot be negative");
    } else {
      this.fps = speed;
      mainLoop.setDelay(PlayerViewImpl.getDelay(fps));
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
