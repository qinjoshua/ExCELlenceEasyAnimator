package com.company.view.swing.editor;

import com.company.controller.animatoractions.AnimatorAction;
import com.company.controller.animatoractions.CreateKeyframe;
import com.company.controller.animatoractions.DeleteShape;
import com.company.controller.viewactions.editoractions.EditorAction;
import com.company.controller.viewactions.editoractions.RefreshView;
import com.company.controller.viewactions.editoractions.SetTick;
import com.company.model.Frame;
import com.company.model.ReadOnlyAnimatorModel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.function.Consumer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;

/**
 * Represents a panel to show all of the timelines for each specific shape.
 */
public class TimelinesPanel extends JPanel {
  private static final int TIMELINE_HEIGHT = 200;
  private final JPanel timelinesPanel;
  private final JPanel namesPanel;
  private final JPanel outerPanel;
  private final JPanel addFramePanel;
  Map<String, TimelinePanel> timelines;
  ReadOnlyAnimatorModel model;
  Consumer<AnimatorAction> modelCallback;
  Consumer<EditorAction> viewCallback;

  public TimelinesPanel(ReadOnlyAnimatorModel model, Consumer<AnimatorAction> modelCallback) {
    this.model = model;
    this.modelCallback = modelCallback;
    timelinesPanel = new JPanel();
    namesPanel = new JPanel();
    addFramePanel = new JPanel();
    outerPanel = new JPanel();

    Map<String, SortedSet<Frame>> frames = model.getKeyframes();
    timelines = new LinkedHashMap<>();
    for (Map.Entry<String, SortedSet<Frame>> entry : frames.entrySet()) {
      timelines.put(entry.getKey(), new TimelinePanel(entry.getKey(), model, modelCallback));
    }

    for (Map.Entry<String, TimelinePanel> entry : timelines.entrySet()) {
      this.addShape(entry.getKey(), entry.getValue());
    }

    timelinesPanel.setLayout(new BoxLayout(timelinesPanel, BoxLayout.Y_AXIS));
    namesPanel.setLayout(new BoxLayout(namesPanel, BoxLayout.Y_AXIS));
    addFramePanel.setLayout(new BoxLayout(addFramePanel, BoxLayout.Y_AXIS));
    addFramePanel.setPreferredSize(new Dimension(100,
        (int) addFramePanel.getPreferredSize().getHeight()));

    JScrollPane innerScrollPane = new JScrollPane(timelinesPanel);
    JScrollBar tScroll = new JScrollBar(JScrollBar.HORIZONTAL);
    innerScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    innerScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    innerScrollPane.setHorizontalScrollBar(tScroll);

    outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.X_AXIS));
    outerPanel.add(namesPanel);
    outerPanel.add(innerScrollPane);
    outerPanel.add(addFramePanel);
    outerPanel.setPreferredSize(new Dimension(0,
        (int) outerPanel.getPreferredSize().getHeight()));

    JScrollPane outerScrollPane = new JScrollPane(outerPanel);
    outerScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    outerScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    outerScrollPane.setPreferredSize(new Dimension(1200, TIMELINE_HEIGHT - 50));

    JPanel outerOuterPanel = new JPanel(new BorderLayout());
    outerOuterPanel.add(outerScrollPane);

    this.setLayout(new BorderLayout());
    this.add(tScroll, BorderLayout.NORTH);
    this.add(outerScrollPane, BorderLayout.CENTER);
    this.setPreferredSize(new Dimension(1200, TIMELINE_HEIGHT));
  }

  private void addShape(String name, TimelinePanel timeline) {
    JLabel label = new JLabel(name, JLabel.TRAILING);
    JPanel labelPanel = new JPanel();
    labelPanel.add(label);
    labelPanel.setPreferredSize(new Dimension(
        (int) label.getPreferredSize().getWidth() + 10,
        (int) TimelinePanel.KEYFRAME_SIZE.getHeight()));
    label.setLabelFor(timeline);

    AddFrameButton addBtn = new AddFrameButton(name, modelCallback);
    DelShapeButton delBtn = new DelShapeButton(name, modelCallback);
    JPanel actionsPanel = new JPanel(new FlowLayout());
    actionsPanel.add(addBtn);
    actionsPanel.add(delBtn);

    namesPanel.add(labelPanel);
    timelinesPanel.add(timeline);
    addFramePanel.add(actionsPanel);

    // set the callbacks for the new buttons if the callback exists
    this.setViewCallback(this.viewCallback);
  }

  public void setViewCallback(Consumer<EditorAction> callback) {
    this.viewCallback = callback;
    for (TimelinePanel timeline : timelines.values()) {
      timeline.setViewCallback(callback);
    }
    for (Component comp : addFramePanel.getComponents()) {
      JPanel panel = (JPanel) comp;
      ((AddFrameButton) panel.getComponent(0)).setViewCallback(callback);
      ((DelShapeButton) panel.getComponent(1)).setViewCallback(callback);
    }
  }

  public void setTick(int tick) {
    for (TimelinePanel timeline : timelines.values()) {
      timeline.setTick(tick);
    }
  }

  public void update() {
    ArrayList<String> toRemove = new ArrayList<>();
    ArrayList<Integer> toRemoveInds = new ArrayList<>();
    int i = 0;
    for (String shapeName : timelines.keySet()) {
      if (!model.getKeyframes().containsKey(shapeName)) {
        toRemove.add(shapeName);
        toRemoveInds.add(i);
      }
      i += 1;
    }
    for (String name : toRemove) {
      timelines.remove(name);
    }
    for (int removeInd : toRemoveInds) {
      namesPanel.remove(removeInd);
      addFramePanel.remove(removeInd);
      timelinesPanel.remove(removeInd);
    }

    for (Map.Entry<String, SortedSet<Frame>> entry : model.getKeyframes().entrySet()) {
      if (!timelines.containsKey(entry.getKey())) {
        TimelinePanel timeline = new TimelinePanel(entry.getKey(), model, modelCallback);
        timelines.put(entry.getKey(), timeline);
        this.addShape(entry.getKey(), timeline);
      }
    }

    for (TimelinePanel timeline : timelines.values()) {
      timeline.updateButtonText();
    }
  }

  static class DelShapeButton extends JPanel {
    Consumer<EditorAction> viewCallback;

    /**
     * Makes a new panel with a button to add a frame for the given shape.
     *
     * @param shapeName the name of the shape to add a frame for
     * @param callback  the callback to use when the frame should be added
     */
    public DelShapeButton(String shapeName, Consumer<AnimatorAction> callback) {
      Dimension btnSize = new Dimension(50, (int) TimelinePanel.KEYFRAME_SIZE.getHeight() - 5);
      Dimension panelSize = new Dimension(50, (int) TimelinePanel.KEYFRAME_SIZE.getHeight());

      JButton delShapeButton = new JButton("Kill");
      delShapeButton.setPreferredSize(btnSize);
      this.setPreferredSize(panelSize);
      delShapeButton.addActionListener(e -> {
        callback.accept(new DeleteShape(shapeName));
        getViewCallback().accept(new RefreshView());
      });

      this.add(delShapeButton);
    }

    public Consumer<EditorAction> getViewCallback() {
      return viewCallback;
    }

    public void setViewCallback(Consumer<EditorAction> viewCallback) {
      this.viewCallback = viewCallback;
    }
  }

  /**
   * A class that holds a button that adds a frame at any time for a specific shape.
   */
  class AddFrameButton extends JPanel {
    Consumer<EditorAction> viewCallback;

    /**
     * Makes a new panel with a button to add a frame for the given shape.
     *
     * @param shapeName the name of the shape to add a frame for
     * @param callback  the callback to use when the frame should be added
     */
    public AddFrameButton(String shapeName, Consumer<AnimatorAction> callback) {
      Dimension btnSize = new Dimension(50, (int) TimelinePanel.KEYFRAME_SIZE.getHeight() - 5);
      Dimension panelSize = new Dimension(50, (int) TimelinePanel.KEYFRAME_SIZE.getHeight());

      JButton addFrameButton = new JButton("+");
      addFrameButton.setPreferredSize(btnSize);
      this.setPreferredSize(panelSize);
      addFrameButton.addActionListener(e -> {
        final JDialog dialog = new JDialog((Dialog) null, "Add Key Frame For Shape " + shapeName);

        JPanel panel = new JPanel();

        JSpinner tickSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        JButton addButton = new JButton("Add Keyframe");

        JLabel tickLabel = new JLabel("Time of new frame: ");
        tickLabel.setLabelFor(tickSpinner);
        panel.add(tickLabel);
        panel.add(tickSpinner);
        panel.add(addButton);

        addButton.addActionListener(evt -> {
          int newTick = (Integer) tickSpinner.getValue();
          callback.accept(new CreateKeyframe(shapeName, newTick));
          this.getViewCallback().accept(new SetTick(newTick));
          this.getViewCallback().accept(new RefreshView());
          dialog.dispose();
        });

        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setVisible(true);
      });

      this.add(addFrameButton);
    }

    public Consumer<EditorAction> getViewCallback() {
      return viewCallback;
    }

    public void setViewCallback(Consumer<EditorAction> viewCallback) {
      this.viewCallback = viewCallback;
    }
  }
}
