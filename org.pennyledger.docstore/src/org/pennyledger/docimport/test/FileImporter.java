package org.pennyledger.docimport.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.pennyledger.docstore.parser.ClassSet;
import org.pennyledger.docstore.parser.Dictionary;
import org.pennyledger.docstore.parser.IImageParser;
import org.pennyledger.docstore.parser.IPDFParser;
import org.pennyledger.docstore.parser.impl.DocumentContentsBuilder;
import org.pennyledger.docstore.parser.impl.InstanceSet;
import org.pennyledger.docstore.parser.impl.PDFBoxPDFParser;
import org.pennyledger.docstore.parser.impl.TesseractImageOCR;
import org.pennyledger.docstore.parser.weka.WekaClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.core.Instance;
import weka.core.Instances;

public class FileImporter {

  private static final Logger logger = LoggerFactory.getLogger(FileImporter.class);
  
  public static void main(String[] args) throws Exception {
    String dirBase = "C:/Users/Kevin/Accounts/JH Shares/";
    String[] targetNames = {
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
    ClassSet classSet = new ClassSet(targetNames);
    
    IImageParser imageParser = new TesseractImageOCR();
    IPDFParser pdfParser = new PDFBoxPDFParser(imageParser);
    DocumentContentsBuilder docContentsBuilder = new DocumentContentsBuilder(pdfParser, imageParser);
    
    logger.info("Build training set");
    List<Path> trainingPaths = new ArrayList<>();
    List<String> classNames = new ArrayList<>();
    for (String targetName : targetNames) {
      Path dir = Paths.get(dirBase, targetName);
      docContentsBuilder.directorySearchBuildContents(dir, "200*.pdf", dictionary, trainingPaths);
      while (classNames.size() < trainingPaths.size()) {
        classNames.add(targetName);
      }
    }
    dictionary.setReadOnly(true);
    
    InstanceSet trainingSet = new InstanceSet("TrainingSet", classSet, dictionary);
    trainingSet.loadInstances(trainingPaths, classNames);
    logger.info("{} files loaded", trainingPaths.size());
    trainingSet.print();
    
    logger.info("Build evaluation set");
    List<Path> evaluationPaths = new ArrayList<>();
    classNames = new ArrayList<>();
    for (String targetName : targetNames) {
      Path dir = Paths.get(dirBase, targetName);
      docContentsBuilder.directorySearchBuildContents(dir, "2010*.pdf", null, evaluationPaths);
      while (classNames.size() < evaluationPaths.size()) {
        classNames.add(targetName);
      }
    }
    
    InstanceSet evaluationSet = new InstanceSet("EvaluationSet", classSet, dictionary);
    evaluationSet.loadInstances(evaluationPaths, classNames);
    logger.info("{} files loaded", evaluationPaths.size());

    evaluationSet.print();
    
    WekaClassifier classifier = new WekaClassifier(trainingSet.getInstances());
    // Test the model
    classifier.evaluate(evaluationSet.getInstances());

    logger.info("Build classification tests");
    List<Path> classifyPaths = new ArrayList<>();
    for (String targetName : targetNames) {
      Path dir = Paths.get(dirBase, targetName);
      docContentsBuilder.directorySearchBuildContents(dir, "2011*.pdf", null, classifyPaths);
    }
    
    InstanceSet classifySet = new InstanceSet("ClassifySet", classSet, dictionary);
    classifySet.loadInstances(evaluationPaths);
    //classifySet.print();
    logger.info("{} files loaded", classifyPaths.size());
    
    Instances classifyInstances = classifySet.getInstances();
    for (int j = 0; j < classifyInstances.numInstances(); j++) {
      Instance instance = classifyInstances.get(j);
      int score = classifier.classify(instance);
      System.out.println();
      System.out.println(classifyPaths.get(j));
      System.out.println(instance);
      System.out.println(score);
    }
  }
  
}
