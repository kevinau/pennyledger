package org.gyfor.report.page.pdf;

import java.awt.Color;

import org.gyfor.report.page.BaseFont;
import org.gyfor.report.page.IPageContent;
import org.gyfor.report.page.IPageImage;
import org.gyfor.report.page.IPageTemplate;
import org.gyfor.report.page.LineStyle;


/**
 * @author Kevin Holloway
 *
 */
public class PDFContent extends PDFIndirect implements IPageContent {

  private final PDFDocument document;
  private final PDFContentReference contentReference;
  private final int paperWidth;
  private final int paperHeight;
  
  private final ByteArrayBuilder buffer;
  private int lineIndex = 0;
  private BaseFont font;
  private float fontSize;
  
  
  public PDFContent (PDFDocument document, PDFContentReference contentReference, int paperWidth, int paperHeight) {
    super (document);
    this.document = document;
    this.contentReference = contentReference;
    this.paperWidth = paperWidth;
    this.paperHeight = paperHeight;
    buffer = getByteArray();
  }
  
  
  @Override
  public int getPaperWidth () {
    return paperWidth;
  }
  
  
  @Override
  public int getPaperHeight () {
    return paperHeight;
  }
  
  
  private void appendMeasure (int n) {
    if (n == 0) {
      buffer.append('0');
    } else {
      boolean negative = false;
      if (n < 0) {
        negative = true;
        n = -n;
      }
      int n1 = n;
      int j = 0;
      while (n1 % 10 == 0 && j < 3) {
        j++;
        n1 = n1 / 10;
      }
      if (j == 3) {
        j++;
      }
      byte[] nx = new byte[16];
      int i = nx.length;
      while (n > 0 || i > nx.length - 4) {
        if (i == nx.length - 3) {
          nx[--i] = '.';
        }
        nx[--i] = (byte)('0' + n % 10);
        n = n / 10;
      }
      if (negative) {
        nx[--i] = '-';
      }
      buffer.append(nx, i, nx.length - i - j);
    }
  }

  
  @Override
  public void optionalLineBreak () {
    if (buffer.getIndex() > lineIndex) {
      buffer.append('\r');
      buffer.append('\n');
      lineIndex = buffer.getIndex();
    }
  }
  
  
  @Override
  public void setStrokeGrey (int g) {
    buffer.append(g / 255.0F);
    buffer.append(" G ");
  }
  
  
  @Override
  public void setNonStrokeGrey (int g) {
    buffer.append(g / 255.0F);
    buffer.append(" g ");
  }
  
  
  @Override
  public void setStrokeColor (Color color) {
    setStrokeColor(color.getRed(), color.getGreen(), color.getBlue());
  }
  
  
  @Override
  public void setStrokeColor (int hex) {
    setStrokeColor((hex >> 16) & 0xff, (hex >> 8) & 0xFF, hex & 0xFF);
  }
  
  
  public void setStrokeColor (int r, int g, int b) {
    if (r == g && g == b) {
      setStrokeGrey(r);
    } else {
      buffer.append(r / 255.0F);
      buffer.appendSpace();
      buffer.append(g / 255.0F);
      buffer.appendSpace();
      buffer.append(b / 255.0F);
      buffer.append(" RG ");
    }
  }
  
  
  @Override
  public void setNonStrokeColor (Color color) {
    setNonStrokeColor(color.getRed(), color.getGreen(), color.getBlue());
  }
  
  
  @Override
  public void setNonStrokeColor (int hex) {
    setNonStrokeColor((hex >> 16) & 0xff, (hex >> 8) & 0xFF, hex & 0xFF);
  }
  
  
  public void setNonStrokeColor (int r, int g, int b) {
    if (r == g && g == b) {
      setNonStrokeGrey(r);
    } else {
      buffer.append(r / 255.0F);
      buffer.appendSpace();
      buffer.append(g / 255.0F);
      buffer.appendSpace();
      buffer.append(b / 255.0F);
      buffer.append(" rg ");
    }
  }
  
  
  @Override
  public void setLineWidth (int width) {
    appendMeasure(width);
    buffer.append(" w ");
  }
  
  
  @Override
  public void setLineCapStyle (int style) {
    buffer.append(style);
    buffer.append(" J ");
  }
  
  
  @Override
  public void setLineJoinStyle (int style) {
    buffer.append(style);
    buffer.append(" j ");
  }
  
  
  @Override
  public void setLineDashPattern (int phase, int... pattern) {
    buffer.append('[');
    boolean first = false;
    for (int i : pattern) {
      if (first) {
        first = false;
      } else {
        buffer.appendSpace();
      }
      appendMeasure(i);
    }
    buffer.append("] ");
    appendMeasure(phase);
    buffer.append(" d ");
  }
  
  
  public void closeStrokeAndFill () {
    buffer.append("b ");
  }
  
  
  @Override
  public void closeAndFill () {
    buffer.append("h f ");
  }
  
  
  @Override
  public void closeAndStroke () {
    buffer.append("s ");
  }
  
  
  @Override
  public void rectangle (int x0, int y0, int width, int height) {
    appendMeasure(x0);
    buffer.appendSpace();
    appendMeasure(paperHeight - (y0 + height));
    buffer.appendSpace();
    appendMeasure(width);
    buffer.appendSpace();
    appendMeasure(height);
    buffer.append(" re ");
    
    
  }
  
