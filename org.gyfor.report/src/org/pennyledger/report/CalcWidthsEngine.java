package org.pennyledger.report;

public class CalcWidthsEngine implements Engine {

  @Override
  public void processHeader(IReportGrouping<?> level) {
    IReportBlock block = level.getLogicalHeader();
    block.calcWidths();

    block = level.getPhysicalHeader();
    if (block != null) {
      block.calcWidths();
    }
    block = level.getPhysicalFooter();
    if (block != null) {
      block.calcWidths();
    }
    block = level.getFirstFooter();
    if (block != null) {
      block.calcWidths();
    }
  }

  
  @Override
  public void processDetail(IReportDetail detail) {
    IReportBlock block = detail.getDetail();
    block.calcWidths();
  }

  
  @Override
  public void processFooter(IReportGrouping<?> level) {
    IReportBlock block = level.getLogicalFooter();
    if (block != null) {
      block.calcWidths();
    }
  }

}
