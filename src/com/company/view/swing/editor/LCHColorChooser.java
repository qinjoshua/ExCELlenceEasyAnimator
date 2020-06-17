package com.company.view.swing.editor;

import com.company.controller.animatoractions.AnimatorAction;
import com.company.controller.animatoractions.ChangeColor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.function.Consumer;

import javax.swing.Icon;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A CIELCH-based color chooser panel.
 */
public class LCHColorChooser extends AbstractColorChooserPanel implements ChangeListener {
  JSpinner rSpinner;
  JSpinner gSpinner;
  JSpinner bSpinner;

  public LCHColorChooser(Color oldColor) {
    rSpinner = new JSpinner(new SpinnerNumberModel(oldColor.getRed(), 0, 255, 1));
    gSpinner = new JSpinner(new SpinnerNumberModel(oldColor.getGreen(), 0, 255, 1));
    bSpinner = new JSpinner(new SpinnerNumberModel(oldColor.getBlue(), 0, 255, 1));

    rSpinner.addChangeListener(this);
    gSpinner.addChangeListener(this);
    bSpinner.addChangeListener(this);
  }

  @Override
  public void updateChooser() {
    Color color = getColorFromModel();
    rSpinner.setValue(color.getRed());
    gSpinner.setValue(color.getGreen());
    bSpinner.setValue(color.getBlue());
  }

  @Override
  protected void buildChooser() {
    this.add(rSpinner);
    this.add(gSpinner);
    this.add(bSpinner);
  }

  @Override
  public String getDisplayName() {
    return "LCH";
  }

  @Override
  public Icon getSmallDisplayIcon() {
    return null;
  }

  @Override
  public Icon getLargeDisplayIcon() {
    return null;
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    Color newColor = new Color((int)rSpinner.getValue(), (int)gSpinner.getValue(),
        (int)bSpinner.getValue());
    getColorSelectionModel().setSelectedColor(newColor);
  }
}
