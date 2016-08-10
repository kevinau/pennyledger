package org.pennyledger.report;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.pennyledger.report.page.pdf.PDFContent;
import org.pennyledger.report.page.pdf.PDFPageWriter;

public class PDFReportPager implements IReportPager, AutoCloseable {

  private final Path pdfPath;
  private final PDFPageWriter pdfWriter;
  private final PaperSize paperSize;

  private PDFContent pdfContent;
  
  
  public PDFReportPager (PaperSize paperSize) {
    try {
      pdfPath = Files.createTempFile(null, ".pdf");
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    pdfWriter = new PDFPageWriter(pdfPath, paperSize);
    this.paperSize = paperSize;
    newPage();
  }
  
  
  public PDFReportPager (String pathName, PaperSize paperSize) {
    pdfPath = Paths.get(pathName);
    pdfWriter = new PDFPageWriter(pdfPath, paperSize);
    this.paperSize = paperSize;
    newPage();
  }
  
  
  public PDFReportPager (Path pdfPath, PaperSize paperSize) {
    this.pdfPath = pdfPath;
    this.pdfWriter = new PDFPageWriter(pdfPath, paperSize);
    this.paperSize = paperSize;
    newPage();
  }
  
  
  public PDFReportPager (OutputStream os, PaperSize paperSize) {
    pdfPath = null;
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
  public void close(Path newPdfPath) {
    pdfWriter.close();
    if (pdfPath == null) {
      throw new IllegalArgumentException("Cannot rename an output file created as OutputStream");
    }
    try {
      Files.move(pdfPath, newPdfPath, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  
  @Override
  public PDFContent getContent() {
    return pdfContent;
  }
  
}
