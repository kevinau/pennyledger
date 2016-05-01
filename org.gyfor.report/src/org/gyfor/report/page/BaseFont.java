package org.gyfor.report.page;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.Map;

import org.gyfor.report.page.pdf.PDFBoolean;
import org.gyfor.report.page.pdf.PDFDecimal;
import org.gyfor.report.page.pdf.PDFName;

public class BaseFont {

  public static final int UNDERLINE = 1;
  public static final int STRIKETHRU = 2;
  
  public static final int MEDIUM = 400;
  public static final int BOLD = 700;
  
  public static final int UPRIGHT = 0;
  public static final int ITALIC = 2;
  
  private String fontFamily;
  private String fontName;
  private int fontWeight = MEDIUM;
  private int fontStyle;
  private int ascender;
  private int descender;
  private int emWidth;
  private short widths[] = new short[256];
  private Map<String, Object> descriptors = new LinkedHashMap<String, Object>();
  
  
  BaseFont (String afmName) {
    this (afmName, new FontEncoding());
  }
  
  
  BaseFont (String afmName, FontEncoding encoding) {
    try {
      boolean isDescriptor = true;
      
      InputStream is = getClass().getResourceAsStream(afmName);
      if (is == null) {
        throw new FileNotFoundException(".afm file: " + afmName);
      }
      Reader afmReader = new InputStreamReader(is);
      BufferedReader reader = new BufferedReader(afmReader);
      String line = reader.readLine();
      while (line != null) {
        String[] args = line.split(" +");
        String key = args[0];
        if (key.equals("FamilyName")) {
          fontFamily = args[1];
        } else if (key.equals("FontName")) {
          fontName = args[1];
        } else if (key.equals("Weight")) {
          if (args[1].equals("Bold")) {
            fontWeight = BOLD;
          } else {
            fontWeight = MEDIUM;
          }
        } else if (key.equals("ItalicAngle")) {
          double angle = Double.parseDouble(args[1]);
          if (angle == 0) {
            fontStyle = UPRIGHT;
          } else {
            fontStyle = ITALIC;
          }
        } else if (key.equals("Ascender")) {
          ascender = Integer.parseInt(args[1]);
        } else if (key.equals("Descender")) {
          // Note descender is positive
          descender = -Integer.parseInt(args[1]);
        } else if (key.equals("StartCharMetrics")) {
          int n = Integer.parseInt(args[1]);
          for (int i = 0; i < n; i++) {
            line = reader.readLine();
            args = line.split(" +");
            int ch = encoding.getCode(args[7]);
            if (ch >= 0 && ch <= 255) {
              widths[ch] = Short.parseShort(args[4]);
              if (ch == 'M') {
                emWidth = widths[ch];
              }
            }
          }
          isDescriptor = false;
        }
        if (isDescriptor) {
          if (args[1].equals("true") || args[1].equals("false")) {
            descriptors.put(key, new PDFBoolean(args[1]));
          } else {
            char c = args[1].charAt(0);
            if (Character.isLetter(c)) {
              descriptors.put(key, new PDFName(args[1]));
            } else {
              descriptors.put(key, new PDFDecimal(Double.parseDouble(args[1])));
            }
          }
        }
        line = reader.readLine();
      }
      reader.close();
    } catch (FileNotFoundException ex) {
      throw new RuntimeException(ex);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
  
  
  public String getFontName () {
    return fontName;
  }
  
  
  public String getFamilyName () {
    return fontFamily;
  }
  
  
  
  public int getWeight () {
    return fontWeight;
  }
  
  
  public int getStyle () {
    return fontStyle;
  }
  
  
  public int getAscent (float fontSize) {
    return (int)(ascender * fontSize + 0.5);
  }
  
  
  public int getDescent (float fontSize) {
    return (int)(descender * fontSize + 0.5);
  }
  
  
  private static final double LINE_HEIGHT_PERCENT = 1.3;
  
  
  public int getLineHeight (float fontSize) {
    return (int)((ascender + descender) * LINE_HEIGHT_PERCENT * fontSize + 0.5);
  }
  
  
  public int getAboveBaseLine (float fontSize) {
    double topSpace = (LINE_HEIGHT_PERCENT - 1.0) / 2;
    return (int)(((ascender + descender) * topSpace + ascender) * fontSize + 0.5);
  }
  
  
  public int getAdvance (char ch, float fontSize) {
    return (int)(widths[ch] * fontSize + 0.5);
  }
  
  
  public int getAdvance (String s, float fontSize) {
    char[] cx = s.toCharArray();
    int advance = 0;
    for (int i = 0; i < cx.length; i++) {
      advance += widths[cx[i]];
    }
    int totalAdvance = (int)(advance * fontSize + 0.5);
    return totalAdvance;
  }

  
  public int getEmWidth (float fontSize) {
    return (int)(emWidth * fontSize + 0.5);
  }
  
  
  public int getOffset (String s, float fontSize, int width) {
    int advance = getAdvance(s, fontSize);
    return (width - advance) / 2;
  }
  
}
