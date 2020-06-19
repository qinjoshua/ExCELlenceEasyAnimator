package com.company.controller.viewactions.menuactions;

import com.company.controller.animatoractions.AnimatorActionConsumerImpl;
import com.company.controller.viewactions.editoractions.EditorActionConsumerImpl;
import com.company.model.AnimatorModel;
import com.company.model.AnimatorModelImpl;
import com.company.util.AnimationBuilder;
import com.company.util.AnimationReader;
import com.company.view.swing.editor.EditorView;
import com.company.view.swing.editor.EditorViewImpl;
import com.company.view.swing.menu.MenuView;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Opens the editor to a model read in from a file.
 */
public class OpenFile implements MenuAction {
  private final String fileName;

  /**
   * Sets the file name/path to be opened.
   *
   * @param fileName the path of the file to be opened
   */
  public OpenFile(String fileName) {
    this.fileName = fileName;
  }

  @Override
  public void actOn(MenuView view) {
    try {
      BufferedReader input = Files.newBufferedReader(Paths.get(fileName));
      AnimationBuilder<AnimatorModel> builder = new AnimatorModelImpl.Builder();
      AnimatorModel model = AnimationReader.parseFile(input, builder);

      EditorView editorView = new EditorViewImpl(model, new AnimatorActionConsumerImpl(model));
      editorView.setCallback(new EditorActionConsumerImpl(editorView));
      editorView.renderVisual();

      view.close();
    } catch (IOException e) {
      // TODO
    }
  }
}
