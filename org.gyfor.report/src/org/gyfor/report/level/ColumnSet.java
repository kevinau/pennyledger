package org.gyfor.report.level;

import java.util.HashMap;
import java.util.Map;

public class ColumnSet {

  public static class ColumnWidth {
    private int k = 0;
    private double Ak = 0;
    private double Qk = 0;
    private double max;
    
    public void add(double x) {
      k++;
      double Ak1 = Ak;
      Ak += (x - Ak1) / k;
      Qk += (x - Ak1) * (x - Ak);
      max = Math.max(max, x);
    }
    
    public double getMax() {
      return max;
    }
    
    public double stdDev() {
      return Math.sqrt(Qk / k);
    }
    
  }
  
  private Map<String, ColumnWidth> columns = new HashMap<>();
  
  public void add(String key, double width) {
    ColumnWidth cw = columns.get(key);
    if (cw == null) {
      cw = new ColumnWidth();
      columns.put(key, cw);
    }
    cw.add(width);
  }

  public double getWidth(String key) {
    return columns.get(key).getMax();
  }
  
}
