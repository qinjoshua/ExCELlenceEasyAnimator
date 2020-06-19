package com.company.view.swing.editor;

import com.company.controller.animatoractions.AnimatorAction;
import com.company.controller.animatoractions.RemoveKeyframe;
import com.company.controller.viewactions.editoractions.EditorAction;
import com.company.controller.viewactions.editoractions.HighlightShape;
import com.company.controller.viewactions.editoractions.RefreshView;
import com.company.model.ReadOnlyAnimatorModel;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A panel representing the top banner of the application.
 */
public class BannerPanel extends JPanel {
  String shapeName;
  int tick;
  ReadOnlyAnimatorModel model;
  Consumer<AnimatorAction> modelCallback;
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
    this.setPreferredSize(new Dimension(1200, 50));
    this.setLayout(new FlowLayout());
    this.add(new JLabel("Excellence Animation Editor"));
    if (shapeName != null) {
      this.add(new JLabel(String.format("Current Shape: %s", shapeName)));
    }
    this.add(new JLabel(String.format("Current Time: %d", tick)));
    if (shapeName != null) {
      JButton delButton = new JButton("Delete Current Frame");
      delButton.addActionListener(e -> {
        modelCallback.accept(new RemoveKeyframe(shapeName, tick));
        getViewCallback().accept(new HighlightShape(null));
        getViewCallback().accept(new RefreshView());
      });
      this.add(delButton);
    }
  }

  public Consumer<EditorAction> getViewCallback() {
    return viewCallback;
  }

  public void setViewCallback(Consumer<EditorAction> viewCallback) {
    this.viewCallback = viewCallback;
  }
}
