package org.gyfor.report.page.pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gyfor.report.PaperSize;
import org.gyfor.report.page.BaseFont;
import org.gyfor.report.page.IPageDocument;

public class PDFDocument implements IPageDocument {
  
  private static final String PRODUCER = "Gecko Software (UK and Australia)";

  private final CountedOutputStream writer;
  
  private PDFTemplate pageCountTemplate;
  private boolean pageCountTemplateReady = false;
  
  private PDFIndirect catalogObject;
  private PDFIndirect infoObject;
  private PDFIndirect pageSetObject;
  private PDFIndirect extGraphicStateObject;
  private List<PDFReference> pageObjectList;
  private int templateIndex = 1;
  private int imageIndex = 1;
  
  private final List<PDFIndirect> indirects = new ArrayList<PDFIndirect>();
  private final List<Integer> xrefs = new ArrayList<Integer>();
  private final PDFDictionary trailer = new PDFDictionary();
  
  private Map<String, PDFIndirect> fontMap = new HashMap<String, PDFIndirect>();
  private Map<String, PDFImage> imageMap = new HashMap<String, PDFImage>();
  
  private int pageNumber;
  
  
  public PDFDocument (String fileName) throws IOException {
    this(new FileOutputStream(fileName));
  }
  
  
  public PDFDocument (Path path) throws IOException {
    this (Files.newOutputStream(path));
  }

  
  public PDFDocument (OutputStream os) {
    writer = new CountedOutputStream(os);
    writer.writeln("%PDF-1.7");
    writer.writeln("%\u00f6\u00e4\u00fc\u00df");

    pageNumber = 0;
    pageCountTemplate = createTemplate();

//    createPageCountTemplate (pageLevelFont, 10);
  }
  
  
  int getBodyObjectCount () {
    return indirects.size();
  }
  
  
  public void addBodyObject (PDFIndirect obj) {
    indirects.add(obj);
  }
  
  
  public PDFIndirect createCatalogObject () {
    catalogObject = new PDFIndirect(this, "Catalog");
    return catalogObject;
  }
  
  
  public PDFIndirect createFont (String fontName) {
    PDFIndirect fontObject = fontMap.get(fontName);
    if (fontObject == null) {
      int fontIndex = fontMap.size() + 1;
      String fn = "F" + fontIndex;
      PDFName fontId = new PDFName(fn);
      fontObject = new PDFIndirect(this, "Font");
      fontObject.put("Subtype", new PDFName("Type1"));
      fontObject.put("Name", fontId);
      fontObject.put("BaseFont", new PDFName(fontName));
      fontObject.put("Encoding", new PDFName("WinAnsiEncoding"));
      writeTop(fontObject);
      fontMap.put(fontName, fontObject);
    }
    return fontObject;
  }

  
  @Override
  public PDFImage createImage (String imageFileName, InputStream imageInputStream) {
    PDFImage imageObject = imageMap.get(imageFileName);
    if (imageObject == null) {
      imageObject = new PDFImage(this, imageIndex, imageInputStream);
      imageIndex++;
      imageMap.put(imageFileName, imageObject);
    }
    return imageObject;
  }
  
  
  public PDFPage createPage () {
    return createPage (PaperSize.A4);
  }
  
  
  @Override
  public PDFPage createPage (PaperSize paperSize) {
    if (pageSetObject == null) {
      pageSetObject = new PDFIndirect(this, "Pages");
      pageObjectList = new ArrayList<PDFReference>();
    }

    pageNumber++;
    PDFPage pageObject = new PDFPage(this, paperSize);
    pageObject.put("Parent", pageSetObject);
    pageObjectList.add(pageObject.getReference());

    pageObject.addTemplateRef(pageCountTemplate);
    return pageObject;
  }
  
  
  @Override
  public PDFIndirect createIndirect (PaperSize paperSize) {
    PDFIndirect indirect = new PDFIndirect(this);
    return indirect;
  }
  
  
  @Override
  public PDFTemplate createTemplate () {
    PDFTemplate templateObject = new PDFTemplate(this, templateIndex);
    templateIndex++;
    return templateObject;
  }
  
  
  public PDFIndirect createExtGraphicState () {
    if (extGraphicStateObject == null) {
      extGraphicStateObject = new PDFIndirect(this, "ExtGState");
      extGraphicStateObject.put("SA", new PDFBoolean(true));
      writeTop(extGraphicStateObject);
    }
    return extGraphicStateObject;
  }

  
  public void incrementPageNumber () {
    pageNumber++;
  }
  
  
  public int getPageNumber () {
    return pageNumber;
  }
  
  
  @Override
  public void setTitle(String title) {
    if (infoObject == null) {
      infoObject = new PDFIndirect(this);
    }
    infoObject.put("Title", new PDFString(title));
  }
  
  
  @Override
  public void setAuthor(String author) {
    if (infoObject == null) {
      infoObject = new PDFIndirect(this);
    }
    infoObject.put("Author", new PDFString(author));
  }
  
  
  public void setSubject(String subject) {
    if (infoObject == null) {
      infoObject = new PDFIndirect(this);
    }
    infoObject.put("Subject", new PDFString(subject));
  }
  
  
  public void setCreator(String creator) {
    if (infoObject == null) {
      infoObject = new PDFIndirect(this);
    }
    infoObject.put("Creator", new PDFString(creator));
  }
  
  
  public void writeTop (PDFIndirect indirect) {
    int n = indirect.getObjNumber();
    while (xrefs.size() <= n) {
      xrefs.add(0);
    }
    xrefs.set(n, writer.getOffset());
    indirect.write(writer);
    writer.writeln();
  }
  
  
  private NumberFormat nformat = new DecimalFormat("0000000000");
  private DateFormat dformat = new SimpleDateFormat("yyyyMMddhhmmss"); 
  
