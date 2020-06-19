package com.company.view.swing.editor;

import com.company.controller.animatoractions.AnimatorAction;
import com.company.controller.animatoractions.CreateKeyframe;
import com.company.controller.viewactions.editoractions.EditorAction;
import com.company.controller.viewactions.editoractions.SetTick;
import com.company.model.Frame;
import com.company.model.ReadOnlyAnimatorModel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 * The panel representing a single timeline of keyframes for a single shape.
 */
public class TimelinePanel extends JPanel {
  String shapeName;
  ReadOnlyAnimatorModel model;
  Consumer<AnimatorAction> modelCallback;
  Consumer<EditorAction> viewCallback;
  List<JToggleButton> buttons;
  static final Dimension KEYFRAME_SIZE = new Dimension(15, 40);
  public static final String BULLET = "•";

  /**
   * Creates a panel for a specific shape, showing its keyframes over time in a timeline.
   *
   * @param shapeName     the shape name to make a timeline for
   * @param model         the underlying model to use
   * @param modelCallback a callback for modifying the model
   */
  public TimelinePanel(String shapeName, ReadOnlyAnimatorModel model,
                       Consumer<AnimatorAction> modelCallback) {

    this.shapeName = shapeName;
    this.model = model;
    this.modelCallback = modelCallback;
    this.viewCallback = null;

    SortedSet<Frame> frames = this.model.getKeyframes().get(shapeName);
    int lastTick = (int) frames.last().getTime();

    buttons = new ArrayList<>();

    for (int tick = 1; tick <= lastTick; tick++) {
      buttons.add(this.makeTickBtn(tick));
    }

    this.updateButtonText();



    this.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
    // this.setLayout(new GridLayout(1, 0, 0, 0));
    for (JToggleButton button : buttons) {
      this.add(button);
    }
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(this.buttons.size() * (int)KEYFRAME_SIZE.getWidth(),
        (int)KEYFRAME_SIZE.getHeight());
  }

  private JToggleButton makeTickBtn(int tick) {
    JToggleButton tickBtn = new JToggleButton();
    tickBtn.setMargin(new Insets(0, 0, 0, 0));
    tickBtn.setPreferredSize(KEYFRAME_SIZE);
    tickBtn.addActionListener(e -> {
      JToggleButton btn = (JToggleButton) e.getSource();
      if (btn.getText().equals("")) {
        modelCallback.accept(new CreateKeyframe(shapeName, tick));
        this.getViewCallback().accept(new SetTick(tick));
      } else {
        btn.setSelected(true);
      }
    });

    return tickBtn;
  }

  /**
   * Updates the buttons so "." marks buttons that correspond to currently extant keyframes.
   */
  public void updateButtonText() {
    for (JToggleButton button : buttons) {
      button.setText("");
    }

    SortedSet<Frame> frames = this.model.getKeyframes().get(shapeName);
    for (Frame frame : frames) {
      buttons.get((int) frame.getTime() - 1).setText(BULLET);
    }
  }

  public Consumer<EditorAction> getViewCallback() {
    return viewCallback;
  }

  public void setViewCallback(Consumer<EditorAction> viewCallback) {
    this.viewCallback = viewCallback;
  }

  /**
   * Sets the timeline panel to the specified tick, untoggling all of the buttons besides this one.
   *
   * @param tick the tick to set to
   */
  public void setTick(int tick) {
    if (tick > buttons.size()) {
      for (int t = buttons.size(); t < tick; t++) {
        JToggleButton tickBtn = this.makeTickBtn(t+1);
        buttons.add(tickBtn);
        this.add(tickBtn);
      }
    }

    for (int i = 0; i < buttons.size(); i++) {
      buttons.get(i).setSelected(i == tick - 1);
    }
    this.updateButtonText();
    this.repaint();
  }
}
