package org.gyfor.reportdb;

import java.lang.reflect.Field;

import org.gyfor.report.IReportBlock;
import org.gyfor.report.page.FQFont;
import org.gyfor.report.page.pdf.PDFContent;
import org.pennyledger.object.type.IType;


public class SimpleDetailBlock extends ColumnarDetailBlock implements IReportBlock {

  private static final FQFont detailFont = new FQFont("Helvetica", 9);

  private final Field[] fields;

  private Object data;
  
  
  public SimpleDetailBlock (IType<Object>[] types, String[] names, Field[] fields, ColumnSet colSet) {
    super (types, names, colSet);
    this.fields = fields;
  }

  
  @Override
  public void setData (Object data) {
    this.data = data;
  }
  
  
  @Override
  protected Object getValue(int i) {
    try {
      return fields[i].get(data);
    } catch (IllegalArgumentException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }


  @Override
  public int getHeight() {
    return getHeight(detailFont, null);
  }


  @Override
  public void emit(PDFContent canvas, int offset) {
    emit(canvas, offset, detailFont, null);
  }


  @Override
  public void calcWidths() {
    calcWidths(detailFont);
  }

}
