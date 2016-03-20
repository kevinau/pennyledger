package org.pennyledger.entity;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.pennyledger.osgi.ComponentConfiguration;
import org.pennyledger.osgi.Configurable;

@Component(configurationPolicy=ConfigurationPolicy.REQUIRE)
public class EntitySet implements IEntitySet {

  private static class MappingEntry {
    private final String classPrefix;
    private final String databaseName;
    private final String schema;
    
    private MappingEntry (String classPrefix, String databaseName, String schema) {
      this.classPrefix = classPrefix;
      this.databaseName = databaseName;
      this.schema = schema;
    }
  }
  
  private ComponentContext context;
  
  private String name;
  
  private List<MappingEntry> entityDbMapping;
  
  @Activate
  protected void activate (ComponentContext context) {
    this.context = context;
    
    // The properties of this component are too complex to use ComponentConfiguration.load
    Dictionary<String, Object> dict = context.getProperties();
    for (Enumeration<String> e = dict.keys(); e.hasMoreElements(); ) {
      String key = e.nextElement();
      String value = dict.get(key).toString();
      if (key.equals("name")) {
        this.name = value;
      } else {
        String databaseName, schema;
        
        int n = value.indexOf('.');
        if (n == -1) {
          databaseName = value;
          schema = null;
        } else {
          databaseName = value.substring(0, n);
          schema = value.substring(n + 1);
        }
        entityDbMapping.add(new MappingEntry(key, databaseName, schema));
      }
    }
  }

  
  @Deactivate
  protected void deactivate () {
    this.context = null;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public List<String> getClassNamePrefixes() {
    return cnprefix;
  }

}
