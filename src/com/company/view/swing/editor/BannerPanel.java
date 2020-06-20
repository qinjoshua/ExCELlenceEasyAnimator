package com.company.view.swing.editor;

import com.company.controller.animatoractions.AnimatorAction;
import com.company.controller.animatoractions.RemoveKeyframe;
import com.company.controller.viewactions.editoractions.EditorAction;
import com.company.controller.viewactions.editoractions.HighlightShape;
import com.company.controller.viewactions.editoractions.RefreshView;
import com.company.model.ReadOnlyAnimatorModel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * A panel representing the top banner of the application.
 */
public class BannerPanel extends JPanel {
  final String shapeName;
  final int tick;
  final ReadOnlyAnimatorModel model;
  final Consumer<AnimatorAction> modelCallback;
  Consumer<EditorAction> viewCallback;

  /**
   * Creates a new panel for the banner of the editor view.
   *
   * @param shapeName name of the shape that is currently being edited
   * @param tick      current tick in the timeline
   * @param model     a read-only model to retrieve information from
   * @param callback  a callback to request changes to the model
   */
  public BannerPanel(String shapeName, int tick, ReadOnlyAnimatorModel model,
                     Consumer<AnimatorAction> callback) {
    this.shapeName = shapeName;
    this.tick = tick;
    this.model = model;
    this.modelCallback = callback;
    this.viewCallback = null;
    this.setPreferredSize(new Dimension(1200, 35));
    JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 0));
    if (shapeName != null) {
      infoPanel.add(new JLabel(String.format("<html><h4>Current Shape: %s</h4></html>",
          shapeName)));
    }
    infoPanel.add(new JLabel(String.format("<html><h4>Current Time: %d</h4></html>\"", tick)));
    if (shapeName != null) {
      JButton delButton = new JButton("<html><h4>Delete Current Frame</h4></html>");
      delButton.setMargin(new Insets(0, 0, 0, 0));
      delButton.setPreferredSize(new Dimension(
          (int) delButton.getPreferredSize().getWidth(),
          25));
      delButton.addActionListener(e -> {
        modelCallback.accept(new RemoveKeyframe(shapeName, tick));
        getViewCallback().accept(new HighlightShape(null));
        getViewCallback().accept(new RefreshView());
      });
      infoPanel.add(delButton);
    }

    this.setLayout(new BorderLayout());
    JLabel mainLabel = new JLabel("<html><h2>Excellence Animation Editor</h2></html>");
    mainLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
    this.add(mainLabel, BorderLayout.WEST);
    this.add(infoPanel, BorderLayout.EAST);
  }

  public Consumer<EditorAction> getViewCallback() {
    return viewCallback;
  }

  public void setViewCallback(Consumer<EditorAction> viewCallback) {
    this.viewCallback = viewCallback;
  }
}
