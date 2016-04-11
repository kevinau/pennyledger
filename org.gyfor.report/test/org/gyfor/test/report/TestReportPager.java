package org.gyfor.test.report;

import java.util.Queue;

import org.gyfor.report.IReportBlock;
import org.gyfor.report.IReportPager;
import org.junit.Assert;

public class TestReportPager implements IReportPager {
  
  public static class Expected {
    final int page;
    final int offset;
    
    public Expected (int page, int offset) {
      this.page = page;
      this.offset = offset;
    }
  }
  
  private final Queue<Expected> expecteds;
  private int page;
  private int offset;
 

  public TestReportPager (Queue<Expected> expecteds) {
    this.expecteds = expecteds;
  }
  
  
  @Override
  public void newPage () {
    page++;
    offset = 0;
  }


  public void incrementOffset (int height) {
    offset += height;
  }

  
  void emit (IReportBlock block) {
    Expected expected = expecteds.poll();
    if (expected.page != -1) {
      Assert.assertEquals("Page number", expected.page, page);
    }
    if (expected.offset != -1) {
      Assert.assertEquals("Vertical offset on page", expected.offset, offset);
    }
    offset += block.getHeight();
  }
  
  
  @Override
  public int getPageHeight() {
    return 100;
  }

  @Override
  public void close() {
    Assert.assertTrue("All expected blocks emitted", expecteds.isEmpty());
  }
}
