package org.pennyledger.docstore.parser;

import org.pennyledger.docstore.SegmentType;

public interface ISegmentMatchResult {

  public int start();
  
  public int end();
  
  public SegmentType type();
  
  public Object value();
  
}
