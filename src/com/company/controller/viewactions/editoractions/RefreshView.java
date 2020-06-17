package com.company.controller.viewactions.editoractions;

import com.company.view.swing.editor.EditorView;

/**
 * Action to refresh the view, updating it based on its own internal state.
 */
public class RefreshView implements EditorAction {
  /**
   * Refreshes the view, updating its display based on the underlying model.
   *
   * @param view the editor view to be acted on
   */
  @Override
  public void actOn(EditorView view) {
    view.refreshView();
  }
}
