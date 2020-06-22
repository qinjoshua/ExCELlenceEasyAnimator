package com.company.view.swing.editor;

import com.company.controller.animatoractions.AnimatorAction;
import com.company.controller.animatoractions.CreateKeyframe;
import com.company.controller.animatoractions.DeleteShape;
import com.company.controller.viewactions.editoractions.EditorAction;
import com.company.controller.viewactions.editoractions.HighlightShape;
import com.company.controller.viewactions.editoractions.RefreshView;
import com.company.controller.viewactions.editoractions.SetTick;
import com.company.model.Frame;
import com.company.model.ReadOnlyAnimatorModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;

/**
 * Represents a panel to show all of the timelines for each specific shape.
 */
public class TimelinesPanel extends JPanel {
  private static final int TIMELINE_HEIGHT = 200;
  private final JPanel timelinesPanel;
  private final JPanel namesPanel;
  private final JPanel addFramePanel;

  private JPanel highlightedNamePanel;
  private final Map<String, JPanel> namesPanels;
  private JPanel highlightedTimelinePanel;
  private final Map<String, TimelinePanel> timelines;

  private int timelinesHeight;

  private final ReadOnlyAnimatorModel model;
  private final Consumer<AnimatorAction> modelCallback;
  private Consumer<EditorAction> viewCallback;

  private final JPanel outerPanel;

  private static final int TIMELINE_OFFSET_SIZE = 2;

