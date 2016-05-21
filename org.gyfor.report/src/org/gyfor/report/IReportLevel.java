package org.gyfor.report;

public interface IReportLevel {

  public boolean isDetail();
  
  public void finalizeWidths();
  
  public default IReportBlock getSeparator() {
    return null;
  };
  
}
