package org.pennyledger.util;

import java.io.Serializable;

public interface Hash extends Comparable<Hash>, Serializable {
  
  @Override
  public String toString();
  
}
