package org.pennyledger.entitydb.impl;

import java.util.ArrayList;
import java.util.List;

import org.gyfor.report.page.BaseFont;
import org.gyfor.report.page.BaseFontFactory;

public class ColumnSet {

  private static final float gapFontSize = 9;
  private static final BaseFont gapFont = BaseFontFactory.getFont("Helvetica");
  private static final int gapSize = gapFont.getEmWidth(gapFontSize);

  private List<ColumnWidth> columns = new ArrayList<>();
  
  public void noteHeadingWidth(int i, double width) {
    while (i >= columns.size()) {
      columns.add(new ColumnWidth());
    }
    ColumnWidth cw = columns.get(i);
    cw.noteHeadingWidth(width);
  }

  public void noteDataWidth(int i, double width1, double width2) {
    while (i >= columns.size()) {
      columns.add(new ColumnWidth());
    }
    ColumnWidth cw = columns.get(i);
    cw.noteDataWidth(width1, width2);
  }

  public double getWidth(int i) {
    return columns.get(i).getWidth();
  }
  
  public double getMax1(int i) {
    return columns.get(i).getMax1();
  }
  
  public double getHeadingLead(int i) {
    return columns.get(i).getHeadingLead();
  }
  
  public double getDataLead(int i) {
    return columns.get(i).getDataLead();
  }
  
  public int getGap() {
    return gapSize;
  }
}
