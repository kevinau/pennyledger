package org.pennyledger.reportdb;

import org.pennyledger.report.page.FQLine;

public class AccumulationStyle {

  private static FQLine THIN = new FQLine(500);
  private static FQLine MEDIUM = new FQLine(1000);
  private static FQLine THICK = new FQLine(2000);
  
  public FQLine getLine(int depth, int level) {
    switch (depth) {
    case 1 :
      return MEDIUM;
    case 2 :
      if (level == 0) {
        return THIN;
      } else {
        return MEDIUM;
      }
    case 3 :
      if (level == 0) {
        return THIN;
      } else if (level == 1) {
        return MEDIUM;
      } else {
        return THICK;
      }
    }
    throw new RuntimeException("Unsupported depth");
  }
  
}
