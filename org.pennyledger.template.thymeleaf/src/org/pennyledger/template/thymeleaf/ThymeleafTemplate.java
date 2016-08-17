package org.pennyledger.template.thymeleaf;

import java.io.Writer;
import java.net.URL;
import java.util.Locale;
import java.util.Set;

import org.pennyledger.template.ITemplate;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IWebContext;

public class ThymeleafTemplate implements ITemplate, IWebContext {

  private ThymeleafTemplateService templateService;
  private URL templateURL;
  private Context model;

  public ThymeleafTemplate(ThymeleafTemplateService templateService, URL templateURL) {
    this.templateService = templateService;
    this.templateURL = templateURL;
    this.model = new Context();
  }

  @Override
  public void setParameter(String name, Object value) {
    model.setVariable(name, value);
  }

  @Override
  public void merge(Writer writer) {
    templateService.process(templateURL, model, writer);
  }

  @Override
  public String merge() {
    return templateService.process(templateURL, model);
  }

}
