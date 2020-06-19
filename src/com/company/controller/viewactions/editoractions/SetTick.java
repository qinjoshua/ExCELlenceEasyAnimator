package com.company.controller.viewactions.editoractions;

import com.company.view.swing.editor.EditorView;

/**
 * An action that sets the current tick of the editor view.
 */
public class SetTick implements EditorAction {
  final int tick;

  /**
   * Creates an action that sets the tick to the given value.
   *
   * @param tick the tick to set the editor view to
   */
  public SetTick(int tick) {
    this.tick = tick;
  }

  @Override
  public void actOn(EditorView view) {
    view.setTick(tick);
    view.refreshView();
  }
}
