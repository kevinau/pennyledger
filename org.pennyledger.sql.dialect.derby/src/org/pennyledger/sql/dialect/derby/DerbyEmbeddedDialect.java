package org.pennyledger.sql.dialect.derby;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.pennyledger.fs.BaseDirectory;
import org.pennyledger.osgi.Configurable;
import org.pennyledger.sql.dialect.IDialect;


/**
 * An implementation of IDialect for the Derby database.
 * 
 * @author Kevin Holloway
 * 
 */
@Component(property={"dialectName:String=DerbyEmbedded"}, service=IDialect.class)
public class DerbyEmbeddedDialect extends DerbyServerDialect {

  @Configurable
  private BaseDirectory baseDirectory;
  
  private Path derbyDir;
  
  @Reference
  public void setBaseDirectory (BaseDirectory baseDirectory) {
    this.baseDirectory = baseDirectory;
  }
  
  
  public void unsetDataLocation (BaseDirectory baseDirectory) {
    this.baseDirectory = null;
  }
  
  
  @Activate 
  protected void activate () {
    try {
      derbyDir = baseDirectory.resolve("derby");
      Files.createDirectories(derbyDir);
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }
  
  
  @Override
  public String getName () {
    return "Embedded";
  }
  
  @Override
  public String getURLTemplate () {
    return "jdbc:derby:directory:" + derbyDir + "/{1};create=true";
  }
  
}
