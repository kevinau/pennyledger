/*
 * Copyright (c) 2004-2009, Jean-Marc Fran√ßois. All Rights Reserved.
 * Licensed under the New BSD license.  See the LICENSE file.
 */

package org.pennyledger.docimport.test;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.pennyledger.docstore.parser.Dictionary;
import org.pennyledger.docstore.parser.IDocumentContents;
import org.pennyledger.docstore.parser.IImageParser;
import org.pennyledger.docstore.parser.IPDFParser;
import org.pennyledger.docstore.parser.ISegment;
import org.pennyledger.docstore.parser.ITrainingData;
import org.pennyledger.docstore.parser.SegmentType;
import org.pennyledger.docstore.parser.SourcePath;
import org.pennyledger.docstore.parser.impl.DocumentContentsBuilder;
import org.pennyledger.docstore.parser.impl.FileIO;
import org.pennyledger.docstore.parser.impl.PDFBoxPDFParser;
import org.pennyledger.docstore.parser.impl.TesseractImageOCR;

import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.Observation;
import be.ac.ulg.montefiore.run.jahmm.ObservationInteger;
import be.ac.ulg.montefiore.run.jahmm.OpdfInteger;
import be.ac.ulg.montefiore.run.jahmm.OpdfIntegerFactory;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchLearner;
import be.ac.ulg.montefiore.run.jahmm.toolbox.KullbackLeiblerDistanceCalculator;
import be.ac.ulg.montefiore.run.jahmm.toolbox.MarkovGenerator;

/**
 * This class demonstrates how to build a HMM with known parameters, how to
 * generate a sequence of observations given a HMM, how to learn the parameters
 * of a HMM given observation sequences, how to compute the probability of an
 * observation sequence given a HMM and how to convert a HMM to a Postscript
 * drawing.
 * <p>
 * The example used is that of a wireless computer network that can experience
 * jamming. When the wireless medium is (resp. is not) jammed, a lot (resp. few)
 * packets are lost. Thus, the HMMs built here have two states (jammed/not
 * jammed).
 */
public class HMMTest {
  
  private static class SegmentXComparator implements Comparator<ISegment> {

    /* One point at 150dpi */
    private static final double POINT = 150.0 / 72.0;
    
    @Override
    public int compare(ISegment arg0, ISegment arg1) {
      float xdiff = arg0.getX0() - arg1.getX0();
      if (xdiff > -POINT && xdiff < POINT) {
        // The x coordinates are close enough to be the same
        if (arg0.getY0() < arg1.getY0()) {
          return -1;
        } else if (arg0.getY0() > arg1.getY0()) {
          return +1;
        } else {
          return 0;
        }
      }
      if (arg0.getX0() < arg1.getX0()) {
        return -1;
      } else {
        return +1;
      }
    }
        
  }
  
  
  private static class SegmentYComparator implements Comparator<ISegment> {

    /* One point at 150dpi */
    private static final double POINT = 150.0 / 72.0;
    
    @Override
    public int compare(ISegment arg0, ISegment arg1) {
      float ydiff = arg0.getY0() - arg1.getY0();
      if (ydiff > -POINT && ydiff < POINT) {
        // The y coordinates are close enough to be the same
        if (arg0.getX0() < arg1.getX0()) {
          return -1;
        } else if (arg0.getX0() > arg1.getX0()) {
          return +1;
        } else {
          return 0;
        }
      }
      if (arg0.getY0() < arg1.getY0()) {
        return -1;
      } else {
        return +1;
      }
    }
    
  }
  
  
  private final IImageParser imageParser;
  private final IPDFParser pdfParser;

  private Dictionary dictionary;
  
