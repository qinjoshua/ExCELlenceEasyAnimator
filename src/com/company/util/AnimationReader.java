package com.company.util;

import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * A helper to read animation data and construct an animation from it.
 */
public class AnimationReader {
  /**
   * A factory for producing new animations, given a source of shapes and a builder for constructing
   * animations.
   *
   * <p>
   * The input file format consists of two types of lines:
   * <ul>
   * <li>Shape lines: the keyword "shape" followed by two identifiers (i.e.
   * alphabetic strings with no spaces), giving the unique name of the shape,
   * and the type of shape it is.</li>
   * <li>Motion lines: the keyword "motion" followed by an identifier giving the name
   * of the shape to move, and 16 integers giving the initial and final conditions of the motion:
   * eight numbers giving the time, the x and y coordinates, the width and height,
   * and the red, green and blue color values at the start of the motion; followed by
   * eight numbers for the end of the motion.  See {@link AnimationBuilder#addMotion}</li>
   * </ul>
   * </p>
   *
   * @param readable The source of data for the animation
   * @param builder  A builder for helping to construct a new animation
   * @param <Doc>    The main model interface type describing animations
   * @return the newly constructed document
   */
  public static <Doc> Doc parseFile(Readable readable, AnimationBuilder<Doc> builder) {
    Objects.requireNonNull(readable, "Must have non-null readable source");
    Objects.requireNonNull(builder, "Must provide a non-null AnimationBuilder");
    Scanner s = new Scanner(readable);
    // Split at whitespace, and ignore # comment lines
    s.useDelimiter(Pattern.compile("(\\p{Space}+|#.*)+"));
    while (s.hasNext()) {
      String word = s.next();
      switch (word) {
        case "canvas":
          readCanvas(s, builder);
          break;
        case "shape":
          readShape(s, builder);
          break;
        case "motion":
          readMotion(s, builder);
          break;
        case "layer":
          readLayer(s, builder);
          break;
        default:
          throw new IllegalStateException("Unexpected keyword: " + word + s.nextLine());
      }
    }
    return builder.build();
  }

  private static <Doc> void readLayer(Scanner s, AnimationBuilder<Doc> builder) {
    String name;
    if (s.hasNext()) {
      name = s.next();
    } else {
      throw new IllegalStateException("Layer: Expected a name, but no more input available");
    }

    builder.declareLayer(name);
  }

  private static <Doc> void readCanvas(Scanner s, AnimationBuilder<Doc> builder) {
    double[] vals = new double[4];
    String[] fieldNames = {"left", "top", "width", "height"};
    for (int i = 0; i < 4; i++) {
      vals[i] = getDouble(s, "Canvas", fieldNames[i]);
    }
    builder.setBounds((int) vals[0], (int) vals[1], (int) vals[2], (int) vals[3]);
  }

  private static <Doc> void readShape(Scanner s, AnimationBuilder<Doc> builder) {
    String name;
    String type;
    // make the IDE happy even though this can never be null and actually used
    String layer = "";
    String line = s.nextLine();
    Scanner lineScanner = new Scanner(line);
    boolean isUsingLayer;
    if (lineScanner.hasNext()) {
      name = lineScanner.next();
    } else {
      throw new IllegalStateException("Shape: Expected a name, but no more input available");
    }
    if (lineScanner.hasNext()) {
      type = lineScanner.next();
    } else {
      throw new IllegalStateException("Shape: Expected a type, but no more input available");
    }
    if (lineScanner.hasNext()) {
      layer = lineScanner.next();
      isUsingLayer = true;
    } else {
      isUsingLayer = false;
    }
    if (isUsingLayer) {
      builder.declareShape(name, type, layer);
    } else {
      builder.declareShape(name, type);
    }
  }

  private static <Doc> void readMotion(Scanner s, AnimationBuilder<Doc> builder) {
    String[] fieldNames = new String[]{
            "initial time",
            "initial x-coordinate", "initial y-coordinate",
            "initial width", "initial height",
            "initial red value", "initial green value", "initial blue value",
            "initial angle",
            "final time",
            "final x-coordinate", "final y-coordinate",
            "final width", "final height",
            "final red value", "final green value", "final blue value",
            "final angle",
    };

    String name;
    if (s.hasNext()) {
      name = s.next();
    } else {
      throw new IllegalStateException("Motion: Expected a shape name, but no more input available");
    }

    String line = s.nextLine();

    boolean usingAngles = line.split(" +").length == 19;

    Scanner lineScanner = new Scanner(line);
    lineScanner.useDelimiter(" +");
    double[] vals = new double[18];

    for (int i = 0; i < 18; i++) {
      if (i == 8 || i == 17) {
        if (usingAngles) {
          vals[i] = getDouble(lineScanner, "Motion", fieldNames[i]) % (2 * Math.PI);
        } else {
          vals[i] = 0;
        }
      } else {
        vals[i] = getDouble(lineScanner, "Motion", fieldNames[i]);
      }
    }
    builder.addMotion(name,
            (int) vals[0], (int) vals[1], (int) vals[2], (int) vals[3], (int) vals[4],
            (int) vals[5], (int) vals[6], (int) vals[7], vals[8],
            (int) vals[9], (int) vals[10], (int) vals[11], (int) vals[12], (int) vals[13],
            (int) vals[14], (int) vals[15], (int) vals[16], vals[17]);
  }

  private static double getDouble(Scanner s, String label, String fieldName) {
    if (s.hasNextDouble()) {
      return s.nextDouble();
    } else if (s.hasNext()) {
      throw new IllegalStateException(
              String.format("%s: expected integer for %s, got: %s", label, fieldName, s.next()));
    } else {
      throw new IllegalStateException(
              String.format("%s: expected integer for %s, but no more input available",
                      label, fieldName));
    }
  }
}
