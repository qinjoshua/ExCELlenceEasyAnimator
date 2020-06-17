package com.company.view.swing.editor.boundingbox;

import java.awt.Cursor;

public enum AnchorType {
  Top(0, -1, Cursor.N_RESIZE_CURSOR),
  Bottom(0, 1, Cursor.S_RESIZE_CURSOR),
  Left(-1, 0, Cursor.W_RESIZE_CURSOR),
  Right(1, 0, Cursor.E_RESIZE_CURSOR),
  TopLeft(-1, -1, Cursor.NW_RESIZE_CURSOR),
  TopRight(1, -1, Cursor.NE_RESIZE_CURSOR),
  BottomLeft(-1, 1, Cursor.SW_RESIZE_CURSOR),
  BottomRight(1, 1, Cursor.SE_RESIZE_CURSOR);

  private final int x;
  private final int y;
  private final int cursor;

  AnchorType(int x, int y, int cursor) {
    this.x = x;
    this.y = y;
    this.cursor = cursor;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getCursor() {
    return cursor;
  }
}