package org.gyfor.report;

public interface IReportLevel {

  public boolean isDetail();
  
  public void setData(Object source);
  
  public void finalizeWidths();
  
}
