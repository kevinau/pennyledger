package org.pennyledger.docstore.parser;

public interface ISegmentMatcher {

  public ISegmentMatchResult find (String input, int start, int end);
  
}
