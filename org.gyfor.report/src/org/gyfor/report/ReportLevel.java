package org.gyfor.report;

public abstract class ReportLevel implements IReportLevel {

  private IReportBlock separator = null;

  @Override
  public IReportBlock getSeparator() {
    return separator;
  }

  
  protected void setSeparator (IReportBlock separator) {
    if (this.separator != null) {
      throw new IllegalStateException("Separator has already been set");
    }
    this.separator = separator;
  }
 
}
