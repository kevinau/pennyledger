package org.pennyledger.report;

import org.pennyledger.report.page.pdf.PDFContent;

public interface IReportBlock {

  /**
   * Get the height of this block.
   * <p>
   * The height of a block reported here must match exactly the amount of 
   * vertical space occupied by the block when it is emitted to the report pager.
   * <p>
   * The returned value can be in any units, provided it is the same as that
   * returned by getPageHeight of IReportPager.
   * <p>
   * Note that this method is called quite frequently.  The height value should 
   * be cached if it is expensive to calculate.
   */
  public int getHeight ();

  /** 
   * Write the contents of this block to the report pager.
   * <p>
   * The vertical space occupied by the block when written out must match
   * exactly the value returned by getHeight.
   */
  public void emit (PDFContent canvas, int offset);
  

  public void calcWidths ();
  
  
  public default <T> void setData (T record) {
  }
  
  
  /**
   * Is this block always written out, or will it only be written out if required.
   * <ul>
   * <li>Detail, logical footers and first footers are always mandatory.  This method
   * is not applicable for these report block types.</li>
   * <li>Logical headers can be marked as mandatory or not.  A mandatory logical
   * header is always written out regardless of whether there is any nested content.</li>
   * <li>Physical headers and footers are never mandatory.  They will only be written out 
   * if required.  This method is not applicable for these report block types.</li>
   * </ul>
   */
  public default boolean isMandatory () {
    return false;
  }

}
  
