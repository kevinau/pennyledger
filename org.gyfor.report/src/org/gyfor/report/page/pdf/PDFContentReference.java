package org.gyfor.report.page.pdf;


public class PDFContentReference extends PDFIndirect {

  private final PDFDocument document;
  
  private PDFContent contentObject;
  private PDFDictionary resourcesObject;
  private PDFDictionary fontSetObject;
  private PDFDictionary xobjSetObject;
  
  
  public PDFContentReference (PDFDocument document, String type) {
    super (document, type);
    this.document = document;
  }
  
  
  public PDFName createFont (String fontName) {
    if (resourcesObject == null) {
      resourcesObject = new PDFDictionary();
    }
    if (fontSetObject == null) {
      fontSetObject = new PDFDictionary();
      resourcesObject.put("Font", fontSetObject);
    }
    PDFIndirect fontObject = document.createFont(fontName);
    PDFName fontId = (PDFName)fontObject.get("Name");
    fontSetObject.put(fontId.getName(), fontObject);
    return fontId;
  }

  
  public PDFName getImage (PDFImage image) {
    if (resourcesObject == null) {
      resourcesObject = new PDFDictionary();
    }
    if (xobjSetObject == null) {
      xobjSetObject = new PDFDictionary();
      resourcesObject.put("XObject", xobjSetObject);
    }
    PDFName imageId = image.getId();
    xobjSetObject.put(imageId.getName(), image.getReference());
    return imageId;
  }

  
  public PDFName getTemplate (PDFTemplate template) {
    if (resourcesObject == null) {
      resourcesObject = new PDFDictionary();
    }
    if (xobjSetObject == null) {
      xobjSetObject = new PDFDictionary();
      resourcesObject.put("XObject", xobjSetObject);
    }
    PDFName templateId = template.getId();
    xobjSetObject.put(templateId.getName(), template.getReference());
    return templateId;
  }
  
  
  public void addTemplateRef (PDFTemplate template) {
    if (resourcesObject == null) {
      resourcesObject = new PDFDictionary();
    }
    if (xobjSetObject == null) {
      xobjSetObject = new PDFDictionary();
      resourcesObject.put("XObject", xobjSetObject);
    }
    PDFName templateId = template.getId();
    xobjSetObject.put(templateId.getName(), template.getReference());
  }
  
  
  public void addExtGraphicState () {
    PDFDictionary extGraphicStateDict = new PDFDictionary();
    PDFIndirect extGraphicStateObject = document.createExtGraphicState();
    extGraphicStateDict.put("GS1", extGraphicStateObject.getReference());

    if (resourcesObject == null) {
      resourcesObject = new PDFDictionary();
    }
    resourcesObject.put("ExtGState", extGraphicStateDict);
  }

  
  public PDFContent createContent (int paperWidth, int paperHeight) {
    if (contentObject == null) {
      contentObject = new PDFContent(document, this, paperWidth, paperHeight);
      put("Contents", contentObject);
    }
    return contentObject;
  }

  
  public PDFContent getContent () {
    return contentObject;
  }

  
  public void close () {
    if (contentObject != null) {
      contentObject.close();
      contentObject = null;
    }
    if (resourcesObject != null) {
      put("Resources", resourcesObject);
    }
    addExtGraphicState();
    document.writeTop(this);
  }

}