  /**
   * Creates a timelines panel with the given model and callback for the model.
   *
   * @param model         read-only model to for the timelines panel to retrieve information from
   * @param modelCallback callback for requesting changes to the model
   */
  public TimelinesPanel(ReadOnlyAnimatorModel model, Consumer<AnimatorAction> modelCallback) {
    this.model = model;
    this.modelCallback = modelCallback;
    timelinesPanel = new JPanel();
    namesPanel = new JPanel();
    addFramePanel = new JPanel();
    this.outerPanel = new JPanel();

    this.highlightedNamePanel = null;
    this.namesPanels = new HashMap<>();
    this.highlightedTimelinePanel = null;
    timelines = new LinkedHashMap<>();

    Map<String, SortedSet<Frame>> frames = model.getKeyframes();

    for (Map.Entry<String, SortedSet<Frame>> entry : frames.entrySet()) {
      timelines.put(entry.getKey(), new TimelinePanel(entry.getKey(), model, modelCallback));
    }

    timelinesPanel.setLayout(new BoxLayout(timelinesPanel, BoxLayout.Y_AXIS));
    namesPanel.setLayout(new BoxLayout(namesPanel, BoxLayout.Y_AXIS));
    addFramePanel.setLayout(new BoxLayout(addFramePanel, BoxLayout.Y_AXIS));
    addFramePanel.setPreferredSize(new Dimension(110,
            (int) addFramePanel.getPreferredSize().getHeight()));

    timelinesPanel.add(Box.createVerticalStrut(TIMELINE_OFFSET_SIZE));
    this.timelinesHeight = TIMELINE_OFFSET_SIZE;

    for (Map.Entry<String, TimelinePanel> entry : timelines.entrySet()) {
      this.addShape(entry.getKey(), entry.getValue());
    }

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
            this.timelinesHeight));

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
    JPanel actionsPanel = new JPanel(new BorderLayout());
    actionsPanel.add(addBtn, BorderLayout.WEST);
    actionsPanel.add(delBtn, BorderLayout.EAST);
    actionsPanel.setPreferredSize(new Dimension(110,
        (int) TimelinePanel.KEYFRAME_SIZE.getHeight()));

    namesPanels.put(name, labelPanel);

    namesPanel.add(labelPanel);
    timelinesPanel.add(timeline);
    addFramePanel.add(actionsPanel);

    this.timelinesHeight += timeline.getPreferredSize().getHeight();

    // set the callbacks for the new buttons if the callback exists
    this.setViewCallback(this.viewCallback);
  }

  /**
   * Sets the view callback.
   *
   * @param callback the view callback
   */
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

  /**
   * Sets the tick for this timelines panel.
   *
   * @param tick the tick to be set
   */
  public void setTick(int tick) {
    for (TimelinePanel timeline : timelines.values()) {
      timeline.setTick(tick);
    }
  }

  /**
   * Updates the timelines to any changes made in the model.
   *
   * @param tick the tick to update the selection to
   */
  public void update(int tick) {
    ArrayList<String> toRemove = new ArrayList<>();
    ArrayList<Integer> toRemoveIndices = new ArrayList<>();
    int i = 0;
    for (String shapeName : timelines.keySet()) {
      if (!model.getKeyframes().containsKey(shapeName)) {
        toRemove.add(shapeName);
        toRemoveIndices.add(i);
      }
      i += 1;
    }
    for (String name : toRemove) {
      timelines.remove(name);
    }
    for (int removeInd : toRemoveIndices) {
      namesPanel.remove(removeInd);
      addFramePanel.remove(removeInd);
      timelinesPanel.remove(removeInd + 1);
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

    this.revalidate();
    this.repaint();
    this.setTick(tick);
  }

  private static class DelShapeButton extends JPanel {
    Consumer<EditorAction> viewCallback;

    /**
     * Makes a new panel with a button to add a frame for the given shape.
     *
     * @param shapeName the name of the shape to add a frame for
     * @param callback  the callback to use when the frame should be added
     */
    public DelShapeButton(String shapeName, Consumer<AnimatorAction> callback) {
      Dimension btnSize = new Dimension(50, (int) TimelinePanel.KEYFRAME_SIZE.getHeight() - 5);

      JButton delShapeButton = new JButton();
      delShapeButton.setIcon(new ImageIcon(this.getClass().getResource("/icons/trash.png")));
      delShapeButton.setToolTipText("Delete the shape \"" + shapeName + "\" in the timeline");
      delShapeButton.setPreferredSize(btnSize);
      this.setPreferredSize(btnSize);
      delShapeButton.addActionListener(e -> {
        Object[] options = {"Yes, kill it with fire!", "No, it's too young to die!"};
        int response = JOptionPane.showOptionDialog(
                null,
                String.format("Are you sure you want to delete shape %s? This cannot be undone.",
                        shapeName),
                "Delete Shape",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[1]
        );
        if (response == JOptionPane.YES_OPTION) {
          callback.accept(new DeleteShape(shapeName));
          getViewCallback().accept(new HighlightShape(null));
          getViewCallback().accept(new RefreshView());
        }
      });

      this.add(delShapeButton);
    }

    /**
     * Gets the view callback.
     *
     * @return the view callback
     */
    public Consumer<EditorAction> getViewCallback() {
      return viewCallback;
    }

    /**
     * Sets the view callback.
     *
     * @param viewCallback the view callback
     */
    public void setViewCallback(Consumer<EditorAction> viewCallback) {
      this.viewCallback = viewCallback;
    }
  }

  /**
   * A class that holds a button that adds a frame at any time for a specific shape.
   */
  static class AddFrameButton extends JPanel {
    Consumer<EditorAction> viewCallback;

    /**
     * Makes a new panel with a button to add a frame for the given shape.
     *
     * @param shapeName the name of the shape to add a frame for
     * @param callback  the callback to use when the frame should be added
     */
    public AddFrameButton(String shapeName, Consumer<AnimatorAction> callback) {
      Dimension btnSize = new Dimension(50, (int) TimelinePanel.KEYFRAME_SIZE.getHeight() - 5);

      JButton addFrameButton = new JButton();
      addFrameButton.setIcon(new ImageIcon(this.getClass().getResource("/icons/plus.png")));

      addFrameButton.setToolTipText("Add a new frame to the shape + \"" + shapeName + "\"");
      addFrameButton.setPreferredSize(btnSize);
      this.setPreferredSize(btnSize);
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

  void highlightPanel(String name) {
    this.deHighlightPanel();
    this.highlightedNamePanel = this.namesPanels.get(name);
    this.highlightedNamePanel.setBackground(new Color(128, 179, 229));
    this.highlightedTimelinePanel = this.timelines.get(name);
    this.highlightedTimelinePanel.setBackground(new Color(128, 179, 229));
  }

  void deHighlightPanel() {
    if (this.highlightedNamePanel != null) {
      this.highlightedNamePanel.setBackground(UIManager.getColor( "Panel.background" ));
      this.highlightedNamePanel = null;
      this.highlightedTimelinePanel.setBackground(UIManager.getColor( "Panel.background" ));
      this.highlightedTimelinePanel = null;
    }
  }
}
