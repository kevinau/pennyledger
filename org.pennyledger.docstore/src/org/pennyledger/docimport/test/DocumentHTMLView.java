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

import org.pennyledger.docstore.impl.FileSystemDocumentStore;
import org.pennyledger.docstore.parser.Dictionary;
import org.pennyledger.docstore.parser.IDocumentContents;
import org.pennyledger.docstore.parser.IImageParser;
import org.pennyledger.docstore.parser.ISegment;
import org.pennyledger.docstore.parser.ITrainingData;
import org.pennyledger.docstore.parser.SegmentType;
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

public class DocumentHTMLView {

  private static final Logger logger = LoggerFactory.getLogger(DocumentHTMLView.class);

  public static void main(String[] args) throws Exception {
    String[] sources = {
        "C:/Users/Kevin/Accounts/JH Shares/Telstra/2010-09-24.pdf",
        "C:/Users/Kevin/Accounts/JH Shares/Telstra/2009-04-02.pdf",
    };
    
    IImageParser imageParser = new TesseractImageOCR();
    PDFBoxPDFParser pdfParser = new PDFBoxPDFParser(imageParser);
    FileSystemDocumentStore docStore = new FileSystemDocumentStore();
    docStore.activate(null);
    
    DocumentContentsBuilder contentsBuilder = new DocumentContentsBuilder(pdfParser, imageParser);
    for (String source : sources) {
      String id = new MD5HashFactory().getFileDigest(Paths.get(source)).toString();
      logger.info ("Building {} as {}", source, id);
      contentsBuilder.buildContent(id,  true, docStore);
    }
  }

  public buildHTML (String id, IDocumentStore docStore) throws IOException {
    boolean buildingTrainingData = true;
    boolean buildingHTML = true;
    boolean testHMM = true;

    IImageParser imageParser = new TesseractImageOCR();
    PDFBoxPDFParser pdfParser = new PDFBoxPDFParser(imageParser);

    DocumentContentsBuilder contentsBuilder = new DocumentContentsBuilder(pdfParser, imageParser);

    Path dir = Paths.get("C:/Users/Kevin/Accounts/JH Shares/Telstra");
    //Path dir = Paths.get("C:/Users/Kevin/Accounts/JH Shares/Australian Foundation");
    String pattern = "*.pdf";
    //String pattern = "2011-02-25a.pdf";
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

    if (buildingTrainingData == false) {
      for (Path sourcePath : sources) {
        Path hashNamedPath = SourcePath.createMD5Named(sourcePath);
        logger.info("Loading document contents for: " + hashNamedPath.getFileName());

        FileIO.conditionallyCopyFile(sourcePath, hashNamedPath);

        IDocumentContents doc = contentsBuilder.buildContent(hashNamedPath, false);
        Path dataPath = new SourcePath(hashNamedPath).getDataPath();
        ITrainingData trainingData = ITrainingData.load(dataPath, DividendStatement.class);
        doc.updateDictionary(dictionary, trainingData);
      }
    }
    dictionary.dump();

    if (buildingTrainingData) {
      for (Path sourcePath : sources) {
        Path hashNamedPath = SourcePath.createMD5Named(sourcePath);
        FileIO.conditionallyCopyFile(sourcePath, hashNamedPath);

        IDocumentContents doc = contentsBuilder.buildContent(hashNamedPath, false);
        TreeSet<LocalDate> dateSet = new TreeSet<>();
        TreeSet<Decimal> currencySet = new TreeSet<>();
        LocalDate declaredDate = null;
        LocalDate paymentDate = null;
        Decimal dividendAmount = null;
        Decimal frankingCredit = null;

        for (ISegment segment : doc.getSegments()) {
          switch (segment.getType()) {
          case DATE:
            LocalDate d = LocalDateFactory.parseDate(segment.getText());
            dateSet.add(d);
            break;
          case CURRENCY:
            String t = segment.getText();
            int i = 0;
            while (!Character.isDigit(t.charAt(i)) || t.charAt(i) == '-') {
              i++;
            }
            Decimal d1 = Decimal.parse(t.substring(i));
            currencySet.add(d1);
            break;
          default:
            break;
          }
        }
        Iterator<LocalDate> i = dateSet.descendingIterator();
        if (i.hasNext()) {
          paymentDate = i.next();
          if (i.hasNext()) {
            declaredDate = i.next();
          }
        }
        Iterator<Decimal> j = currencySet.descendingIterator();
        if (j.hasNext()) {
          dividendAmount = j.next();
          if (j.hasNext()) {
            frankingCredit = j.next();
          }
        }
        DividendStatement divStmt = new DividendStatement(declaredDate, paymentDate, dividendAmount, frankingCredit);
        Path dataPath = new SourcePath(hashNamedPath).getDataPath();
        divStmt.save(dataPath);
      }
    }

    for (Path sourcePath : sources) {
      Path hashNamedPath = SourcePath.createMD5Named(sourcePath);
      FileIO.conditionallyCopyFile(sourcePath, hashNamedPath);

      IDocumentContents doc = contentsBuilder.buildContent(hashNamedPath, false);

      List<? extends ISegment> segments = doc.getSegments();

      if (testHMM) {
        
      }
      if (buildingHTML) {
        // Now build HTML
        SourcePath hashNamedSourcePath = new SourcePath(hashNamedPath);
        Path imagePath = hashNamedSourcePath.getViewImagePath();
        Path htmlPath = hashNamedSourcePath.getViewHTMLPath();

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
          int x0 = (int) (segment.getX0() - 0.99);
          int y0 = (int) (segment.getY0() - 0.99);
          int width = (int) (segment.getWidth() + 0.99 * 2);
          int height = (int) (segment.getHeight() + 0.99 * 2);
          style.add("#S" + i + "{ display: inline-block;" + "position: absolute;" + "left: " + x0 + "px;" + "top: " + y0
              + "px;" + "width: " + width + "px;" + "height: " + height + "px;" + "}\r\n");
          i++;
        }
        Body body = html.newBody();
        String imageFileName = imagePath.getFileName().toString();
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
          //div.newSpan().addText(Integer.toString(i));
          i++;
        }
        html.write(htmlPath);
      }
    }
  }
}
