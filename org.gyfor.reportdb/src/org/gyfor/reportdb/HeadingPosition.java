package org.gyfor.reportdb;

public enum HeadingPosition {
  
  /**
   * The heading appears at the top of the page, before the column headings.  Headings in this position
   * will case a page break.
   */
  PAGE,
  
  /** 
   * The heading appears at the side of the page, after the column headings.  Multiple headings of this
   * type are emitted down the page, between other side headings and detail lines.
   */
  BODY,
  
  /**
   * The heading appears as a column of the report.  Multiple headings of this type appear as blank columns. 
   */
  LINE;
  
}
