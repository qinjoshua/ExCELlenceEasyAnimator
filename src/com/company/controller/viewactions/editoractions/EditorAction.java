package com.company.controller.viewactions.editoractions;

import com.company.view.swing.editor.EditorView;

/**
 * An action on an editor view.
 */
public interface EditorAction {
  /**
   * Runs the given action on the editor view.
   *
   * @param view the editor view to be acted on
   * @throws IllegalStateException if the action was incorrectly created
   */
  void actOn(EditorView view) throws IllegalStateException;
}
