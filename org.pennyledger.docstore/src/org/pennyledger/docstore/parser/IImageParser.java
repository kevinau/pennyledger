package org.pennyledger.docstore.parser;

import java.nio.file.Path;

import org.pennyledger.docstore.IDocumentContents;

public interface IImageParser {

  public IDocumentContents parse (String id, Path path);
  
}
