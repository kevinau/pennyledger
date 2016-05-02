package org.pennyledger.entitydb;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.pennyledger.osgi.IllegalConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(configurationPolicy=ConfigurationPolicy.REQUIRE, immediate=true)
public class EntityDatabaseMapping implements IEntityDatabaseMapping {

  private static final Logger logger = LoggerFactory.getLogger(EntityDatabaseMapping.class);
  
  //private ComponentContext context;
  
  private String name;
  
  private List<MappingEntry> entityDbMapping = new ArrayList<>();
  
  @Activate
  protected void activate (ComponentContext context) {
    //this.context = context;
    
    // The properties of this component are too complex to use ComponentConfiguration.load
    Dictionary<String, Object> dict = context.getProperties();
    logger.info("Activating entity-database mapping, for name='{}'", dict.get("name"));

    for (Enumeration<String> e = dict.keys(); e.hasMoreElements(); ) {
      String key = e.nextElement();
      String value = dict.get(key).toString();
      if (key.equals("name")) {
        this.name = value;
      } else if (key.startsWith("mapping.")){
        String databaseName, schema;
        
        int n = value.indexOf('.');
        if (n == -1) {
          databaseName = value;
          schema = null;
        } else {
          databaseName = value.substring(0, n);
          schema = value.substring(n + 1);
        }
        entityDbMapping.add(new MappingEntry(key.substring(8), databaseName, schema));
      }
    }
    if (this.name == null) {
      throw new IllegalConfigurationException("No 'name' entry in ...");
    }
    logger.info("Found {} entity-database mappings", entityDbMapping.size());
  }

  
  @Deactivate
  protected void deactivate () {
    logger.info("Deactivating entity-database mapping named '{}'", name);
    //this.context = null;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public List<MappingEntry> getEntityMappings() {
    return entityDbMapping;
  }

}
