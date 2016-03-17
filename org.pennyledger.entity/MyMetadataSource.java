package org.pennyledger.jpa;

import java.util.Map;

import org.eclipse.persistence.internal.jpa.metadata.xml.XMLEntityMappings;
import org.eclipse.persistence.jpa.metadata.MetadataSourceAdapter;
import org.eclipse.persistence.logging.SessionLog;

public class MyMetadataSource extends MetadataSourceAdapter {

  @Override
  public XMLEntityMappings getEntityMappings(Map<String, Object> properties, ClassLoader classLoader, SessionLog log) {
    log.fine("########################## " + properties.size());
    for (String key : properties.keySet()) {
      Object value = properties.get(key);
      if (value instanceof String) {
        log.fine("########################## " + key + " = " + value.toString());
      } else {
        log.fine("########################## " + key + " = " + value.getClass());
      }
    }
    //log.fine("########################## " + properties);
    log.fine("########################## " + classLoader.getClass());
    return new XMLEntityMappings();
  }

  @Override
  public Map<String, Object> getPropertyOverrides(Map<String, Object> properties, ClassLoader classLoader, SessionLog log) {
    // TODO Auto-generated method stub
    return properties;
  }

}
