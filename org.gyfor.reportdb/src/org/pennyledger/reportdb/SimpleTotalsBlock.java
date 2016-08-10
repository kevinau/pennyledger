package org.pennyledger.reportdb;

import org.pennyledger.object.type.IType;
import org.pennyledger.report.IReportBlock;
import org.pennyledger.report.page.FQFont;
import org.pennyledger.report.page.FQLine;
import org.pennyledger.report.page.pdf.PDFContent;


public class SimpleTotalsBlock extends ColumnarDetailBlock implements IReportBlock {

  private static final FQLine totalLine = FQLine.MEDIUM;
  private static final FQFont totalFont = new FQFont("Helvetica", 9);

  private final IAccumulationColumn[] accumCols;
  
  
  public SimpleTotalsBlock (IType<Object>[] types, String[] names, IAccumulationColumn[] accumCols, ColumnSet colSet) {
    super (types, names, colSet);
    this.accumCols = accumCols;
  }

  
  @Override
  public void setData (Object data) {
  }


  @Override
  protected Object getValue(int i) {
    return accumCols[i].getValue();
  }


  @Override
  public int getHeight() {
    return getHeight(totalFont, totalLine);
  }


  @Override
  public void emit(PDFContent canvas, int offset) {
    emit(canvas, offset, totalFont, totalLine);
  }


  @Override
  public void calcWidths() {
    calcWidths(totalFont);
  }

}
