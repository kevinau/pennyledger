package org.gyfor.test.report;

import org.gyfor.report.IReportBlock;


public class TestReportBlock implements IReportBlock {

  private final TestReportPager pager;
  private final int height;

  
  public TestReportBlock (TestReportPager pager, String heightx) {
    this.pager = pager;
    this.height = Integer.parseInt(heightx);
  }

  
  public TestReportBlock (TestReportPager pager, int height) {
    this.pager = pager;
    this.height = height;
  }

  
  @Override
  public int getHeight () {
    return height;
  }

  
  @Override
  public void emit (int offset) {
    pager.emit(this);
  }

  
  @Override
  public boolean isMandatory() {
    return false;
  }
}
