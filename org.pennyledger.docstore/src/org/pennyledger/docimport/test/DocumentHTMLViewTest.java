package org.pennyledger.docimport.test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.pennyledger.docstore.Dictionary;
import org.pennyledger.docstore.IDocumentContents;
import org.pennyledger.docstore.IDocumentStore;
import org.pennyledger.docstore.ISegment;
import org.pennyledger.docstore.SegmentType;
import org.pennyledger.docstore.impl.FileSystemDocumentStore;
import org.pennyledger.docstore.parser.IImageParser;
import org.pennyledger.docstore.parser.SourcePath;
import org.pennyledger.docstore.parser.impl.DocumentContentsBuilder;
import org.pennyledger.docstore.parser.impl.FileIO;
import org.pennyledger.docstore.parser.impl.PDFBoxPDFParser;
import org.pennyledger.docstore.parser.impl.TesseractImageOCR;
import org.pennyledger.html.Body;
import org.pennyledger.html.Div;
import org.pennyledger.html.Head;
import org.pennyledger.html.Html;
import org.pennyledger.html.Style;
import org.pennyledger.math.Decimal;
import org.pennyledger.time.LocalDateFactory;
import org.pennyledger.util.MD5HashFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentHTMLViewTest {

  private static final Logger logger = LoggerFactory.getLogger(DocumentHTMLViewTest.class);

  public static void main(String[] args) throws Exception {
    String[] sources = { "C:/Users/Kevin/Accounts/JH Shares/Telstra/2010-09-24.pdf",
        "C:/Users/Kevin/Accounts/JH Shares/Telstra/2009-04-02.pdf", };

    FileSystemDocumentStore docStore = new FileSystemDocumentStore();
    docStore.activate(null);

    DocumentHTMLViewTest htmlBuilder = new DocumentHTMLViewTest();
    for (String source : sources) {
      String id = new MD5HashFactory().getFileDigest(Paths.get(source)).toString();
      logger.info("Building HTML {} as {}", source, id);
      htmlBuilder.build(id, docStore);
    }
  }

  public void build(String id, IDocumentStore docStore) throws IOException {
    IImageParser imageParser = new TesseractImageOCR();
    PDFBoxPDFParser pdfParser = new PDFBoxPDFParser(imageParser);

    DocumentContentsBuilder contentsBuilder = new DocumentContentsBuilder(pdfParser, imageParser);

    Path dir = Paths.get("C:/Users/Kevin/Accounts/JH Shares/Telstra");
    // Path dir = Paths.get("C:/Users/Kevin/Accounts/JH Shares/Australian
    // Foundation");
    String pattern = "*.pdf";
    // String pattern = "2011-02-25a.pdf";
    List<Path> sources = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, pattern)) {
      for (Path fileEntry : stream) {
        sources.add(fileEntry);
      }
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

    Dictionary dictionary = new Dictionary();
    dictionary.setReadOnly(false);

    IDocumentContents doc = docStore.getContents(id);
    List<? extends ISegment> segments = doc.getSegments();

    // Now build HTML
    Path imagePath = docStore.getViewImagePath(id);
    Path htmlPath = docStore.getViewHTMLPath(id);

    Html html = new Html();
    Head head = html.newHead();
    Style style = head.newStyle();
    style.add("html, body { margin:0; padding:0; }");
    style.add(".dateSegment { border: 1px solid red; }");
    style.add(".currencySegment { border: 1px solid cyan; }");
    style.add(".specialSegment { border: 1px solid green; }");
    style.add(".unknownSegment { border: 1px dashed #888; }");
    style.add("div span { font-family: sans-serif; font-size: 6pt; background: white; }");

    int i = 0;
    for (ISegment segment : segments) {
      int x0 = (int)(segment.getX0() - 0.99);
      int y0 = (int)(segment.getY0() - 0.99);
      int width = (int)(segment.getWidth() + 0.99 * 2);
      int height = (int)(segment.getHeight() + 0.99 * 2);
      style.add("#S" + i + "{ display: inline-block;" + "position: absolute;" + "left: " + x0 + "px;" + "top: " + y0
          + "px;" + "width: " + width + "px;" + "height: " + height + "px;" + "}\r\n");
      i++;
    }
    Body body = html.newBody();

    Path relativeImagePath = htmlPath.getParent().relativize(imagePath);
    String imageFileName = relativeImagePath.toString();
    body.newImg(imageFileName);

    i = 0;
    for (ISegment segment : segments) {
      Div div = body.newDiv();
      div.setId("S" + i);
      String tooltip = segment.getText();
      if (segment.getType() != SegmentType.TEXT) {
        tooltip += "(" + segment.getType() + ")";
      }
      div.setTitle(tooltip);

      switch (segment.getType()) {
      case DATE:
        div.setClass("dateSegment");
        break;
      case CURRENCY:
        div.setClass("currencySegment");
        break;
      case SPECIAL:
        div.setClass("specialSegment");
        break;
      case TEXT:
        div.setClass("unknownSegment");
        break;
      }
      // div.newSpan().addText(Integer.toString(i));
      i++;
    }
    html.write(htmlPath);
  }
}
