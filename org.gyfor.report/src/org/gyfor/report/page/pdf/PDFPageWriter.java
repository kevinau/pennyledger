/*******************************************************************************
 * Copyright (c) 2008 Kevin Holloway (kholloway@geckosoftware.com.au).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.txt
 * 
 * Contributors:
 *     Kevin Holloway - initial API and implementation
 ******************************************************************************/
package org.gyfor.report.page.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import org.gyfor.report.PaperSize;
import org.gyfor.report.page.IPageWriter;


public class PDFPageWriter implements IPageWriter {
  private PDFDocument document;
  private PDFPage page;
  
  private final PaperSize paperSize;
  private final int pageWidth;
  private final int pageHeight;

 
  public PDFPageWriter(String outputFileName) {
    this(new File(outputFileName), PaperSize.A4);
  }
  
  
  public PDFPageWriter(String outputFileName, PaperSize paperSize) {
    this(new File(outputFileName), paperSize);
  }
  
  
  public PDFPageWriter(File outputFile, PaperSize paperSize) {
    this.paperSize = paperSize;
    pageWidth = paperSize.getMilliWidth();
    pageHeight = paperSize.getMilliHeight();
    try {
      document = new PDFDocument(outputFile);
    } catch (FileNotFoundException ex) {
      throw new RuntimeException(ex.getMessage());
    } catch (IOException ex) {
      throw new RuntimeException(ex.getMessage());
    }
  }


  public PDFPageWriter(OutputStream os, PaperSize paperSize) {
    this.paperSize = paperSize;
    pageWidth = paperSize.getMilliWidth();
    pageHeight = paperSize.getMilliHeight();
    document = new PDFDocument(os);
  }


  @Override
  public void close() {
    if (page != null) {
      page.close();
      page = null;
    }
    document.close();
  }
  

  @Override
  public void newPage() {
    if (page != null) {
      page.close();
    }
    page = document.createPage(paperSize);
  }

  
  @Override
  public int getPageNumber() {
    return document.getPageNumber();
  }
  
  //public void writeTemplate (PageCountTemplate templ) {
  //  document.writeTop(templ.getPDFTemplate());
  //}
  
  
  @Override
  public PDFDocument getDocument () {
    return document;
  }
  
  
  @Override
  public PDFContent getPageContent() {
    if (page == null) {
      newPage();
    }
    return page.createContent();
  }


  @Override
  public PDFTemplate createTemplate(int x0, int y0, int x1, int y1) {
    return document.createTemplate(x0, y0, x1, y1);
  }

  
  @Override
  public int getPageWidth() {
    return pageWidth;
  }

  
  @Override
  public int getPageHeight() {
    return pageHeight;
  }
}
