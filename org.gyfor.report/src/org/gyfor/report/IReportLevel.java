package org.gyfor.report;

import org.eclipse.jdt.annotation.NonNull;

public interface IReportLevel {

  public @NonNull IReportBlock getLogicalHeader();
  
  public IReportBlock getPhysicalHeader();
  
  public IReportBlock getLogicalFooter();
  
  public IReportBlock getPhysicalFooter();
  
  public IReportBlock getFirstFooter();
  
}
