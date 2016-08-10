package org.pennyledger.reportdb;

public class ColumnWidth {
  
  private int startPosn = 0;
  private double maxHeading = 0;
  private int k = 0;
  private double Ak = 0;
  private double Qk = 0;
  private double max1 = 0;
  private double max2 = 0;
  private double sum = 0;
  
  
  public void noteHeadingWidth(double x) {
    maxHeading = Math.max(maxHeading, x);
  }
  
  
  public void noteDataWidth(double x1, double x2) {
    k++;
    double Ak1 = Ak;
    double x = x1 + x2;
    Ak += (x - Ak1) / k;
    Qk += (x - Ak1) * (x - Ak);
    max1 = Math.max(max1, x1);
    max2 = Math.max(max2, x2);
    sum += x1 + x2;
  }
  
  
  public double getMax1() {
    return max1;
  }
  
  
  public double getMax2() {
    return max2;
  }
  
  
  public double stdDev() {
    return Math.sqrt(Qk / k);
  }
  

  public double getAveData() {
    if (k > 0) {
      return sum / k;
    } else {
      return 0;
    }
  }
  

  public double getWidth() {
    double m = max1 + max2;
    if (maxHeading > m) {
      return maxHeading;
    } else {
      return m;
    }
  }
  
  
  public double getMaxHeading() {
    return maxHeading;
  }

  
  public double getMaxData() {
    return max1 + max2;
  }

  
  public double getDataLead() {
    double w = getWidth();
    return (w - (max1 + max2)) / 2;
  }

  
  public void setStartPosition (int startPosn) {
    this.startPosn = startPosn;
  }
  
  
  public int getStartPosition() {
    return startPosn;
  }
  
}
