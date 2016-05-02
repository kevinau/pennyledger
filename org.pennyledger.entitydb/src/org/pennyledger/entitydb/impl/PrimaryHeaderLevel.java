package org.pennyledger.entitydb.impl;

import java.util.function.Consumer;
import java.util.function.Function;

import org.gyfor.report.IReportBlock;
import org.gyfor.report.IReportGrouping;
import org.gyfor.report.page.BaseFont;
import org.gyfor.report.page.BaseFontFactory;
import org.gyfor.report.page.pdf.PDFContent;
import org.pennyledger.object.plan.IEntityPlan;
import org.pennyledger.object.plan.IFieldPlan;


public class PrimaryHeaderLevel implements IReportGrouping {

  private IEntityPlan entityPlan;
  
  private static final int LEFT_MARGIN = 30 * 720;
  private static final float groupFontSize = 8;
  
  private static final BaseFont groupFont = BaseFontFactory.getFont("Helvetica");
  
  private final IReportBlock groupHeader;
  private final IReportBlock groupFooter;

  
  public PrimaryHeaderLevel(IFieldPlan<?>[] plans, Function<T,?>[] suppliers) {
    groupHeader = new IReportBlock() {
      private T data;
      
      @Override
      public int getHeight() {
        // Top margin, then line, then a blank line
        int n = plans.length;
        return groupFont.getLineHeight(groupFontSize) * (n + 1);
      }


      @Override
      public void emit(PDFContent canvas, int offset) {
        int baseLineOffset = groupFont.getAboveBaseLine(groupFontSize);
        offset += baseLineOffset;
        int lineHeight = groupFont.getLineHeight(groupFontSize);

        int maxLabelLength = 0;
        for (int i = 0; i < plans.length; i++) {
          String label = plans[i].getDeclaredLabel();
          int n = groupFont.getAdvance(label, groupFontSize);
          maxLabelLength = Math.max(maxLabelLength, n);
        }
        
        for (int i = 0; i < plans.length; i++) {
          canvas.beginText();
          canvas.setFontAndSize(groupFont, groupFontSize);
          canvas.setNonStrokeGrey(128);
          canvas.drawTextRight(offset, plans[i].getDeclaredLabel(), LEFT_MARGIN + maxLabelLength);
          canvas.endText();
          
          canvas.beginText();
          canvas.setFontAndSize(groupFont, groupFontSize);
          canvas.setNonStrokeGrey(0);
          canvas.drawText(LEFT_MARGIN + maxLabelLength, offset, suppliers[i].apply(data).toString());
          canvas.endText();
          offset += lineHeight;
        }
      }


      @Override
      public void setData(T data) {
        this.data = data;
      }
      
      
      @Override
      public boolean isMandatory() {
        return false;
      }


      @Override
      public void calcWidth() {
      }
    };

    groupFooter = new IReportBlock() {
      @Override
      public int getHeight() {
        // Blank line, then line, then bottom margin
        return groupFont.getLineHeight(groupFontSize) * 2;
      }


      @Override
      public void emit(PDFContent canvas, int offset) {
        canvas.beginText();
        canvas.setFontAndSize(groupFont, groupFontSize);
        canvas.setNonStrokeGrey(0);
        int lineHeight = groupFont.getLineHeight(groupFontSize);
        int baseLineOffset = groupFont.getAboveBaseLine(groupFontSize);
        String text = "Group footing";
        canvas.drawText(LEFT_MARGIN, offset + lineHeight + baseLineOffset, text);
        canvas.endText();
      }


      @Override
      public boolean isMandatory() {
        return false;
      }


      @Override
      public void setData(T data) {
      }


      @Override
      public void calcWidth() {
      }
    };
  }


  public <T, P> PrimaryHeaderLevel accumulate (String fieldName) {
    IFieldPlan fieldPlan = entityPlan.getFieldPlan(fieldName);
    
    return this;
  }
  
  
  public PrimaryHeaderLevel count (String fieldName) {
    
    return this;
  }
  
  
  public <T, p> PrimaryHeaderLevel weightedAve (String fieldName, String fieldName2) {
    
    return this;
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
}
