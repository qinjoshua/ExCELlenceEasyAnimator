package com.company.view.swing.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.function.Function;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A CIELCH-based color chooser panel.
 */
public class LCHColorChooser extends AbstractColorChooserPanel implements ChangeListener {
  JSpinner lSpinner;
  JSpinner cSpinner;
  JSpinner hSpinner;
  boolean iterativelyUpdate;

  public LCHColorChooser(Color oldColor) {
    LCHColor lch = new LCHColor(oldColor);
    lSpinner = new JSpinner(new SpinnerNumberModel(lch.l, 0, 100, 1));
    cSpinner = new JSpinner(new SpinnerNumberModel(lch.c, 0, 100, 1));
    hSpinner = new JSpinner(new SpinnerNumberModel(lch.h, -Math.PI, Math.PI, 0.01));

    lSpinner.addChangeListener(this);
    cSpinner.addChangeListener(this);
    hSpinner.addChangeListener(this);
    iterativelyUpdate = true;
  }

  @Override
  public void updateChooser() {
    iterativelyUpdate = false;
    LCHColor newLCH = new LCHColor(getColorFromModel());
    lSpinner.setValue(newLCH.l);
    cSpinner.setValue(newLCH.c);
    hSpinner.setValue(newLCH.h);
    iterativelyUpdate = true;
  }

  @Override
  protected void buildChooser() {
    this.add(lSpinner);
    this.add(cSpinner);
    this.add(hSpinner);
  }

  @Override
  public String getDisplayName() {
    return "LCH";
  }

  @Override
  public Icon getSmallDisplayIcon() {
    return null;
  }

  @Override
  public Icon getLargeDisplayIcon() {
    return null;
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    if (iterativelyUpdate) {
      LCHColor newLCH = new LCHColor(
          (double) lSpinner.getValue(), (double) cSpinner.getValue(), (double) hSpinner.getValue());
      getColorSelectionModel().setSelectedColor(newLCH.toRGB());
    }
  }

  static class LCHPreviewPanel extends JPanel {
    private BufferedImage im;
    private LCHColor lch;

