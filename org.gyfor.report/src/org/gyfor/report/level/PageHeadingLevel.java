package org.gyfor.report.level;

import java.util.function.Supplier;

import org.gyfor.report.IReportBlock;
import org.gyfor.report.IReportGrouping;
import org.gyfor.report.page.BaseFont;
import org.gyfor.report.page.BaseFontFactory;
import org.gyfor.report.page.pdf.PDFContent;
import org.pennyledger.object.plan.IFieldPlan;
import org.pennyledger.object.type.IType;


public class PageHeadingLevel implements IReportGrouping {

  private static final int LEFT_MARGIN = 30 * 720;
  private static final int pageHeadingGrey = 0;
  private static final float pageHeadingFontSize = 9;
  
  private static final BaseFont pageHeadingFont = BaseFontFactory.getFont("Helvetica");
  
  private final IReportBlock groupHeader;
  private final IReportBlock groupFooter;

  
  public PageHeadingLevel(IFieldPlan<?> plan, Supplier<?> supplier, ColumnSet colSet) {
    String label = plan.getDeclaredLabel();
    double width = pageHeadingFont.getAdvance(label, pageHeadingFontSize);
    colSet.add("label", width);
    
    groupHeader = new IReportBlock() {
      @Override
      public int getHeight() {
        // Top margin, then line, then a blank line
        int n = 1;
        return pageHeadingFont.getLineHeight(pageHeadingFontSize) * (n + 1);
      }


      @Override
      public void calcWidth() {
      }
      
      
      @Override
      public void emit(PDFContent canvas, int offset) {
        int baseLineOffset = pageHeadingFont.getAboveBaseLine(pageHeadingFontSize);
        offset += baseLineOffset;
        int lineHeight = pageHeadingFont.getLineHeight(pageHeadingFontSize);

        int labelWidth = (int)(new ColumnSet().getWidth("label") + 0.5);
        Object value = supplier.get();
          
        IType<?> type = plan.getType();
        String valuex = type.toValueString(value);
          
        canvas.beginText();
        canvas.setFontAndSize(pageHeadingFont, pageHeadingFontSize);
        canvas.setNonStrokeGrey(pageHeadingGrey);
        canvas.drawTextRight(offset, plan.getDeclaredLabel(), LEFT_MARGIN + labelWidth);
        canvas.endText();
        
        canvas.beginText();
        canvas.setFontAndSize(pageHeadingFont, pageHeadingFontSize);
        canvas.setNonStrokeGrey(pageHeadingGrey);
        canvas.drawText(LEFT_MARGIN + labelWidth, offset, valuex);
        canvas.endText();
          
        offset += lineHeight;
      }
    };

    groupFooter = null;
  }
  
  
  public PageHeadingLevel(IFieldPlan<?>[] plans, Supplier<?>[] suppliers, ColumnSet colSet) {
    for (IFieldPlan<?> plan : plans) {
      String label = plan.getDeclaredLabel();
      double width = pageHeadingFont.getAdvance(label, pageHeadingFontSize);
      colSet.add("label", width);
    }
    
    groupHeader = new IReportBlock() {
      @Override
      public int getHeight() {
        // Top margin, then line, then a blank line
        int n = plans.length;
        return pageHeadingFont.getLineHeight(pageHeadingFontSize) * (n + 1);
      }


      @Override
      public void calcWidth() {
      }
      
      
      @Override
      public void emit(PDFContent canvas, int offset) {
        int baseLineOffset = pageHeadingFont.getAboveBaseLine(pageHeadingFontSize);
        offset += baseLineOffset;
        int lineHeight = pageHeadingFont.getLineHeight(pageHeadingFontSize);

        for (int i = 0; i < plans.length; i++) {
          int labelWidth = (int)(new ColumnSet().getWidth("label") + 0.5);

          Object value = suppliers[i].get();
          
          IFieldPlan<?> plan = plans[i];
          IType<?> type = plan.getType();
          String valuex = type.toValueString(value);
          
          canvas.beginText();
          canvas.setFontAndSize(pageHeadingFont, pageHeadingFontSize);
          canvas.setNonStrokeGrey(pageHeadingGrey);
          canvas.drawTextRight(offset, plan.getDeclaredLabel(), LEFT_MARGIN + labelWidth);
          canvas.drawText(LEFT_MARGIN + labelWidth, offset, valuex);
          canvas.endText();
          
          offset += lineHeight;
        }
      }
    };

    groupFooter = new IReportBlock() {
      @Override
      public int getHeight() {
        // Blank line, then line, then bottom margin
        return pageHeadingFont.getLineHeight(pageHeadingFontSize) * 2;
      }


      @Override
      public void emit(PDFContent canvas, int offset) {
        canvas.beginText();
        canvas.setFontAndSize(pageHeadingFont, pageHeadingFontSize);
        canvas.setNonStrokeGrey(pageHeadingGrey);
        int lineHeight = pageHeadingFont.getLineHeight(pageHeadingFontSize);
        int baseLineOffset = pageHeadingFont.getAboveBaseLine(pageHeadingFontSize);
        String text = "Page footer";
        canvas.drawText(LEFT_MARGIN, offset + lineHeight + baseLineOffset, text);
        canvas.endText();
      }


      @Override
      public void calcWidth() {
      }
    };
  }


  @Override
  public IReportBlock getLogicalHeader() {
    return groupHeader;
  }


  @Override
  public IReportBlock getLogicalFooter() {
    return groupFooter;
  }

  
  @Override
  public IReportBlock getPhysicalFooter() {
    return groupFooter;
  }


  @Override
  public IReportBlock getPhysicalHeader() {
    return groupHeader;
  }


  @Override
  public IReportBlock getFirstFooter() {
    return null;
  }


  @Override
  public Object getGroup(Object source) {
    // TODO Auto-generated method stub
    return null;
  }
}
