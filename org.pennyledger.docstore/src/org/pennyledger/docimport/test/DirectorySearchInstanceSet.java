package org.pennyledger.docimport.test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.pennyledger.docstore.parser.ClassSet;
import org.pennyledger.docstore.parser.Dictionary;
import org.pennyledger.docstore.parser.IDocumentContents;
import org.pennyledger.docstore.parser.IImageParser;
import org.pennyledger.docstore.parser.IPDFParser;
import org.pennyledger.docstore.parser.SourcePath;
import org.pennyledger.docstore.parser.impl.DocumentContentsBuilder;
import org.pennyledger.docstore.parser.impl.InstanceSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.core.Instances;

public class DirectorySearchInstanceSet {

  private static final Logger logger = LoggerFactory.getLogger(DirectorySearchInstanceSet.class);
  
  private final String relationName;
  private final Dictionary dictionary;
  private final ClassSet classSet;
  private final DocumentContentsBuilder docContentsBuilder;
  private InstanceSet dataSet;
  
  
  public DirectorySearchInstanceSet (String relationName, ClassSet classSet, Dictionary dictionary, IPDFParser pdfParser, IImageParser imageParser) {
    this.relationName = relationName;
    this.classSet = classSet;
    this.dictionary = dictionary;
    this.docContentsBuilder = new DocumentContentsBuilder(pdfParser, imageParser);
  }

  
  public void accumInstanceSet (SourcePath file, String className) {
    logger.info("Accumulating " + className + ": " + file.getFileName());

    double classIndex = classSet.getClassIndex(className);
    
    Path docContentsFile = file.getContentsPath();
    IDocumentContents docContents = IDocumentContents.load(docContentsFile);
    //docContents.resolveWordIndexs(dictionary);
    if (dataSet == null) {
      dataSet = new InstanceSet(relationName, classSet, dictionary);
    }
    dataSet.addInstance(classIndex, docContents);
  }
 
  
  public void importFiles (Path dir, String pattern, String className, boolean buildingDictionary, List<Path> foundFiles) {
    if (Files.exists(dir)) {
      try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, pattern)) {
        for (Path fileEntry : stream) {
          if (foundFiles != null) {
            foundFiles.add(fileEntry);
          }
          IDocumentContents docContents = docContentsBuilder.buildContent(fileEntry, false);
          if (buildingDictionary) {
            docContents.updateDictionary(dictionary);
          }
          classSet.resolveClass(className);
        }
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
  }
  
  
  public void buildContents (List<Path> files, Dictionary dictionary) {
    for (Path fileEntry : files) {
      IDocumentContents docContents;
      try {
        docContents = docContentsBuilder.buildContent(fileEntry, false);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
      docContents.updateDictionary(dictionary);
    }
  }
  
  
  public void accumInstanceSets (Path dir, String pattern, String className) {
    double classIndex = classSet.getClassIndex(className);
    
    if (Files.exists(dir)) {
      try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, pattern)) {
        for (Path fileEntry : stream) {
          System.out.println(fileEntry);
          SourcePath sourcePath = new SourcePath(fileEntry);
          Path documentSetFile = sourcePath.getContentsPath();
          IDocumentContents docContents = IDocumentContents.load(documentSetFile);          
          //docContents.updateDictionary(dictionary);
          if (dataSet == null) {
            dataSet = new InstanceSet(relationName, classSet, dictionary);
          }
          dataSet.addInstance(classIndex, docContents);
        }
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  
  public void buildInstanceSets (List<Path> files, List<Integer> classIndexes, Dictionary dictionary) {
    if (classIndexes.size() != 1 && files.size() != classIndexes.size()) {
      throw new IllegalArgumentException("Files size and classIndexes size are not the same");
    }
    
    for (int i = 0; i < files.size(); i++) {
      Path fileEntry = files.get(i);
      SourcePath sourcePath = new SourcePath(fileEntry);
      Path documentSetFile = sourcePath.getContentsPath();
      IDocumentContents docContents = IDocumentContents.load(documentSetFile);
      docContents.updateDictionary(dictionary);
      if (dataSet == null) {
        dataSet = new InstanceSet(relationName, classSet, dictionary);
      }
      int classIndex = classIndexes.get(i);
      dataSet.addInstance(classIndex, docContents);      
    }
  }

  
  public Instances getInstances() {
    return dataSet.getInstances();
  }
  
  
  public void print () {
    dataSet.print();
  }

}
