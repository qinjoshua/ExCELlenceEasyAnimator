package com.company.view.swing.editor;

import com.company.controller.animatoractions.AnimatorAction;
import com.company.controller.animatoractions.CreateKeyframe;
import com.company.controller.viewactions.editoractions.EditorAction;
import com.company.model.Frame;
import com.company.model.ReadOnlyAnimatorModel;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
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

  public TimelinesPanel(ReadOnlyAnimatorModel model, Consumer<AnimatorAction> modelCallback) {
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
      String name = entry.getKey();
      TimelinePanel timeline = entry.getValue();

      JLabel label = new JLabel(name, JLabel.TRAILING);
      JPanel labelPanel = new JPanel();
      labelPanel.add(label);
      labelPanel.setPreferredSize(new Dimension(
          (int) label.getPreferredSize().getWidth() + 10,
          (int) TimelinePanel.KEYFRAME_SIZE.getHeight()));
      label.setLabelFor(timeline);

      AddFrameButton btn = new AddFrameButton(name, modelCallback);

      namesPanel.add(labelPanel);
      timelinesPanel.add(timeline);
      addFramePanel.add(btn);
    }

//    timelinesPanel.setLayout(new BoxLayout(timelinesPanel, BoxLayout.Y_AXIS));
//    namesPanel.setLayout(new BoxLayout(namesPanel, BoxLayout.Y_AXIS));
//    addFramePanel.setLayout(new BoxLayout(addFramePanel, BoxLayout.Y_AXIS));
//    addFramePanel.setPreferredSize(new Dimension(100,
//        (int)addFramePanel.getPreferredSize().getHeight()));
//
//    JScrollPane innerScrollPane = new JScrollPane(timelinesPanel);
//    JScrollBar tScroll = new JScrollBar(JScrollBar.HORIZONTAL);
//    innerScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
//    innerScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
//    innerScrollPane.setHorizontalScrollBar(tScroll);
//    JPanel innerPanel = new JPanel(new BorderLayout());
//    innerPanel.add(innerScrollPane);
    
//    innerScrollPane.setPreferredSize(new Dimension(1500,
//        (int)innerScrollPane.getPreferredSize().getHeight()));

    outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.X_AXIS));
    outerPanel.add(namesPanel);
    outerPanel.add(innerPanel);
    outerPanel.add(addFramePanel);

    JScrollPane outerScrollPane = new JScrollPane(outerPanel);
    outerScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    outerScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
    outerScrollPane.setPreferredSize(new Dimension(1200, TIMELINE_HEIGHT - 50));
    JPanel outerOuterPanel = new JPanel(new BorderLayout());
    outerOuterPanel.add(outerScrollPane);
    this.setLayout(new BorderLayout());
    this.add(tScroll, BorderLayout.NORTH);
    this.add(outerOuterPanel, BorderLayout.CENTER);
    this.setPreferredSize(new Dimension(1200, TIMELINE_HEIGHT));




  }

  public void setViewCallback(Consumer<EditorAction> callback) {
    for (TimelinePanel timeline : timelines.values()) {
      timeline.setViewCallback(callback);
    }
  }

  public void setTick(int tick) {
    for (TimelinePanel timeline : timelines.values()) {
      timeline.setTick(tick);
    }
  }

  /**
   * A class that holds a button that adds a frame at any time for a specific shape.
   */
  class AddFrameButton extends JPanel {
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

        addButton.addActionListener(evt -> callback.accept(new CreateKeyframe(shapeName, (Integer) tickSpinner.getValue())));

        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setVisible(true);
      });

      this.add(addFrameButton);
    }
  }
}
