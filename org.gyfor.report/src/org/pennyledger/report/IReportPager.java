package org.pennyledger.report;

import java.nio.file.Path;

import org.pennyledger.report.page.pdf.PDFContent;

public interface IReportPager {

  /**
   * Get the height of the physical page in the output device.  This is used
   * by the report engine to determine where page breaks should occur.
   * <p>
   * The returned value can be in any units, provided it is the same as that
   * returned by getHeight of IReportBlock.
   */
  public int getPageHeight();


  /**
   * Start a new page on the output device.
   */
  public void newPage();


  /**
   * Indicate to the output device that all printing is complete.  A newPage method
   * will not be called immediately before close, so it may be necessary to eject
   * the last page before closing the output device.
   */
  public void close();

  public void close(Path pathName);


  public PDFContent getContent();

}
