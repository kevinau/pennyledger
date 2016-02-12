package org.pennyledger.web.html;

public class HtmlText extends HtmlNode {

  private final String text;
  private final boolean literal;
  
  public HtmlText (String text) {
    this.text = text;
    this.literal = false;
  }

  
  HtmlText (String text, boolean literal) {
    this.text = text;
    this.literal = literal;
  }

  
  public HtmlText () {
    this.text = "&nbsp;";
    this.literal = true;
  }

  
  @Override
  public void buildString(StringBuilder builder) {
    if (literal == false && text.contains("<>&")) {
      for (char c : text.toCharArray()) {
        switch (c) {
        case '<' :
          builder.append("&lt;");
          break;
        case '>' :
          builder.append("&gt;");
          break;
        case '&' :
          builder.append("&amp;");
          break;
        default :
          builder.append(c);
        }
      }
    } else {
      builder.append(text);
    }
  }
  
  
  @Override
  public void buildString(StringBuilder builder, int level, boolean pack) {
    buildString(builder);
  }
  
}