  static public void main(String[] argv) throws java.io.IOException {
    
    IImageParser imageParser = new TesseractImageOCR();
    IPDFParser pdfParser = new PDFBoxPDFParser(imageParser);

    HMMTest hmmTest = new HMMTest(imageParser, pdfParser);

    hmmTest.run();
  }
  
  
  public HMMTest (IImageParser imageParser, IPDFParser pdfParser) {
    this.imageParser = imageParser;
    this.pdfParser = pdfParser;
  }
  
  
  private void run() {
    DocumentContentsBuilder contentsBuilder = new DocumentContentsBuilder(pdfParser, imageParser);

    Path dir = Paths.get("C:/Users/Kevin/Accounts/JH Shares/Telstra");
    String pattern = "2012-03-23.pdf";

    dictionary = new Dictionary();
    dictionary.setReadOnly(false);
    int dictSize = dictionary.size();
    
    List<Path> trainingSources = new ArrayList<>();
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, pattern)) {
      for (Path fileEntry : stream) {
        Path hashNamedPath = SourcePath.createMD5Named(fileEntry);
        FileIO.conditionallyCopyFile(fileEntry, hashNamedPath);

        IDocumentContents doc = contentsBuilder.buildContent(hashNamedPath, false);
        
        Path dataPath = new SourcePath(hashNamedPath).getDataPath();
        ITrainingData trainingData = ITrainingData.load(dataPath, DividendStatement.class);
        doc.updateDictionary(dictionary, trainingData);
        
        trainingSources.add(hashNamedPath);
      }
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

    if (trainingSources.size() >= 3) {
      dictionary.purgeSingleWords();
    }
    dictionary.setReadOnly(true);
    
    /* Build observation sequences (observationSet) */
    List<List<ObservationInteger>> observationSet = new ArrayList<>();
    
    for (Path hashNamedPath : trainingSources) {
      Path contentsPath = new SourcePath(hashNamedPath).getContentsPath();
      IDocumentContents doc = IDocumentContents.load(contentsPath);
      doc.sortSegments();
      
      Path dataPath = new SourcePath(hashNamedPath).getDataPath();
      ITrainingData trainingData = ITrainingData.load(dataPath, DividendStatement.class);

      System.out.println();
      System.out.println(hashNamedPath.getFileName().toString());
      System.out.println("Dictionary size " + dictionary.size());
 
      List<? extends ISegment> segmentsAcross = doc.getSegments();
      List<? extends ISegment> segmentsDown = new ArrayList<>(segmentsAcross);
      Collections.sort(segmentsAcross, new SegmentXComparator());
      Collections.sort(segmentsDown, new SegmentYComparator());
      
      System.out.print("Left to right: ");
      for (ISegment obv : segmentsAcross) {
        String w = obv.getText();
        System.out.print(w);
        System.out.print('¨');
      }
      System.out.println();
      System.out.print("Down: ");
      for (ISegment obv : segmentsDown) {
        String w = obv.getText();
        System.out.print(w);
        System.out.print('¨');
      }
      System.out.println();

      findSpecials(segmentsAcross, segmentsDown, trainingData, observationSet);
    }
    
    /* Build a HMM */
    Hmm<ObservationInteger> hmm =  new Hmm<>(dictSize, new OpdfIntegerFactory(dictSize));
    
    /* Baum-Welch learning */
    
    BaumWelchLearner bwl = new BaumWelchLearner();
    
    Hmm<ObservationInteger> learntHmm = new Hmm<>(dictSize, new OpdfIntegerFactory(dictSize));
    
    // This object measures the distance between two HMMs
    KullbackLeiblerDistanceCalculator klc = new KullbackLeiblerDistanceCalculator();
    
    // Incrementally improve the solution
    for (int i = 0; i < 10; i++) {
      System.out.println("Distance at iteration " + i + ": " + klc.distance(learntHmm, hmm));
      learntHmm = bwl.iterate(learntHmm, observationSet);
    }
    
    //////System.out.println("Resulting HMM:\n" + learntHmm);
    

//
//    Hmm<ObservationDiscrete<Packet>> learntHmm = buildInitHmm();
//
//    // This object measures the distance between two HMMs
//    KullbackLeiblerDistanceCalculator klc = new KullbackLeiblerDistanceCalculator();
//
//    // Incrementally improve the solution
//    for (int i = 0; i < 10; i++) {
//      System.out.println("Distance at iteration " + i + ": " + klc.distance(learntHmm, hmm));
//      learntHmm = bwl.iterate(learntHmm, sequences);
//    }
//
//    System.out.println("Resulting HMM:\n" + learntHmm);
//
//    /* Computing the probability of a sequence */
//    ObservationDiscrete<Packet> declaredObservation = Packet.DECLARED.observation();
//    ObservationDiscrete<Packet> paymentObservation = Packet.PAYMENT.observation();
//
//    List<ObservationDiscrete<Packet>> testSequence = new ArrayList<ObservationDiscrete<Packet>>();
//    testSequence.add(declaredObservation);
//    testSequence.add(declaredObservation);
//    testSequence.add(paymentObservation);
//
//    System.out.println("Sequence probability: " + learntHmm.probability(testSequence));
//
//    /* Write the final result to a 'dot' (graphviz) file. */
//    (new GenericHmmDrawerDot()).write(learntHmm, "learntHmm.dot");
  }

  
  private void findSpecials (List<? extends ISegment> segmentsAcross, List<? extends ISegment> segmentsDown, ITrainingData trainingData, List<List<ObservationInteger>> observationSet) {
    for (int t = 0; t < segmentsDown.size(); t++) {
      ISegment special = segmentsDown.get(t);
      if (special.getType() != SegmentType.TEXT) {
        String specialWord = trainingData.resolveValue(special.getType(), special.getValue());
        if (specialWord != null) {
          // Walk backwards to find overlapping segments
          LinkedList<ObservationInteger> observationsLeft = leftwiseOverlaps(special, segmentsAcross);
          LinkedList<ObservationInteger> observationsUp = upwardOverlaps(special, segmentsDown);
          //List<ObservationInteger> observationSequence = interlaceObservations(observationsLeft, observationsUp);

          // Add the special observation to the end of the observation list
          int wordIndex = dictionary.getWordIndex(specialWord);
          ObservationInteger observation = new ObservationInteger(wordIndex);
          observationsLeft.add(observation);
          observationsUp.add(observation);
          
          if (observationsLeft.size() <= 1) {
            throw new RuntimeException("Observation sequence too short");
          }
          if (observationsUp.size() <= 1) {
            throw new RuntimeException("Observation sequence too short");
          }
          observationSet.add(observationsLeft);
          observationSet.add(observationsUp);
//          for (Observation obv : observationSequence) {
//            int wi = ((ObservationInteger)obv).value;
//            String w = dictionary.getWord(wi);
//            System.out.print(w);
//            System.out.print('|');
//          }
//          System.out.println();
        }
      }
    }
  }

  
  private LinkedList<ObservationInteger> leftwiseOverlaps (ISegment special, List<? extends ISegment> segmentsAcross) {
    LinkedList<ObservationInteger> found = new LinkedList<>();
    
    float x0 = special.getX0();
    for (int i = segmentsAcross.size() - 1; i >= 0; i--) {
      ISegment prior = segmentsAcross.get(i);
      //System.out.println("--- " + i + " " + prior + " " + prior.overlapsHorizontally(special));
      if (prior.getX0() < x0 && prior.overlapsHorizontally(special)) {
        String word = prior.getText();
        int wordIndex = dictionary.getWordIndex(word);
        if (wordIndex != -1) {
          ObservationInteger observation = new ObservationInteger(wordIndex);
          found.addFirst(observation);
        }
      }
    }
    return found;
  }
  
    
  private LinkedList<ObservationInteger> upwardOverlaps (ISegment special, List<? extends ISegment> segmentsDown) {
    LinkedList<ObservationInteger> found = new LinkedList<>();
    
    float y0 = special.getY0();
    for (int j = segmentsDown.size() - 1; j >= 0; j--) {
      ISegment prior = segmentsDown.get(j);
      //System.out.println("||| " + j + " " + prior + " " + prior.overlapsVertically(special));
      if (prior.getY0() < y0 && prior.overlapsVertically(special)) {
        String word = prior.getText();
        int wordIndex = dictionary.getWordIndex(word);
        if (wordIndex != -1) {
          ObservationInteger observation = new ObservationInteger(wordIndex);
          found.addFirst(observation);
        }
      }
    }
    return found;
  }
  
  
