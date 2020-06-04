package com.company.model.shape;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * A class to test {@link PosnCart}, specifically its constructor, equality/hash code, and
 * interpolation.
 */
public class PosnCartTest {

  /**
   * Check that referential equality isn't being used.
   */
  @Test
  public void equals_notReferential() {
    Posn posnCart1 = new PosnCart(3, 5);
    Posn posnCart2 = new PosnCart(3, 5);
    Posn posnCart3 = new PosnCart(5, 3);
    assertEquals(posnCart1, posnCart2);
    assertEquals(posnCart1, posnCart1);
    assertNotEquals(posnCart1, posnCart3);
  }

  /**
   * Check that referential equality isn't being used for hashes.
   */
  @Test
  public void hashCode_notReferential() {
    Posn posnCart1 = new PosnCart(3, 5);
    Posn posnCart2 = new PosnCart(3, 5);
    Posn posnCart3 = new PosnCart(15, 20);
    assertEquals(posnCart1.hashCode(), posnCart2.hashCode());
    assertEquals(posnCart1.hashCode(), posnCart1.hashCode());
    assertNotEquals(posnCart1.hashCode(), posnCart3.hashCode());
  }

  @Test
  public void interpolate_happyPath() {
    PosnCart posn1 = new PosnCart(1, 10);
    PosnCart posn2 = new PosnCart(5, 15);
    PosnCart posn3 = new PosnCart(-7, 7.5);
    assertEquals(new PosnCart(3, 12.5), posn1.interpolate(posn2, 0.5));
    assertEquals(posn1, posn2.interpolate(posn1, 1));
    assertEquals(posn3, posn3.interpolate(posn3, 1));
    assertEquals(new PosnCart(-3, 10), posn3.interpolate(posn2, 1 / 3.));
    assertEquals(posn1, posn1.interpolate(posn1, 0.543));
  }

  @Test(expected = IllegalArgumentException.class)
  public void interpolate_tooSmallProgress() {
    new PosnCart(1, 2).interpolate(new PosnCart(2, 3), -0.1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void interpolate_tooBigProgress() {
    new PosnCart(1, 2).interpolate(new PosnCart(2, 3), 1.001);
  }
}