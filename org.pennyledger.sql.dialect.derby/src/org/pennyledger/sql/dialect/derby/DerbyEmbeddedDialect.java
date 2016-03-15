package org.pennyledger.sql.dialect.derby;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.pennyledger.osgi.ComponentConfiguration;
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

  /**
   * The path under which Derby creates all its files.  If this path is not configured,
   * it defaults to {user.home}/derby.
   */
  @Configurable
  private Path derbyDir;
  
  @Activate 
  protected void activate (ComponentContext context) {
    ComponentConfiguration.load(this, context);
  }
  
  @Deactivate
  protected void deactivate (ComponentContext context) {
    derbyDir = null;
  }
  
  @Override
  public String getName () {
    return "DerbyEmbedded";
  }
  
  @Override
  public String getURLTemplate () {
    if (derbyDir == null) {
      derbyDir = Paths.get(System.getProperty("user.home"), "derby");
//    if (!Files.exists(derbyDir)) {
//      try {
//        Files.createDirectories(derbyDir);
//      } catch (IOException ex) {
//        throw new UncheckedIOException(ex);
//      }
//    }
    }
    return "jdbc:derby:directory:" + derbyDir + "/{1};create=true";
  }
  
}
