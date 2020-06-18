package com.company.controller.viewactions.editoractions;

import com.company.model.shape.PosnImpl;
import com.company.model.shape.ShapeType;
import com.company.model.shape.shapes.Rectangle;
import com.company.view.swing.editor.EditorView;

import java.awt.Color;

/**
 * Action that tells the editor view to create a new shape of the given type.
 */
public class CreateShape implements EditorAction {
  private ShapeType type;
  private String name;

  public CreateShape(ShapeType type, String name) {
    this.type = type;
    this.name = name;
  }

  @Override
  public void actOn(EditorView view) {
    view.createShape(type, name);
  }
}
