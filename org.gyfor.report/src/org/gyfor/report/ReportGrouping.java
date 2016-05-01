package org.gyfor.report;

import java.util.function.Function;

public class ReportGrouping<T> implements IReportGrouping<T> {

  private final Function<T,Object> grouper;
  
  private final IReportBlock logicalHeader;
  private final IReportBlock logicalFooter;
  
  private final IReportBlock physicalHeader;
  private final IReportBlock physicalFooter;
  private final IReportBlock firstFooter;
  
  public ReportGrouping (Function<T,Object> grouper, IReportBlock logicalHeader) {
    this.grouper = grouper;
    this.logicalHeader = logicalHeader;
    this.logicalFooter = null;
    this.physicalHeader = logicalHeader;
    this.physicalFooter = null;
    this.firstFooter = null;
  }
  
  public ReportGrouping (Function<T,Object> grouper, IReportBlock logicalHeader, IReportBlock logicalFooter) {
    this.grouper = grouper;
    this.logicalHeader = logicalHeader;
    this.logicalFooter = logicalFooter;
    this.physicalHeader = logicalHeader;
    this.physicalFooter = null;
    this.firstFooter = null;
  }
  
  public ReportGrouping (Function<T,Object> grouper, IReportBlock logicalHeader, IReportBlock logicalFooter, IReportBlock physicalHeader, IReportBlock physicalFooter) {
    this.grouper = grouper;
    this.logicalHeader = logicalHeader;
    this.logicalFooter = logicalFooter;
    this.physicalHeader = physicalHeader;
    this.physicalFooter = physicalFooter;
    this.firstFooter = null;
  }
  
  public ReportGrouping (Function<T,Object> grouper, IReportBlock logicalHeader, IReportBlock logicalFooter, IReportBlock physicalHeader, IReportBlock physicalFooter, IReportBlock firstFooter) {
    this.grouper = grouper;
    this.logicalHeader = logicalHeader;
    this.logicalFooter = logicalFooter;
    this.physicalHeader = physicalHeader;
    this.physicalFooter = physicalFooter;
    this.firstFooter = firstFooter;
  }
  
  @Override
  public IReportBlock getLogicalHeader() {
    return logicalHeader;
  }

  @Override
  public IReportBlock getLogicalFooter() {
    return logicalFooter;
  }

  @Override
  public IReportBlock getPhysicalFooter() {
    return physicalFooter;
  }

  @Override
  public IReportBlock getPhysicalHeader() {
    return physicalHeader;
  }

  @Override
  public IReportBlock getFirstFooter() {
    return firstFooter;
  }

  @Override
  public void setData (Object source) {
    logicalHeader.setData(source);
    if (logicalFooter != null) {
      logicalFooter.setData(source);
    }
    if (physicalHeader != null) {
      physicalHeader.setData(source);
    }
    if (physicalFooter != null) {
      physicalFooter.setData(source);
    }
    if (firstFooter != null) {
      firstFooter.setData(source);
    }
  }

  @Override
  public Object getGroup(T source) {
    return grouper.apply(source);
  }

}
