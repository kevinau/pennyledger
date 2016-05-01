package org.gyfor.report;

public interface IReportGrouping<T> extends IReportLevel {

  public Object getGroup(T source);
  
  public IReportBlock getLogicalHeader();
  
  public IReportBlock getPhysicalHeader();
  
  public IReportBlock getLogicalFooter();
  
  public IReportBlock getPhysicalFooter();
  
  public IReportBlock getFirstFooter();
  
  @Override
  public default boolean isGrouping () {
    return true;
  }
    
  public default void accumulate(T source) {
  }

}
