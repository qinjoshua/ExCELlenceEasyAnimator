package com.company.controller.viewactions.editoractions;

import com.company.view.swing.editor.EditorView;

import java.util.function.Consumer;

/**
 * Represents a consumer that performs an action on a editor view.
 */
public class EditorActionConsumerImpl implements Consumer<EditorAction> {
  final EditorView view;

  /**
   * Constructor that sets the editor view to be acted on.
   *
   * @param view editor view to be acted on
   */
  public EditorActionConsumerImpl(EditorView view) {
    this.view = view;
  }

  @Override
  public void accept(EditorAction editorAction) {
    editorAction.actOn(view);
  }
}
