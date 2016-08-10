package org.pennyledger.report;

public interface Engine {

  public void processHeader(IReportGrouping<?> level);

  public void processDetail(IReportDetail detail);

  public void processFooter(IReportGrouping<?> level);

}