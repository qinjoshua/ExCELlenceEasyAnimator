package com.company.view.swing.editor.boundingbox;

public enum AnchorType {
  Top(0, -1), Bottom(0, 1), Left(-1, 0), Right(1, 0);

  int x;
  int y;

  AnchorType(int x, int y) {
    this.x = x;
    this.y = y;
  }
}