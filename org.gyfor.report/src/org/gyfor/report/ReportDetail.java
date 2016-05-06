package org.gyfor.report;

public class ReportDetail extends ReportLevel implements IReportDetail {

  private final IReportBlock detail;
  
  public ReportDetail (IReportBlock detail) {
    this.detail = detail;
  }
  
  @Override
  public IReportBlock getDetail() {
    return detail;
  }

  @Override
  public void setData (Object source) {
    detail.setData(source);
  }

  @Override
  public void finalizeWidths() {
  }
  
  
  public ReportDetail separator (IReportBlock separator) {
    setSeparator(separator);
    return this;
  }
  
  
}