//  private List<ObservationInteger> interlaceObservations (LinkedList<ObservationInteger> leftwiseObservations, LinkedList<ObservationInteger> upwardObservations) {
//    LinkedList<ObservationInteger> combined = new LinkedList<>();
//    
//    Iterator<ObservationInteger> i1 = leftwiseObservations.descendingIterator();
//    Iterator<ObservationInteger> i2 = upwardObservations.descendingIterator();
//    while (i1.hasNext() && i2.hasNext()) {
//      combined.addFirst(i1.next());
//      combined.addFirst(i2.next());
//    }
//    while (i1.hasNext()) {
//      combined.addFirst(i1.next());
//    }
//    while (i2.hasNext()) {
//      combined.addFirst(i2.next());
//    }
//    return combined;
//  }

  
  Hmm<ObservationInteger> buildHmm (int dictSize, int targetsCount, List<List<ObservationInteger>> observationSet) {
    Hmm<ObservationInteger> hmm = new Hmm<ObservationInteger>(targetsCount, new OpdfIntegerFactory(targetsCount));
    
    // Load Pi values for each final state
    int[] targetCounts = new int[dictSize];
    int overallCount = 0;
    for (List<ObservationInteger> observationSequence : observationSet) {
      int n = observationSequence.size();
      int target = observationSequence.get(n - 1).value;
      targetCounts[target]++;
      overallCount++;
    }
    for (int i = 0; i < dictSize; i++) {
      hmm.setPi(i, ((double)targetCounts[i]) / overallCount);
    }
    targetCounts = null;
    
    // Load the transition probabilities
    int[][] transitionCounts = new int[dictSize][dictSize];
    overallCount = 0;
    for (List<ObservationInteger> observationSequence : observationSet) {
      Iterator<ObservationInteger> it = observationSequence.iterator();
      int i = it.next().value;
      while (it.hasNext()) {
        int j = it.next().value;
        transitionCounts[i][j]++;
        overallCount++;
      }
    }
    for (int i = 0; i < dictSize; i++) {
      for (int j = 0; j < dictSize; j++) {
        hmm.setAij(i, j, ((double)transitionCounts[i][j]) / overallCount);
      }
    }

    int[][] counts = new int[dictSize][dictSize];
    overallCount = 0;
    for (List<ObservationInteger> observationSequence : observationSet) {
      Iterator<ObservationInteger> it = observationSequence.iterator();
      int from = it.next().value;
      while (it.hasNext()) {
        int to = it.next().value;
        counts[from][to]++;
        overallCount++;
      }
    }
    

//    hmm.setPi(0, 0.95);
//    hmm.setPi(1, 0.05);
//    
//    hmm.setOpdf(0, new OpdfDiscrete<Packet>(Packet.class, 
//        new double[] { 0.95, 0.05 }));
//    hmm.setOpdf(1, new OpdfDiscrete<Packet>(Packet.class,
//        new double[] { 0.20, 0.80 }));
//    
//    hmm.setAij(0, 1, 0.05);
//    hmm.setAij(0, 0, 0.95);
//    hmm.setAij(1, 0, 0.10);
//    hmm.setAij(1, 1, 0.90);
    
    return hmm;
  }

  
  /* The HMM this example is based on */
