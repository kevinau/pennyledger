package org.pennyledger.report;

public interface IReportColumnHeadings extends IReportLevel {

  @Override
  public default boolean isDetail() {
    return false;
  }
  
}
