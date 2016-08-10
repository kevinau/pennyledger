package org.pennyledger.reportdb;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.pennyledger.report.page.BaseFont;
import org.pennyledger.report.page.BaseFontFactory;

public class ColumnSet implements Iterable<ColumnWidth> {

  private static final float gapFontSize = 9;
  private static final BaseFont gapFont = BaseFontFactory.getFont("Helvetica");
  private static final int gapSize = gapFont.getEmWidth(gapFontSize);

  private Map<String, ColumnWidth> columns = new LinkedHashMap<>();
  
  public void noteHeadingWidth(String name, double width) {
    ColumnWidth cw = columns.get(name);
    if (cw == null) {
      cw = new ColumnWidth();
      columns.put(name, cw);
    }
    cw.noteHeadingWidth(width);
  }

  public void noteDataWidth(String name, double width1, double width2) {
    ColumnWidth cw = columns.get(name);
    if (cw == null) {
      cw = new ColumnWidth();
      columns.put(name, cw);
    }
    cw.noteDataWidth(width1, width2);
  }

  
  public ColumnWidth get(String name) {
    return columns.get(name);
  }
  
  
  public int getGap() {
    return gapSize;
  }

  
  @Override
  public Iterator<ColumnWidth> iterator() {
    return columns.values().iterator();
  }
}
