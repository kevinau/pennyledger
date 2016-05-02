package org.gyfor.report;

public interface IReportGrouping<T> extends IReportLevel {

  public Object getGroup(T source);
  
  public IReportBlock getLogicalHeader();
  
  public IReportBlock getPhysicalHeader();
  
  public IReportBlock getLogicalFooter();
  
  public IReportBlock getPhysicalFooter();
  
  public default IReportBlock getFirstFooter() {
    return null;
  }
  
  @Override
  public default boolean isDetail () {
    return false;
  }
    
  public default void accumulate(T source) {
  }

}
