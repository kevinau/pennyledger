package org.gyfor.report;

public interface IReportLevel {

  public IReportBlock getLogicalHeader();
  
  public default IReportBlock getPhysicalHeader() {
    return getLogicalHeader();
  };
  
  public IReportBlock getLogicalFooter();
  
  public IReportBlock getPhysicalFooter();
  
  public default IReportBlock getFirstFooter() {
    return null;
  }
  
}
