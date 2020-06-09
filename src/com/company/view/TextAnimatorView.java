package com.company.view;

import com.company.model.Frame;
import com.company.model.ReadOnlyAnimatorModel;

import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

public class TextAnimatorView implements AnimatorView {
  private final ReadOnlyAnimatorModel model;

  public TextAnimatorView(ReadOnlyAnimatorModel model) {
    this.model = model;
  }

  /**
   * Renders all of the shapes in the model in text format.
   *
   * @return String describing all the keyframes of the model
   */
  private String renderShapes() {
    StringBuilder renderString = new StringBuilder();

    SortedMap<String, SortedSet<Frame>> timelines = model.getKeyframes();

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
}