//  static Hmm<ObservationDiscrete<Packet>> buildHmm() {
//    Hmm<ObservationDiscrete<Packet>> hmm = new Hmm<ObservationDiscrete<Packet>>(2,
//        new OpdfDiscreteFactory<Packet>(Packet.class));
//
//    hmm.setPi(0, 0.95);
//    hmm.setPi(1, 0.05);
//
//    hmm.setOpdf(0, new OpdfDiscrete<Packet>(Packet.class, new double[] { 0.95, 0.05 }));
//    hmm.setOpdf(1, new OpdfDiscrete<Packet>(Packet.class, new double[] { 0.20, 0.80 }));
//
//    hmm.setAij(0, 1, 0.05);
//    hmm.setAij(0, 0, 0.95);
//    hmm.setAij(1, 0, 0.10);
//    hmm.setAij(1, 1, 0.90);
//
//    return hmm;
//  }

  /* Initial guess for the Baum-Welch algorithm */
  static Hmm<ObservationInteger> buildInitHmm(int dictSize) {
    Hmm<ObservationInteger> hmm = new Hmm<ObservationInteger>(dictSize, new OpdfIntegerFactory(dictSize));
    
    hmm.setPi(0, 0.50);
    hmm.setPi(1, 0.50);

    hmm.setOpdf(0, new OpdfInteger(new double[] { 0.8, 0.2 }));
    hmm.setOpdf(1, new OpdfInteger(new double[] { 0.1, 0.9 }));

    hmm.setAij(0, 1, 0.2);
    hmm.setAij(0, 0, 0.8);
    hmm.setAij(1, 0, 0.2);
    hmm.setAij(1, 1, 0.8);

    return hmm;
  }

  /* Generate several observation sequences using a HMM */
  static <O extends Observation> List<List<O>> generateSequences(Hmm<O> hmm) {
    MarkovGenerator<O> mg = new MarkovGenerator<O>(hmm);

    List<List<O>> sequences = new ArrayList<List<O>>();
    for (int i = 0; i < 200; i++) {
      sequences.add(mg.observationSequence(100));
    }
    return sequences;
  }
}
