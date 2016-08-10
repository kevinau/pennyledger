package org.pennyledger.report;

public interface IReportLevel {

  public boolean isDetail();
  
  public void finalizeWidths();
  
  public default IReportBlock getSeparator() {
    return null;
  };
  
}