  @Override
  public void setLineStyle (int phase, LineStyle style, int width) {
    setLineWidth(width);
    switch (style) {
    case SOLID :
      // Solid line
      setLineDashPattern(phase);
      break;
    case DOTTED :
      // Dotted line
      int[] dotted = new int[2];
      dotted[0] = width;
      dotted[1] = width * 2;
      setLineDashPattern(phase, dotted);
      break;
    case DASHED :
      // Dashed line
      int[] dashed = new int[2];
      dashed[0] = width * 2;
      dashed[1] = width * 2;
      setLineDashPattern(phase, dashed);
      break;
    case LONG_DASHED :
      // Dashed line with long dashes
      int[] dashed2 = new int[2];
      dashed2[0] = width * 4;
      dashed2[1] = width * 3;
      setLineDashPattern(phase, dashed2);
      break;
    }
  }

  
  public void newLineOffset (int x, int y) {
    appendMeasure(x);
    buffer.appendSpace();
    appendMeasure(y);
    buffer.append(" Td ");
  }
  
  
  @Override
  public void moveTo (int x, int y) {
    appendMeasure(x);
    buffer.appendSpace();
    appendMeasure(paperHeight - y);
    buffer.append(" m ");
  }
  
  
  @Override
  public void lineTo (int x, int y) {
    appendMeasure(x);
    buffer.appendSpace();
    appendMeasure(paperHeight - y);
    buffer.append(" l ");
  }
  
  
  @Override
  public void stroke () {
    buffer.append("S ");
  }
  
  
  public void fill () {
    buffer.append("f ");
  }
  
  
  public void textOffset (int x, int y) {
    buffer.append(" 1 0 0 1 ");
    appendMeasure(x);
    buffer.appendSpace();
    appendMeasure(paperHeight - y);
    buffer.append(" Tm ");
  }
  
  
  public void templateOffset (int x, int y) {
    buffer.append(" 1 0 0 1 ");
    appendMeasure(x);
    buffer.appendSpace();
    appendMeasure(paperHeight - y);
    buffer.append(" cm ");
  }
  
  
  @Override
  public void textTransform (int a, int b, int c, int d, int x, int y) {
    appendMeasure(a);
    buffer.appendSpace();
    appendMeasure(b);
    buffer.appendSpace();
    appendMeasure(c);
    buffer.appendSpace();
    appendMeasure(d);
    buffer.appendSpace();
    appendMeasure(x);
    buffer.appendSpace();
    appendMeasure(paperHeight - y);
    buffer.appendSpace();
    buffer.append(" Tm ");
  }
  
  
  public void setGraphicState (String gsName) {
    buffer.append(gsName);
    buffer.append(" gs ");
  }
  
  
  @Override
  public void saveGraphicState () {
    buffer.append("q ");
  }
  
  
  @Override
  public void restoreGraphicState () {
    buffer.append("Q ");
  }
  
  
  @Override
  public void beginText () {
    buffer.append("BT ");
  }
  
  
  @Override
  public void endText () {
    buffer.append("ET ");
    buffer.append("\r\n");
  }
  
  
  public void drawText (String s) {
    buffer.append('(');
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c == '(' || c == ')' || c == '\\') {
        buffer.append('\\');
      }
      buffer.append((byte)c);
    }
    buffer.append(") Tj ");
  }
  
  
  @Override
  public void drawChar (char c) {
    buffer.append('(');
    if (c == '(' || c == ')' || c == '\\') {
      buffer.append('\\');
    }
    buffer.append((byte)c);
    buffer.append(") Tj ");
  }
  
  
  public void drawChar (int x, int y, char c) {
    textOffset (x, y);
    drawChar (c);
  }
  
  
  @Override
  public void drawText (int x, int y, String s) {
    textOffset (x, y);
    drawText (s);
  }
  
  
  @Override
  public void drawTextCentered (int x, int y, String s, int width) {
    int offset = font.getOffset(s, fontSize, width);
    textOffset (x + offset, y);
    drawText (s);
  }
 
  
  @Override
  public void drawTextRight (int y, String s, int x) {
    int advance = font.getAdvance(s, fontSize);
    textOffset (x - advance, y);
    drawText (s);
  }
 
  
  @Override
  public void setFontAndSize (BaseFont baseFont, float fontSize) {
    this.font = baseFont;
    this.fontSize = fontSize;
    PDFName fontId = contentReference.createFont(baseFont.getFontName());
    buffer.append('/');
    buffer.append(fontId.getName());
    buffer.appendSpace();
    buffer.append(fontSize);
    buffer.append(" Tf ");
  }
  
  
  private void transform (int xOffset, int yOffset) {
    buffer.append(" 1 0 0 1 ");
    appendMeasure(xOffset);
    buffer.appendSpace();
    appendMeasure(yOffset);
    buffer.append(" cm ");
  }
  
  
  private void scale (int width, int height) {
    buffer.appendSpace();
    appendMeasure(width);
    buffer.append(" 0 0 ");
    appendMeasure(height);
    buffer.append(" 0 0 cm");
  }
  
  
  @Override
  public void drawImage (IPageImage image, int x, int y, int width) {
    saveGraphicState();
    int scaleX;
    int scaleY;
    //if (width == 0) {
    //  scaleX = (int)(height * (double)image.getPixelsWide() / image.getPixelsHigh());
    //  scaleY = height;
    //} else {
      scaleX = width;
      scaleY = (int)(width * (double)image.getPixelsHigh() / image.getPixelsWide() + 0.5);
    //}
//    transform(x, paperHeight - (y + height));
//    //scale (scaleX, scaleY);
//    PDFName imageId = contentReference.getImage(image);
//    buffer.append('/');
//    buffer.append(imageId.getName());
//    buffer.append(" Do ");
//    width = height * image.getPixelsWide() / image.getPixelsHigh();
    transform(x, paperHeight - (y + scaleY));
    scale (scaleX, scaleY);
    PDFName imageId = contentReference.getImage((PDFImage)image);
    buffer.append('/');
    buffer.append(imageId.getName());
    buffer.append(" Do ");
    restoreGraphicState();
  }
  
  
  @Override
  public void drawTemplate (PDFName templateId, int xOffset, int yOffset) {
    saveGraphicState();
    templateOffset(xOffset, yOffset);
    buffer.append('/');
    buffer.append(templateId.getName());
    buffer.append(" Do ");
    restoreGraphicState();
  }
  
  
  @Override
  public void drawTemplate (IPageTemplate template) {
    saveGraphicState();
    PDFName templateId = contentReference.getTemplate((PDFTemplate)template);
    buffer.append('/');
    buffer.append(templateId.getName());
    buffer.append(" Do ");
    restoreGraphicState();
  }
  
  
  public int getPageNumber() {
    return document.getPageNumber();
  }
  
  
  public byte[] array () {
    return buffer.array();
  }
  

  public int size () {
    return buffer.size();
  }

  
  public void close () {
    put("Length", buffer.size());
    document.writeTop(this);
  }


  public PDFName getPageCountRef (BaseFont pagelevelfont, float pagelevelfontsize) {
    return document.getPageCountRef(pagelevelfont, pagelevelfontsize);
  }
}