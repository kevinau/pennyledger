package org.gyfor.report;

public interface IReportLevel {

  public boolean isGrouping();
  
  public default void setData(Object source) {
  }
  
}
