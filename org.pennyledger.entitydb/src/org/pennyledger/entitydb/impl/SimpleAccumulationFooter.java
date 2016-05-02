package org.pennyledger.entitydb.impl;

import java.util.ArrayList;
import java.util.List;

import org.gyfor.report.IReportBlock;
import org.gyfor.report.page.pdf.PDFContent;

public class SimpleAccumulationFooter implements IReportBlock {

  private List<IAccumulation> accums = new ArrayList<>();
  
  @Override
  public int getHeight() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void emit(PDFContent canvas, int offset) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void calcWidths() {
  }
  
  @Override
  public void setData (Object data) {
    for (IAccumulation accum : accums) {
      accum.accumulate(data);
    }
  }

}
