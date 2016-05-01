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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.gyfor.report.PaperSize;
import org.gyfor.report.page.IPageWriter;


public class PDFPageWriter implements IPageWriter {
  private PDFDocument document;
  private PDFPage page;
  
  private final PaperSize paperSize;
  private final int pageWidth;
  private final int pageHeight;

 
  public PDFPageWriter(String outputPathName) {
    this(Paths.get(outputPathName), PaperSize.A4);
  }
  
  
  public PDFPageWriter(String outputPathName, PaperSize paperSize) {
    this(Paths.get(outputPathName), paperSize);
  }
  
  
  public PDFPageWriter(Path outputPath, PaperSize paperSize) {
    this.paperSize = paperSize;
    pageWidth = paperSize.getMilliWidth();
    pageHeight = paperSize.getMilliHeight();
    try {
      document = new PDFDocument(outputPath);
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
  public int getPageWidth() {
    return pageWidth;
  }

  
  @Override
  public int getPageHeight() {
    return pageHeight;
  }
}
