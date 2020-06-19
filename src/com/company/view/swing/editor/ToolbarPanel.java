package com.company.view.swing.editor;

import com.company.controller.viewactions.editoractions.CreateShape;
import com.company.controller.viewactions.editoractions.EditorAction;
import com.company.controller.viewactions.editoractions.OpenPreview;
import com.company.model.ReadOnlyAnimatorModel;
import com.company.model.shape.ShapeType;
import com.company.view.svg.SVGView;
import com.company.view.svg.SVGViewImpl;
import com.company.view.text.TextAnimatorView;
import com.company.view.text.TextView;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Panel that contains the tools for controlling the editor. Includes the ability to create new
 * shapes, preview the animation, and export the animation.
 */
public class ToolbarPanel extends JPanel {
  private final ReadOnlyAnimatorModel model;
  private Consumer<EditorAction> callback;

  /**
   * Creates the toolbar panel with the given read-only model.
   *
   * @param model the read-only model that this toolbar can retrieve information from
   */
  public ToolbarPanel(ReadOnlyAnimatorModel model) {
    Map<String, AbstractButton> buttons = new HashMap<>();

    this.model = model;

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

    final Action preview = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        callback.accept(new OpenPreview(model));
      }
    };

    final Action createRectangle = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        openNameDialog(ShapeType.Rectangle);
      }
    };

    final Action createEllipse = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        openNameDialog(ShapeType.Ellipse);
      }
    };

    final Action export = new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        exportDialog();
      }
    };

    previewButton.addActionListener(preview);
    rectButton.addActionListener(createRectangle);
    circleButton.addActionListener(createEllipse);
    exportButton.addActionListener(export);
  }

  void setCallback(Consumer<EditorAction> callback) {
    this.callback = callback;
  }

  private void exportDialog() {
    final JDialog dialog = new JDialog((Frame) null, "Excellence Exporter");

    JPanel panel = new JPanel();

    panel.add(new JLabel("How would you like to export your animation?"));

    ButtonGroup exportOptions = new ButtonGroup();
    JRadioButton svgOption = new JRadioButton("SVG");
    JRadioButton txtOption = new JRadioButton("TXT");

    exportOptions.add(svgOption);
    exportOptions.add(txtOption);

    panel.add(svgOption);
    panel.add(txtOption);

    JButton export = new JButton("Export");
    panel.add(export);

    export.addActionListener(e -> {
      JFileChooser fileChooser = new JFileChooser();
      fileChooser.setDialogTitle("Specify a file to save");

      String description = "";
      String extension = "";

      if (svgOption.isSelected()) {
        description = "SVG file";
        extension = "svg";
      } else if (txtOption.isSelected()) {
        description = "Text file";
        extension = "svg";
      } else {
        JOptionPane.showMessageDialog(null,
                "Please select an export option",
                "Excellence " +
                        "Warning", JOptionPane.WARNING_MESSAGE);
      }

      FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extension);
      fileChooser.addChoosableFileFilter(filter);

      int userSelection = fileChooser.showSaveDialog(this);

      if (userSelection == JFileChooser.APPROVE_OPTION) {
        try {
          Appendable out = new FileWriter(fileChooser.getSelectedFile().getAbsolutePath());

          if (svgOption.isSelected()) {
            SVGView svg = new SVGViewImpl(model, 20);
            svg.outputSVG(out);
          } else if (txtOption.isSelected()) {
            TextView txt = new TextAnimatorView(model);
            txt.outputText(out);
          }
          ((Closeable) out).close();

        } catch (IOException ioException) {
          JOptionPane.showMessageDialog(null,
                  "The file could not be created. Please contact customer support for help.",
                  "Excellence " +
                          "Error", JOptionPane.WARNING_MESSAGE);
        }

        dialog.dispose();
      }
    });

    dialog.getContentPane().add(panel);
    dialog.pack();
    dialog.setVisible(true);
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
        JOptionPane.showMessageDialog(null,
                "The name \"" + txtFieldName.getText() + "\" is already in" +
                        " use. Please choose another name.",
                "Excellence " +
                        "Warning", JOptionPane.WARNING_MESSAGE);
        txtFieldName.setText("");
      }
    });

    dialog.getContentPane().add(panel);
    dialog.pack();
    dialog.setVisible(true);
  }
}
