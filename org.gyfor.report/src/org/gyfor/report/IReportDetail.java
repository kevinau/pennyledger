package org.gyfor.report;

public interface IReportDetail extends IReportLevel {

  public IReportBlock getDetail();
  
  public void setData(Object source);
  
  @Override
  public default boolean isDetail () {
    return true;
  }
    
}
