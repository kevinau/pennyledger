package org.gyfor.report.level;

import org.eclipse.jdt.annotation.NonNull;
import org.gyfor.report.IReportBlock;
import org.gyfor.report.IReportLevel;
import org.gyfor.report.PDFReportBlock;
import org.gyfor.report.PDFReportPager;
import org.gyfor.report.page.BaseFont;
import org.gyfor.report.page.BaseFontFactory;
import org.gyfor.report.page.pdf.PDFContent;
import org.gyfor.report.page.pdf.PDFName;


public class PageLevel implements IReportLevel {

  private static final int TOP_MARGIN = 30 * 720;
  private static final int LEFT_MARGIN = 30 * 720;
  private static final int pageLevelGrey = 128;
  private static final float pageLevelFontSize = 8;
  
  private static final BaseFont pageLevelFont = BaseFontFactory.getFont("Helvetica");

  private final @NonNull IReportBlock pageHeader;
  private final IReportBlock pageFooter;


  public PageLevel(PDFReportPager pager, String ownerName) {
    pageHeader = new PDFReportBlock(pager) {
      @Override
      public int getHeight() {
        // Top margin, then line, then a blank line
        return TOP_MARGIN + pageLevelFont.getLineHeight(pageLevelFontSize) * 2;
      }


      @Override
      public void emit(int offset) {
        PDFContent pdfContent = getContent();

        pdfContent.beginText();
        pdfContent.setFontAndSize(pageLevelFont, pageLevelFontSize);
        pdfContent.setNonStrokeGrey(pageLevelGrey);
        int baseLineOffset = pageLevelFont.getAboveBaseLine(pageLevelFontSize);
        pdfContent.drawText(LEFT_MARGIN, offset + TOP_MARGIN + baseLineOffset, ownerName);
        pdfContent.endText();
      }


      @Override
      public boolean isMandatory() {
        return true;
      }
    };

    pageFooter = new PDFReportBlock(pager) {
      @Override
      public int getHeight() {
        // Blank line, then line, then bottom margin
        return pageLevelFont.getLineHeight(pageLevelFontSize) * 2 + TOP_MARGIN;
      }


      @Override
      public void emit(int offset) {
        PDFContent pdfContent = getContent();
        PDFName pageCountRef = pdfContent.getPageCountRef(pageLevelFont, pageLevelFontSize);

        int pageNumber = pdfContent.getPageNumber();

        pdfContent.beginText();
        pdfContent.setFontAndSize(pageLevelFont, pageLevelFontSize);
        pdfContent.setNonStrokeGrey(128);
        int lineHeight = pageLevelFont.getLineHeight(pageLevelFontSize);
        int baseLineOffset = pageLevelFont.getAboveBaseLine(pageLevelFontSize);
        String text = "Page " + pageNumber + " of ";
        pdfContent.drawText(LEFT_MARGIN, offset + lineHeight + baseLineOffset, text);
        int n = pageLevelFont.getAdvance(text, pageLevelFontSize);
        pdfContent.drawTemplate(pageCountRef, LEFT_MARGIN + n, offset + lineHeight + baseLineOffset);
        pdfContent.endText();
      }


      @Override
      public boolean isMandatory() {
        return true;
      }
    };
  }


  @Override
  public @NonNull IReportBlock getLogicalHeader() {
    return pageHeader;
  }


  @Override
  public IReportBlock getLogicalFooter() {
    return pageFooter;
  }

  
  @Override
  public IReportBlock getPhysicalFooter() {
    return pageFooter;
  }


  @Override
  public IReportBlock getPhysicalHeader() {
    return pageHeader;
  }


  @Override
  public IReportBlock getFirstFooter() {
    return null;
  }
}
