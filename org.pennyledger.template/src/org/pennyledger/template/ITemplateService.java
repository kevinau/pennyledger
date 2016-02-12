package org.pennyledger.template;

import org.osgi.framework.BundleContext;

public interface ITemplateService {

  public ITemplate createTemplate (BundleContext bundleContext, String templateName);
    
}