    public LCHPreviewPanel(Color rgb, ColorSelectionModel model) {
      lch = new LCHColor(rgb);
      this.im = LCHPreviewPanel.makePreviewImage(lch);
      this.setPreferredSize(new Dimension(140, 140));
      this.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          super.mouseClicked(e);
          int x = e.getX();
          int y = e.getY();
          int xCart = x - im.getWidth() / 2;
          int yCart = im.getHeight() / 2 - y;
          double c = Math.hypot(xCart, yCart);
          double h = Math.atan2(yCart, xCart);
          LCHColor newColor = new LCHColor(lch.l, c, h);
          model.setSelectedColor(newColor.toRGB());
        }
      });
    }

    private static BufferedImage makePreviewImage(LCHColor lch) {
      BufferedImage im = new BufferedImage(140, 140, BufferedImage.TYPE_INT_ARGB);
      for (int y = 0; y < im.getHeight(); y++) {
        for (int x = 0; x < im.getWidth(); x++) {
          int xCart = x - im.getWidth() / 2;
          int yCart = im.getHeight() / 2 - y;
          double c = Math.hypot(xCart, yCart);
          double h = Math.atan2(yCart, xCart);
          im.setRGB(x, y, new LCHColor(lch.l, c, h).toRGB().getRGB());
        }
      }
      return im;
    }

    public void updateRGB(Color rgb) {
      lch = new LCHColor(rgb);
      im = LCHPreviewPanel.makePreviewImage(lch);
      this.repaint();
    }

    @Override
    public void paint(Graphics g) {
      g.drawImage(this.im, 0, 0, null);
      double xCart = lch.c * Math.cos(lch.h);
      double yCart = lch.c * Math.sin(lch.h);
      int x = (int) Math.round(xCart + im.getWidth() / 2.0);
      int y = (int) Math.round(-yCart + im.getHeight() / 2.0);
      g.drawLine(0, y, im.getWidth() - 1, y);
      g.drawLine(x, 0, x, im.getHeight() - 1);
      g.drawOval(x - 4, y - 4, 7, 7);
    }
  }

  /**
   * A color in the CIE LCH color space, a cylindrical transformation of CIELAB.
   */
  static class LCHColor {
    public double l;
    public double c;
    public double h;

    public LCHColor(double l, double c, double h) {
      this.l = l;
      this.c = c;
      this.h = h;
    }

    public LCHColor(LCHColor lch) {
      this.l = lch.l;
      this.c = lch.c;
      this.h = lch.h;
    }

    public LCHColor(Color rgb) {
      this(new XYZColor(rgb).toLCH());
    }

    public Color toRGB() {
      return new XYZColor(this).toRGB();
    }
  }

  /**
   * A color in the CIE XYZ color space, the master space for CIE.
   */
  static class XYZColor {
    public double x;
    public double y;
    public double z;

    static final double X65 = 95.049;
    static final double Y65 = 100;
    static final double Z65 = 108.8840;
    static final double DELTA = 6 / 29.0;

    /**
     * Creates a CIE XYZ color.
     *
     * @param x the x value
     * @param y the y value
     * @param z the z value
     */
    public XYZColor(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }

    /**
     * Initializes the color to match an existing sRGB color.
     *
     * @param rgb the sRGB color to match
     */
    public XYZColor(Color rgb) {
      double rLin = rgb.getRed() / 255.0;
      double gLin = rgb.getGreen() / 255.0;
      double bLin = rgb.getBlue() / 255.0;

      Function<Double, Double> invGamma = u -> {
        if (u <= 0.04045) {
          return u / 12.92;
        } else {
          return Math.pow((u + 0.055) / 1.055, 2.4);
        }
      };

      double r = invGamma.apply(rLin);
      double g = invGamma.apply(gLin);
      double b = invGamma.apply(bLin);

      // essentially an inverse matrix multiplication
      x = 0.4123 * r + 0.3576 * g + 0.1805 * b;
      y = 0.2126 * r + 0.7152 * g + 0.0722 * b;
      z = 0.0193 * r + 0.1192 * g + 0.9505 * b;

      // use white point and 100 base
      x *= X65;
      y *= Y65;
      z *= Z65;
    }

    /**
     * Initializes the color to match an existing LCH color.
     *
     * @param lch the CIELCH color to match
     */
    public XYZColor(LCHColor lch) {
      // undo cylindrical conversion
      double a = lch.c * Math.cos(lch.h);
      double b = lch.c * Math.sin(lch.h);

      Function<Double, Double> invF = t -> {
        if (t <= DELTA) {
          return 3 * DELTA * DELTA * (t - DELTA * 2 / 3);
        } else {
          return Math.pow(t, 3);
        }
      };

      x = X65 * invF.apply((lch.l + 16) / 116 + a / 500);
      y = Y65 * invF.apply((lch.l + 16) / 116);
      z = Z65 * invF.apply((lch.l + 16) / 116 - b / 200);
    }

    /**
     * Converts this color to LCH.
     *
     * @return the color in LCH
     */
    public LCHColor toLCH() {
      // https://www.wikiwand.com/en/CIELAB_color_space#/CIELAB%E2%80%93CIEXYZ_conversions
      Function<Double, Double> f = t -> {
        if (t <= Math.pow(DELTA, 3)) {
          return t / (3 * DELTA * DELTA) + DELTA * 2 / 3;
        } else {
          return Math.cbrt(t);
        }
      };

      // converting with illuminant d65
      double xRel = x / X65;
      double yRel = y / Y65;
      double zRel = z / Z65;

      double l = 116 * f.apply(yRel) - 16;
      double a = 500 * (f.apply(xRel) - f.apply(yRel));
      double b = 200 * (f.apply(yRel) - f.apply(zRel));

      // convert to cylindrical
      double c = Math.hypot(a, b);
      double h = Math.atan2(b, a);
      return new LCHColor(l, c, h);
    }

    /**
     * Converts this color to RGB.
     *
     * @return a color in RGB
     */
    public Color toRGB() {

      // https://www.wikiwand.com/en/SRGB#/Specification_of_the_transformation

      double x65 = x / 100;
      double y65 = y / 100;
      double z65 = z / 100;

      // this is basically a matrix multiplication
      double rLin = +3.241 * x65 - 1.537 * y65 - 0.499 * z65;
      double gLin = -0.969 * x65 + 1.876 * y65 + 0.042 * z65;
      double bLin = +0.056 * x65 - 0.204 * y65 + 1.057 * z65;

      Function<Double, Double> gammaCorrect = u -> {
        if (u <= 0.0031308) {
          return 12.92 * u;
        } else {
          return 1.055 * Math.pow(u, 1 / 2.4) - 0.055;
        }
      };

      Function<Double, Float> clip = u -> {
        if (u < 0) {
          return 0.0f;
        } else if (u > 1) {
          return 1.0f;
        } else {
          return (float) (double) u;
        }
      };

      return new Color(
          clip.apply(gammaCorrect.apply(rLin)),
          clip.apply(gammaCorrect.apply(gLin)),
          clip.apply(gammaCorrect.apply(bLin)));
    }
  }
}
