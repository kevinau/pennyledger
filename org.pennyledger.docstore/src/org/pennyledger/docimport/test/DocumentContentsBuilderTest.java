package org.pennyledger.docimport.test;

import java.nio.file.Paths;

import org.pennyledger.docstore.impl.FileSystemDocumentStore;
import org.pennyledger.docstore.parser.IImageParser;
import org.pennyledger.docstore.parser.impl.DocumentContentsBuilder;
import org.pennyledger.docstore.parser.impl.PDFBoxPDFParser;
import org.pennyledger.docstore.parser.impl.TesseractImageOCR;
import org.pennyledger.util.MD5HashFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentContentsBuilderTest {

  private static final Logger logger = LoggerFactory.getLogger(DocumentContentsBuilderTest.class);
  
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
}
