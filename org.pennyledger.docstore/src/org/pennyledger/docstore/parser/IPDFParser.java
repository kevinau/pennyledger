package org.pennyledger.docstore.parser;

import java.nio.file.Path;

import org.pennyledger.docstore.IDocumentContents;
import org.pennyledger.docstore.IDocumentStore;

public interface IPDFParser {

  public IDocumentContents parse (String id, Path path, int dpi, IDocumentStore docStore);
  
}
