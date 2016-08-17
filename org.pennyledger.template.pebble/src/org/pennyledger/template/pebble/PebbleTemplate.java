package org.pennyledger.template.pebble;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.pennyledger.template.ITemplate;

import com.mitchellbosecke.pebble.error.PebbleException;

public class PebbleTemplate implements ITemplate {

  private com.mitchellbosecke.pebble.template.PebbleTemplate compiledTemplate;
  private Map<String, Object> model;

  
  public PebbleTemplate(com.mitchellbosecke.pebble.template.PebbleTemplate compiledTemplate) {
    this.compiledTemplate = compiledTemplate;
    this.model = new HashMap<>();
  }

  @Override
  public void setParameter(String name, Object value) {
    model.put(name, value);
  }

  @Override
  public void merge(Writer writer) {
    try {
      compiledTemplate.evaluate(writer, model);
    } catch (PebbleException | IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public String merge() {
    StringWriter writer = new StringWriter();
    try {
      compiledTemplate.evaluate(writer, model);
    } catch (PebbleException | IOException ex) {
      throw new RuntimeException(ex);
    }
    return writer.toString();
  }

}
