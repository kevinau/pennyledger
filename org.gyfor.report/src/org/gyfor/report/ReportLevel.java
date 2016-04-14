package org.gyfor.report;

import org.eclipse.jdt.annotation.NonNull;

public class ReportLevel implements IReportLevel {

  private final @NonNull IReportBlock logicalHeader;
  private final IReportBlock logicalFooter;
  
  private final IReportBlock physicalHeader;
  private final IReportBlock physicalFooter;
  private final IReportBlock firstFooter;
  
  public ReportLevel (@NonNull IReportBlock logicalHeader) {
    this.logicalHeader = logicalHeader;
    this.logicalFooter = null;
    this.physicalHeader = logicalHeader;
    this.physicalFooter = null;
    this.firstFooter = null;
  }
  
  public ReportLevel (@NonNull IReportBlock logicalHeader, IReportBlock logicalFooter) {
    this.logicalHeader = logicalHeader;
    this.logicalFooter = logicalFooter;
    this.physicalHeader = logicalHeader;
    this.physicalFooter = null;
    this.firstFooter = null;
  }
  
  public ReportLevel (@NonNull IReportBlock logicalHeader, IReportBlock logicalFooter, IReportBlock physicalHeader, IReportBlock physicalFooter) {
    this.logicalHeader = logicalHeader;
    this.logicalFooter = logicalFooter;
    this.physicalHeader = physicalHeader;
    this.physicalFooter = physicalFooter;
    this.firstFooter = null;
  }
  
  public ReportLevel (@NonNull IReportBlock logicalHeader, IReportBlock logicalFooter, IReportBlock physicalHeader, IReportBlock physicalFooter, IReportBlock firstFooter) {
    this.logicalHeader = logicalHeader;
    this.logicalFooter = logicalFooter;
    this.physicalHeader = physicalHeader;
    this.physicalFooter = physicalFooter;
    this.firstFooter = firstFooter;
  }
  
  @Override
  public @NonNull IReportBlock getLogicalHeader() {
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

}
