package org.pennyledger.web.html;


public class HtmlTableRow extends HtmlElement {

  public HtmlTableRow (HtmlTable parentNode) {
    super (parentNode, "tr");
  }
  
  
  public HtmlTableHeader newHeader () {
    return new HtmlTableHeader (this);
  }
  

  public HtmlTableHeader newHeader (String header) {
    HtmlTableHeader th = new HtmlTableHeader (this);
    th.addText(header);
    return th;
  }
  

  public HtmlTableData newData () {
    return new HtmlTableData (this);
  }

}
