package org.gyfor.test.report;

import java.io.IOException;

import org.gyfor.report.IReportBlock;
import org.gyfor.report.PDFReportBlock;
import org.gyfor.report.PDFReportPager;
import org.gyfor.report.PaperSize;
import org.gyfor.report.ReportEngine;
import org.junit.Test;

public class PDFReportTest {

  @Test
  public void test() throws IOException {
    PDFReportPager pager = new PDFReportPager("test.pdf", PaperSize.A4);

    ReportEngine reportEngine = new ReportEngine(pager);
    IReportBlock detail = new PDFReportBlock(pager);
    reportEngine.printDetail(detail);
    reportEngine.close();
  }
    
}
