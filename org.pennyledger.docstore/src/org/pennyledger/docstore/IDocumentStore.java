package org.pennyledger.docstore;

import java.io.InputStream;
import java.nio.file.Path;

import org.apache.tika.mime.MimeType;

public interface IDocumentStore {

  public String importDocument (Path path);
  
  public String importDocument (InputStream is, MimeType mimeType);
  
  public Path getSourcePath(String id);
  
}
