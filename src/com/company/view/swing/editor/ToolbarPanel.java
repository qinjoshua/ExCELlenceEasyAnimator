package com.company.view.swing.editor;

import com.company.controller.animatoractions.AnimatorAction;
import com.company.controller.animatoractions.OpenPreview;
import com.company.controller.viewactions.editoractions.CreateShape;
import com.company.controller.viewactions.editoractions.EditorAction;
import com.company.model.ReadOnlyAnimatorModel;
import com.company.model.shape.ShapeType;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Panel that contains the tools for controlling the editor. Includes the ability to create new
 * shapes, preview the animation, and export the animation.
 */
public class ToolbarPanel extends JPanel {
  private final Map<String, AbstractButton> buttons;
  private final ReadOnlyAnimatorModel model;
  private final Consumer<AnimatorAction> modelCallback;
  private Consumer<EditorAction> callback;

  public ToolbarPanel(Consumer<AnimatorAction> modelCallback, ReadOnlyAnimatorModel model) {
    this.buttons = new HashMap<>();

    this.model = model;

    this.modelCallback = modelCallback;
    this.setLayout(new GridLayout(0, 1));

    JButton previewButton = new JButton("▶");
    JButton exportButton = new JButton("⛏");

    JButton circleButton = new JButton("●");
    JButton rectButton = new JButton("■");

    previewButton.setToolTipText("Preview/Play animation");
    exportButton.setToolTipText("Build/Export animation");
    circleButton.setToolTipText("Create circle");
    rectButton.setToolTipText("Create rectangle");

    buttons.put("preview", previewButton);
    buttons.put("export", exportButton);
    buttons.put("circle", circleButton);
    buttons.put("rectangle", rectButton);

    this.add(previewButton);
    this.add(exportButton);
    this.add(new JSeparator(SwingConstants.HORIZONTAL));
    this.add(circleButton);
    this.add(rectButton);
    this.add(new JSeparator(SwingConstants.HORIZONTAL));

    final Action PREVIEW = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        modelCallback.accept(new OpenPreview());
      }
    };

    final Action CREATE_RECTANGLE = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        openNameDialog(ShapeType.Rectangle);
      }
    };

    final Action CREATE_ELLIPSE = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        openNameDialog(ShapeType.Ellipse);
      }
    };

    previewButton.addActionListener(PREVIEW);
    rectButton.addActionListener(CREATE_RECTANGLE);
    circleButton.addActionListener(CREATE_ELLIPSE);
  }

  void setCallback(Consumer<EditorAction> callback) {
    this.callback = callback;
  }

  private void openNameDialog(ShapeType type) {
    final JDialog dialog = new JDialog((Frame) null, "Excellence Name Dialog");

    JPanel panel = new JPanel();

    JTextField txtFieldName = new JTextField(15);
    JButton nameButton = new JButton("Create");

    panel.add(txtFieldName);
    panel.add(nameButton);

    nameButton.addActionListener(e -> {
      if (!model.getKeyframes().containsKey(txtFieldName.getText())) {
        callback.accept(new CreateShape(type, txtFieldName.getText()));
        dialog.dispose();
      } else {
        txtFieldName.setText("");
        JOptionPane.showMessageDialog(null,
            "The name \"" + txtFieldName.getText() + "\" is already in" +
                " use. Please choose another name.",
            "Excellence " +
                "Warning", JOptionPane.WARNING_MESSAGE);
      }
    });

    dialog.getContentPane().add(panel);
    dialog.pack();
    dialog.setVisible(true);
  }

}
