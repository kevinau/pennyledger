package org.pennyledger.docstore;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.tika.mime.MimeType;

public interface IDocumentStore {

  public String importDocument (Path path);
  
  public String importDocument (InputStream is, MimeType mimeType);
  
  public IDocumentContents getContents (String id);
  
  public Path getSourcePath(String id);
  
  public Path getContentsPath(String id);

  public void saveViewImage(String id, BufferedImage image);
  
  public Path getViewImagePath(String id);

  /** 
   * For testing only.
   */
  public Path getViewHTMLPath(String id);

}
