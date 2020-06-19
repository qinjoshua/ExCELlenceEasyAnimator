package com.company.controller.viewactions.menuactions;

import com.company.controller.animatoractions.AnimatorActionConsumerImpl;
import com.company.controller.viewactions.editoractions.EditorActionConsumerImpl;
import com.company.model.AnimatorModel;
import com.company.model.AnimatorModelImpl;
import com.company.view.swing.editor.EditorView;
import com.company.view.swing.editor.EditorViewImpl;
import com.company.view.swing.menu.MenuView;

/**
 * Action for creating a new blank project, and launching the editor window.
 */
public class NewProject implements MenuAction {
  @Override
  public void actOn(MenuView view) {
    AnimatorModel model = new AnimatorModelImpl();

    EditorView editorView = new EditorViewImpl(model, new AnimatorActionConsumerImpl(model));
    editorView.setCallback(new EditorActionConsumerImpl(editorView));
    editorView.renderVisual();

    view.close();
  }
}