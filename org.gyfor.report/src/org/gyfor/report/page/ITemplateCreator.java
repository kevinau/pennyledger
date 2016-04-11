package org.gyfor.report.page;

import java.util.Properties;


public interface ITemplateCreator {

  public IPageTemplate createTemplate (IPageWriter pw, Properties properties);
  
}
