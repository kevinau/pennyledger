package org.gyfor.report;

public interface IReportDetail extends IReportLevel {

  public IReportBlock getDetail();
  
  @Override
  public default boolean isDetail () {
    return true;
  }
    
}
