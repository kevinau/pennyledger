package org.gyfor.reportdb;

import org.gyfor.report.IReportBlock;
import org.gyfor.report.IReportGrouping;
import org.gyfor.report.page.BaseFont;
import org.gyfor.report.page.BaseFontFactory;
import org.gyfor.report.page.Greys;
import org.gyfor.report.page.pdf.PDFContent;
import org.gyfor.report.page.pdf.PDFName;


public class TitleLevel<T> implements IReportGrouping<T> {

  private static final int TOP_MARGIN = 30 * 720;
  private static final int LEFT_MARGIN = 30 * 720;

  private static final int ownerFontGrey = Greys.MID;
  private static final float ownerFontSize = 8;
  private static final BaseFont ownerFont = BaseFontFactory.getFont("Helvetica");

  private static final int titleFontGrey = Greys.BLACK;
  private static final float titleFontSize = 14;
  private static final BaseFont titleFont = BaseFontFactory.getFont("Helvetica", BaseFont.BOLD);

  private static final int subTitleFontGrey = Greys.BLACK;
  private static final float subTitleFontSize = 12;
  private static final BaseFont subTitleFont = BaseFontFactory.getFont("Helvetica");

  private final IReportBlock pageHeader;
  private final IReportBlock pageFooter;


  public TitleLevel(String title) {
    this (title, null);
  }
  
  
  public TitleLevel(String title, String subTitle) {
    String ownerName = "PennyLedger";
    pageHeader = new IReportBlock() {
      @Override
      public int getHeight() {
        int height = TOP_MARGIN;
        height += ownerFont.getLineHeight(ownerFontSize) * 2;
        height += titleFont.getLineHeight(titleFontSize);
        if (subTitle != null) {
          height += subTitleFont.getLineHeight(subTitleFontSize);
        }
        return height;
      }


      @Override
      public void emit(PDFContent canvas, int offset) {
        offset += TOP_MARGIN;
        canvas.beginText();
        canvas.setFontAndSize(ownerFont, ownerFontSize);
        canvas.setNonStrokeGrey(ownerFontGrey);
        int baseLineOffset = ownerFont.getAboveBaseLine(ownerFontSize);
        canvas.drawText(LEFT_MARGIN, offset + baseLineOffset, ownerName);
        canvas.endText();

        offset += ownerFont.getLineHeight(ownerFontSize) * 2;
        canvas.beginText();
        canvas.setFontAndSize(titleFont, titleFontSize);
        canvas.setNonStrokeGrey(titleFontGrey);
        baseLineOffset = titleFont.getAboveBaseLine(titleFontSize);
        canvas.drawText(LEFT_MARGIN, offset + baseLineOffset, title);
        canvas.endText();
        
        if (subTitle != null) {
          offset += titleFont.getLineHeight(titleFontSize);
          canvas.beginText();
          canvas.setFontAndSize(subTitleFont, subTitleFontSize);
          canvas.setNonStrokeGrey(subTitleFontGrey);
          baseLineOffset = subTitleFont.getAboveBaseLine(subTitleFontSize);
          canvas.drawText(LEFT_MARGIN, offset + baseLineOffset, subTitle);
          canvas.endText();
        }
      }

      
      @Override
      public void calcWidths() {
      }
    };

    pageFooter = new IReportBlock() {
      @Override
      public int getHeight() {
        // Blank line, then line, then bottom margin
        return ownerFont.getLineHeight(ownerFontSize) * 2 + TOP_MARGIN;
      }


      @Override
      public void emit(PDFContent canvas, int offset) {
        PDFName pageCountRef = canvas.getPageCountRef(ownerFont, ownerFontSize);

        int pageNumber = canvas.getPageNumber();

        canvas.beginText();
        canvas.setFontAndSize(ownerFont, ownerFontSize);
        canvas.setNonStrokeGrey(Greys.MID);
        int lineHeight = ownerFont.getLineHeight(ownerFontSize);
        int baseLineOffset = ownerFont.getAboveBaseLine(ownerFontSize);
        String text = "Page " + pageNumber + " of ";
        canvas.drawText(LEFT_MARGIN, offset + lineHeight + baseLineOffset, text);

        int n = ownerFont.getAdvance(text, ownerFontSize);
        canvas.drawTemplate(pageCountRef, LEFT_MARGIN + n, offset + lineHeight + baseLineOffset);
        canvas.endText();
      }


      @Override
      public void calcWidths() {
      }
    };
  }


  @Override
  public IReportBlock getLogicalHeader() {
    return pageHeader;
  }


  @Override
  public IReportBlock getPhysicalHeader() {
    return pageHeader;
  }


  @Override
  public IReportBlock getPhysicalFooter() {
    return pageFooter;
  }


  @Override
  public IReportBlock getLogicalFooter() {
    return pageFooter;
  }

  
  @Override
  public Object getGroup(Object source) {
    return "";
  }


  @Override
  public void setData(Object source) {
  }


  @Override
  public void finalizeWidths() {
  }

}
