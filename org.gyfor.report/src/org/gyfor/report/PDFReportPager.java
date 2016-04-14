package org.gyfor.report;

import java.io.File;
import java.io.OutputStream;

import org.gyfor.report.page.pdf.PDFContent;
import org.gyfor.report.page.pdf.PDFPageWriter;

public class PDFReportPager implements IReportPager, AutoCloseable {

  private final PDFPageWriter pdfWriter;
  private final PaperSize paperSize;

  private PDFContent pdfContent;
  
  
  public PDFReportPager (String fileName, PaperSize paperSize) {
    pdfWriter = new PDFPageWriter(fileName, paperSize);
    this.paperSize = paperSize;
    newPage();
  }
  
  
  public PDFReportPager (File file, PaperSize paperSize) {
    pdfWriter = new PDFPageWriter(file, paperSize);
    this.paperSize = paperSize;
    newPage();
  }
  
  
  public PDFReportPager (OutputStream os, PaperSize paperSize) {
    pdfWriter = new PDFPageWriter(os, paperSize);
    this.paperSize = paperSize;
    newPage();
  }
  
  
  @Override
  public int getPageHeight() {
    return paperSize.getMilliHeight();
  }

  
  @Override
  public void newPage() {
    pdfWriter.newPage();
    pdfContent = pdfWriter.getPageContent();
  }

  
  @Override
  public void close() {
    pdfWriter.close();
  }

  
  @Override
  public PDFContent getContent() {
    return pdfContent;
  }
  
}
