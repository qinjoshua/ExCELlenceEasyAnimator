package com.company.view.text;

import com.company.model.Frame;
import com.company.model.ReadOnlyAnimatorModel;
import com.company.view.TextView;

import java.io.IOException;
import java.util.Map;
import java.util.SortedSet;

/**
 * A view that outputs the results of the animation in several lies of texts, describing each
 * keyframe of each shape in order order of time and placement.
 */
public class TextAnimatorView implements TextView {
  private final ReadOnlyAnimatorModel model;
  private final int canvasWidth;
  private final int canvasHeight;
  private final int canvasX;
  private final int canvasY;


  public TextAnimatorView(ReadOnlyAnimatorModel model, int canvasWidth, int canvasHeight,
                          int canvasX, int canvasY) {
    this.model = model;
    this.canvasWidth = canvasWidth;
    this.canvasHeight = canvasHeight;
    this.canvasX = canvasX;
    this.canvasY = canvasY;
  }

  /**
   * Renders all of the shapes in the model in text format.
   *
   * @return String describing all the keyframes of the model
   */
  private String renderShapes() {
    StringBuilder renderString = new StringBuilder();

    Map<String, SortedSet<Frame>> timelines = model.getKeyframes();

    renderString.append(String.format("canvas %d %d %d %d\n",
        this.canvasWidth, this.canvasHeight, this.canvasX, this.canvasY));
    for (Map.Entry<String, SortedSet<Frame>> timeline : timelines.entrySet()) {
      renderString.append("shape ").append(timeline.getKey()).append(" ").
          append(timeline.getValue().first().getShape().getShapeType()).append("\n");

      Frame prevFrame = null;

      if (timeline.getValue().size() == 1) {
        renderString.append("motion\t").append(timeline.getKey()).append("\t")
            .append(timeline.getValue().first());
      } else {
        for (Frame frame : timeline.getValue()) {
          if (prevFrame != null) {
            renderString.append("motion\t").append(timeline.getKey()).append("\t")
                .append(prevFrame).append("\t\t");
            renderString.append(timeline.getKey()).append("\t").append(frame).append("\n");
          }
          prevFrame = frame;
        }
      }
      renderString.append("\n");
    }
    return renderString.toString().stripTrailing();
  }

  @Override
  public void outputText(Appendable out) throws IOException {
    out.append(this.renderShapes());
  }
}
