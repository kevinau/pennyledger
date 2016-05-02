package org.gyfor.report;

public interface IReportColumnHeadings extends IReportLevel {

  @Override
  public default boolean isDetail() {
    return false;
  }
  
}
