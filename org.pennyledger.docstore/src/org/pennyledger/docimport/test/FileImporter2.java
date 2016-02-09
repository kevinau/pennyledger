package org.pennyledger.docimport.test;

import java.nio.file.Paths;

import org.pennyledger.docstore.parser.ClassSet;
import org.pennyledger.docstore.parser.Dictionary;
import org.pennyledger.docstore.parser.IImageParser;
import org.pennyledger.docstore.parser.IPDFParser;
import org.pennyledger.docstore.parser.impl.PDFBoxPDFParser;
import org.pennyledger.docstore.parser.impl.TesseractImageOCR;
import org.pennyledger.docstore.parser.weka.WekaClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileImporter2 {

  private static final Logger logger = LoggerFactory.getLogger(FileImporter2.class);
  
  public static void main(String[] args) throws Exception {
    String dirBase = "C:/Users/Kevin/Accounts/JH Shares/";
    String[] targetList = {
        "Australian Foundation",
        "BHP Billiton",
/*        "Commwealth Bank",
        "Echo",
        "Qantas",
        "Tabcorp", */
        "Telstra",
/*        "Wesfarmers",
        "Woolworths",
*/    };
    
    Dictionary dictionary = new Dictionary();
    ClassSet classSet = new ClassSet();
    
    IImageParser imageParser = new TesseractImageOCR();
    IPDFParser pdfParser = new PDFBoxPDFParser(imageParser);

    logger.info("Training set.  Searching: " + dirBase);
    DirectorySearchInstanceSet trainingSet = new DirectorySearchInstanceSet("Training set", classSet, dictionary, pdfParser, imageParser);
    for (int i = 0; i < targetList.length; i++) {
      trainingSet.importFiles(Paths.get(dirBase + targetList[i]), "200*.pdf", targetList[i], true, null);
    }
    dictionary.setReadOnly(true);
    for (int i = 0; i < targetList.length; i++) {
      trainingSet.accumInstanceSets(Paths.get(dirBase + targetList[i]), "200*.pdf", targetList[i]);
    }
    trainingSet.print();
    
    logger.info("Evaluation set.  Searching: " + dirBase);
    DirectorySearchInstanceSet evaluationSet = new DirectorySearchInstanceSet("Evaluation set", classSet, dictionary, pdfParser, imageParser);
    for (int i = 0; i < targetList.length; i++) {
      evaluationSet.importFiles(Paths.get(dirBase + targetList[i]), "2010*.pdf", targetList[i], false, null);
    }
    for (int i = 0; i < targetList.length; i++) {
      evaluationSet.accumInstanceSets(Paths.get(dirBase + targetList[i]), "2010*.pdf", targetList[i]);
    }
    evaluationSet.print();
    
    WekaClassifier classifier = new WekaClassifier(trainingSet.getInstances());
    // Test the model
    classifier.evaluate(evaluationSet.getInstances());

//    DirectorySearchInstanceSet classifySet = new DirectorySearchInstanceSet("Classify set", classSet, dictionary, pdfParser, imageParser);
//    for (int i = 0; i < targetList.length; i++) {
//      classifySet.importFiles(Paths.get(dirBase + targetList[i]), "2011*.pdf", null);
//    }
//    for (int i = 0; i < targetList.length; i++) {
//      classifySet.accumInstanceSets(Paths.get(dirBase + targetList[i]), "2011*.pdf", null);
//    }
//    //classifySet.print();
//    
//    Instances classifyInstances = classifySet.getInstances();
//    for (int i = 0; i < classifyInstances.numInstances(); i++) {
//      Instance instance = classifyInstances.get(i);
//      int score = classifier.classify(instance);
//      System.out.println();
//      System.out.println(instance);
//      System.out.println(score);
//    }
  }
  
}