  public PDFName getPageCountRef (BaseFont font, float fontSize) {
    if (!pageCountTemplateReady) {
      int width = font.getAdvance("000", fontSize);
      int height = font.getLineHeight(fontSize);
      
      // Create the content--but don't use it yet
      pageCountTemplate.createContent(0, 0, width, height);
      writeTop (pageCountTemplate);
      pageCountTemplateReady = true;
    }
    return pageCountTemplate.getId();
  }
  
  
  @Override
  public void close () {
    PDFContent cx = pageCountTemplate.getContent();
    if (cx != null) {
      cx.beginText();
      cx.drawText(Integer.toString(pageNumber));
      cx.endText();
      writeTop(cx);
    }
    
    catalogObject = new PDFIndirect(this, "Catalog");
    catalogObject.put("Pages", pageSetObject);
    writeTop(catalogObject);
    
    pageSetObject.put("Kids", pageObjectList);
    pageSetObject.put("Count", pageObjectList.size());
    writeTop(pageSetObject);

    if (infoObject != null) {
      infoObject.put("Producer", new PDFString(PRODUCER));
      infoObject.put("CreationDate", new PDFString("D:" + dformat.format(new Date())));
      writeTop(infoObject);
      trailer.put("Info", infoObject);
    }

    int xrefOffset = writer.getOffset();
    writer.writeln("xref");
    writer.write("0 ");
    writer.writeln(xrefs.size());
    
    writer.write("0000000000 65535 f");
    writer.write2n();
    for (int i = 1; i < xrefs.size(); i++) {
      int ix = xrefs.get(i);
      writer.write(nformat.format(ix));
      writer.write(" 00000 n");
      writer.write2n();
    }
    
    writer.writeln("trailer");
    trailer.put("Size", xrefs.size());
    trailer.put("Root", catalogObject);
    trailer.write(writer);
    writer.writeln();
    
    writer.writeln("startxref");
    writer.writeln(xrefOffset);
    writer.writeln("%%EOF");
    writer.close();
  }
  
  
}
