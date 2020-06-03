package com.company.model.shape;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * A class to test {@link PosnCart}, specifically its constructor, equality, and hash code.
 */
public class PosnCartTest {

  /**
   * Check that referential equality isn't being used.
   */
  @Test
  public void equals_notReferential() {
    PosnCart posnCart1 = new PosnCart(3, 5);
    PosnCart posnCart2 = new PosnCart(3, 5);
    PosnCart posnCart3 = new PosnCart(5, 3);
    assertEquals(posnCart1, posnCart2);
    assertEquals(posnCart1, posnCart1);
    assertNotEquals(posnCart1, posnCart3);
  }
  /**
   * Check that referential equality isn't being used for hashes.
   */
  @Test
  public void hashCode_notReferential() {
    PosnCart posnCart1 = new PosnCart(3, 5);
    PosnCart posnCart2 = new PosnCart(3, 5);
    PosnCart posnCart3 = new PosnCart(15, 20);
    assertEquals(posnCart1.hashCode(), posnCart2.hashCode());
    assertEquals(posnCart1.hashCode(), posnCart1.hashCode());
    assertNotEquals(posnCart1.hashCode(), posnCart3.hashCode());
  }
}