package com.company.view.swing.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.function.Function;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A CIELCH-based color chooser panel.
 */
public class LCHColorChooser extends AbstractColorChooserPanel implements ChangeListener {
  JSlider rSlider;
  JSlider gSlider;
  JSlider bSlider;

  public LCHColorChooser(Color oldColor) {
    rSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, oldColor.getRed());
    gSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, oldColor.getGreen());
    bSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, oldColor.getBlue());


    rSlider.addChangeListener(this);
    gSlider.addChangeListener(this);
    bSlider.addChangeListener(this);
  }

  @Override
  public void updateChooser() {
    Color newColor = getColorFromModel();
    rSlider.setValue(newColor.getRed());
    gSlider.setValue(newColor.getGreen());
    bSlider.setValue(newColor.getBlue());
  }

  @Override
  protected void buildChooser() {
    this.setLayout(new GridLayout(0, 1));
    JLabel rLabel = new JLabel("Red");
    rLabel.setLabelFor(rSlider);
    this.add(rLabel);
    this.add(rSlider);
    JLabel gLabel = new JLabel("Green");
    gLabel.setLabelFor(gSlider);
    this.add(gLabel);
    this.add(gSlider);
    JLabel bLabel = new JLabel("Blue");
    bLabel.setLabelFor(bSlider);
    this.add(bLabel);
    this.add(bSlider);
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
    Color newColor = new Color(rSlider.getValue(), gSlider.getValue(), bSlider.getValue());
    getColorSelectionModel().setSelectedColor(newColor);
  }

  static class LCHPreviewPanel extends JPanel {
    private BufferedImage im;
    private Color rgb;
    private static final int SIZE = 250;
    private static final double SCALE = 1.1;

    public LCHPreviewPanel(Color rgb, ColorSelectionModel model) {
      this.rgb = rgb;
      this.im = LCHPreviewPanel.makePreviewImage(rgb);
      this.setPreferredSize(new Dimension(SIZE, SIZE));
      MouseAdapter mouseAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          super.mouseClicked(e);
          LCHColor lch = LCHPreviewPanel.getLCHAt(e.getX(), e.getY(), new LCHColor(getRGB()).l);
          if (new XYZColor(lch).isInGamut()) {
            model.setSelectedColor(lch.toRGB());
          }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
          super.mouseDragged(e);
          mouseClicked(e);
        }
      };
      this.addMouseListener(mouseAdapter);
      this.addMouseMotionListener(mouseAdapter);
    }

    private Color getRGB() {
      return this.rgb;
    }

    private static LCHColor getLCHAt(int x, int y, double l) {
      double xCart = x - SIZE / 2.0;
      double yCart = SIZE / 2.0 - y;
      xCart /= SCALE;
      yCart /= SCALE;
      double c = Math.hypot(xCart, yCart);
      double h = Math.atan2(yCart, xCart);
      return new LCHColor(l, c, h);
    }

    private static BufferedImage makePreviewImage(Color rgb) {
      LCHColor lch = new LCHColor(rgb);
      BufferedImage im = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
      for (int y = 0; y < im.getHeight(); y++) {
        for (int x = 0; x < im.getWidth(); x++) {
          LCHColor newLCH = LCHPreviewPanel.getLCHAt(x, y, lch.l);
          if (new XYZColor(newLCH).isInGamut()) {
            im.setRGB(x, y, newLCH.toRGB().getRGB());
          } else {
            im.setRGB(x, y, new Color(255, 255, 255).getRGB());
          }
        }
      }
      return im;
    }

    public void updateRGB(Color rgb) {
      this.rgb = rgb;
      im = LCHPreviewPanel.makePreviewImage(rgb);
      this.repaint();
    }

    @Override
    public void paint(Graphics g) {
      g.drawImage(this.im, 0, 0, null);
      LCHColor lch = new LCHColor(rgb);
      double xCart = lch.c * Math.cos(lch.h);
      double yCart = lch.c * Math.sin(lch.h);
      xCart *= SCALE;
      yCart *= SCALE;
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
  public static class LCHColor {
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

    @Override
    public String toString() {
      return String.format("LCH(l = %.02f, c = %.02f, h = %.02f)", l, c, h);
    }
  }

  /**
   * A color in the CIE XYZ color space, the master space for CIE.
   */
  public static class XYZColor {
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
      this.x = x * X65;
      this.y = y * Y65;
      this.z = z * Z65;
    }

    @Override
    public String toString() {
      return String.format("XYZ(x = %.02f, y = %.02f, z = %.02f)", x, y, z);
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
      x = 0.4124564 * r + 0.3575761 * g + 0.1804375 * b;
      y = 0.2126729 * r + 0.7151522 * g + 0.0721750 * b;
      z = 0.0193339 * r + 0.1191920 * g + 0.9503041 * b;

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
          return 3 * DELTA * DELTA * (t - 4 / 29.0);
        } else {
          return Math.pow(t, 3);
        }
      };

      x = X65 * invF.apply((lch.l + 16) / 116 + a / 500);
      y = X65 * invF.apply((lch.l + 16) / 116);
      z = X65 * invF.apply((lch.l + 16) / 116 - b / 200);
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
          return t / (3 * DELTA * DELTA) + 4 / 29.0;
        } else {
          return Math.cbrt(t);
        }
      };

      // converting with illuminant d50
      double xRel = x / X65;
      double yRel = y / X65;
      double zRel = z / X65;

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

      Function<Double, Float> clip = u -> {
        if (u < 0) {
          return 0.0f;
        } else if (u > 1) {
          return 1.0f;
        } else {
          return (float) (double) u;
        }
      };

      double[] rgb = this.toRGBUnclipped();

      return new Color(
          clip.apply(rgb[0]),
          clip.apply(rgb[1]),
          clip.apply(rgb[2]));
    }

    /**
     * Returns the unclipped RGB values for this color.
     *
     * @return the unclipped RGB values as an array
     */
    private double[] toRGBUnclipped() {

      // https://www.wikiwand.com/en/SRGB#/Specification_of_the_transformation

      double x65 = x / X65;
      double y65 = y / Y65;
      double z65 = z / Z65;

      // this is basically a matrix multiplication
      double rLin = +3.2404542 * x65 - 1.5371385 * y65 - 0.4985314 * z65;
      double gLin = -0.9692660 * x65 + 1.8760108 * y65 + 0.0415560 * z65;
      double bLin = +0.0556434 * x65 - 0.2040259 * y65 + 1.0572252 * z65;

      Function<Double, Double> gammaCorrect = u -> {
        if (u <= 0.0031308) {
          return 12.92 * u;
        } else {
          return 1.055 * Math.pow(u, 1 / 2.4) - 0.055;
        }
      };

      return new double[]{
          gammaCorrect.apply(rLin),
          gammaCorrect.apply(gLin),
          gammaCorrect.apply(bLin)};
    }

    /**
     * Returns true if this color is inside the sRGB gamut and false otherwise.
     *
     * @return whether this color is inside the sRGB gamut or not
     */
    public boolean isInGamut() {
      double[] rgb = this.toRGBUnclipped();
      for (double comp: rgb) {
        if (comp < 0 || comp > 1) {
          return false;
        }
      }
      return true;
    }
  }
}
