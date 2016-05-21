package org.gyfor.report;

public interface IReportGrouping<T> extends IReportLevel {

  public Object getGroup(T source);
  
  public IReportBlock getLogicalHeader();
  
  public default IReportBlock getPhysicalHeader() {
    return getLogicalHeader();
  }
  
  public IReportBlock getLogicalFooter();
  
  public default IReportBlock getPhysicalFooter() {
    return null;
  }
  
  public default IReportBlock getFirstFooter() {
    return null;
  }
  
  @Override
  public default boolean isDetail () {
    return false;
  }
    
  public void setData(T data);
  
  public default void accumulate(T source) {
  }

  public default void reset() {
  }

}
