package org.pennyledger.docimport.thunderbird;

public interface ILineProcessor {

  public void processLine (long fileOffset, String line);
  
}
