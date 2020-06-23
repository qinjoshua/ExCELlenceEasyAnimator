package com.company.view.text;

import com.company.model.Frame;
import com.company.model.ReadOnlyAnimatorModel;

import java.io.IOException;
import java.util.Map;
import java.util.NavigableSet;
import java.util.SortedSet;

/**
 * A view that outputs the results of the animation in several lies of texts, describing each
 * keyframe of each shape in order order of time and placement.
 */
public class TextAnimatorView implements TextView {
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

    Map<String, SortedSet<Frame>> timelines = model.getKeyframes();

    renderString.append(String.format("canvas %d %d %d %d\n",
        this.model.getCanvasX(),
        this.model.getCanvasY(),
        this.model.getCanvasWidth(),
        this.model.getCanvasHeight()
    ));
    for (String layerName : model.getLayers()) {
      renderString.append("layer ").append(layerName).append("\n");
    }
    for (String layerName : model.getLayers()) {
      renderString.append("layer ").append(layerName).append("\n");
      for (String shapeName : model.getShapesInLayer(layerName)) {
        SortedSet<Frame> timeline = timelines.get(shapeName);
        renderString.append("shape ")
            .append(shapeName).append(" ")
            .append(timelines.get(shapeName).first().getShape().getShapeType()).append(" ")
            .append(layerName)
            .append("\n");
      }
    }
    for (String layerName : model.getLayers()) {
      for (String shapeName : model.getShapesInLayer(layerName)) {
        SortedSet<Frame> timeline = timelines.get(shapeName);
        Frame prevFrame = null;

        if (timeline.size() == 1) {
          renderString.append("motion ").append(shapeName).append(" ")
              .append(timeline.first());
        } else {
          for (Frame frame : timeline) {
            if (prevFrame != null) {
              renderString.append("motion ").append(shapeName).append(" ")
                  .append(prevFrame).append("  ").append(frame).append("\n");
            }
            prevFrame = frame;
          }
        }
        renderString.append("\n");
      }
    }
    // for java 8
    // return renderString.toString();
    return renderString.toString().stripTrailing();
  }

  @Override
  public void outputText(Appendable out) throws IOException {
    out.append(this.renderShapes());
  }
}
