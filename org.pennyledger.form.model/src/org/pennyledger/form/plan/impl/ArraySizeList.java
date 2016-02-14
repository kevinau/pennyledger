package org.pennyledger.form.plan.impl;

import java.lang.reflect.Field;

import org.pennyledger.form.Occurs;

public class ArraySizeList {

  private final int[] sizes;
  private int index;
  
  
  public ArraySizeList (Field field) {
    Occurs occursAnn = field.getAnnotation(Occurs.class);
    if (occursAnn != null) {
      this.sizes = occursAnn.value();
    } else {
      this.sizes = new int[0];
    }
    this.index = 0;
  }

  
  public ArraySizeList (int sizes) {
    this.sizes = new int[] {
        sizes,
    };
    this.index = 0;
  }
  
  
  public ArraySizeList () {
    this.sizes = new int[0];
    this.index = 0;
  }
  
  public int nextSize () {
    if (index == sizes.length) {
      return Integer.MAX_VALUE;
    }
    int size = sizes[index++];
    if (size == -1) {
      return Integer.MAX_VALUE;
    } else {
      return size;
    }
  }
}
