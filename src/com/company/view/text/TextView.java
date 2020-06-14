package com.company.view.text;

import java.io.IOException;

/**
 * A textual view that renders a human-readable plaintext description of an animation.
 */
public interface TextView {
  /**
   * Outputs a textual description of the animation to the given Appendable output stream.
   *
   * @param out an appendable representing the output stream that the view is written to
   * @throws IllegalArgumentException if the given output stream is {@code null}
   * @throws IOException              if the given output stream cannot be written to
   */
  void outputText(Appendable out) throws IOException;
}
