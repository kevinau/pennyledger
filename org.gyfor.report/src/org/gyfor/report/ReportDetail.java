package org.gyfor.report;

public class ReportDetail implements IReportDetail {

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
  
}
