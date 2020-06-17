package com.company.view.swing.editor.boundingbox;

public enum AnchorType {
  Top(0, -1), Bottom(0, 1), Left(-1, 0), Right(1, 0),
  TopLeft(-1, -1), TopRight(1, -1), BottomLeft(-1, 1), BottomRight(1, 1);

  int x;
  int y;

  AnchorType(int x, int y) {
    this.x = x;
    this.y = y;
  }
}