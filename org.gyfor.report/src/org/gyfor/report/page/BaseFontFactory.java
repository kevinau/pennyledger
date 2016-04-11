package org.gyfor.report.page;

import java.util.ArrayList;
import java.util.List;

public class BaseFontFactory {

  private static final String[] afms = {
    "Courier-Bold.afm",
    "Courier-BoldOblique.afm",
    "Courier-Oblique.afm",
    "Courier.afm",
    "Helvetica-Bold.afm",
    "Helvetica-BoldOblique.afm",
    "Helvetica-Oblique.afm",
    "Helvetica.afm",
    "Times-Bold.afm",
    "Times-BoldItalic.afm",
    "Times-Italic.afm",
    "ZapfDingbats.afm",
    "Symbol.afm",
  };
  
  
  private static List<BaseFont> fonts;
  
  
  private static boolean comparableStyles (int requestedStyle, int fontStyle) {
    if (requestedStyle == fontStyle) {
      return true;
    } else if (requestedStyle != 0 && fontStyle != 0) {
      return true;
    } else {
      return false;
    }
  }
  
  
  public static BaseFont getFont(String fontFamily) {
    return getFont(fontFamily, BaseFont.MEDIUM, BaseFont.UPRIGHT);
  }
  
  
  public static BaseFont getFont(String fontFamily, int weight) {
    return getFont(fontFamily, weight, BaseFont.UPRIGHT);
  }
  
  
  public static BaseFont getFont(String fontFamily, int weight, int style) {
    if (fonts == null) {
      fonts = new ArrayList<BaseFont>();
      for (String afm : afms) {
        fonts.add(new BaseFont(afm));
      }
    }
    for (BaseFont font : fonts) {
      if (font.getFamilyName().equals(fontFamily) && comparableStyles(font.getStyle(), style)  
          && font.getWeight() == weight) {
        return font;
      }
    }
    return fonts.get(3);
  }
}
