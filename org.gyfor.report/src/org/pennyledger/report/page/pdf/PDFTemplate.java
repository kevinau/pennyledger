package org.pennyledger.report.page.pdf;

import org.pennyledger.report.page.IPageTemplate;

public class PDFTemplate extends PDFContentReference implements IPageTemplate {
  
  private final int index;
  //private final int boxWidth;
  //private final int boxHeight;
  
  
  public PDFTemplate (PDFDocument document, int index) {
    super (document, "XObject");
    this.index = index;
    //this.boxWidth = x1 - x0;
    //this.boxHeight = y1 - y0;
    put ("Subtype", new PDFName("Form"));
  }

  
  @Override
  public PDFContent createContent (int x0, int y0, int x1, int y1) {
    PDFDecimal[] bx = new PDFDecimal[] {
        new PDFDecimal(x0 / 1000.0),
        new PDFDecimal(y0 / 1000.0),
        new PDFDecimal(x1 / 1000.0),
        new PDFDecimal(y1 / 1000.0),
    };
    PDFArray bbox = new PDFArray(bx);
    put ("BBox", bbox);

    int boxWidth = x1 - x0;
    int boxHeight = y1 - y0;
    return createContent(boxWidth, boxHeight);
  }
  
  
  //public int getWidth () {
  //  return boxWidth;
  //}


  //@Override
  //public int getHeight () {
  //  return boxHeight;
  //}


  public PDFName getId () {
    return new PDFName("Xf" + index);
  }
  
}
