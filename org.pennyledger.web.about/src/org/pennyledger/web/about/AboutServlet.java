package org.pennyledger.web.about;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.http.HttpService;
import org.pennyledger.template.ITemplate;
import org.pennyledger.template.ITemplateService;
import org.pennyledger.web.OSGiServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Component(service=AboutServlet.class, configurationPolicy=ConfigurationPolicy.REQUIRE, immediate=true)
public class AboutServlet extends OSGiServlet {

  private static final long serialVersionUID = 1L;

  private static final Logger logger = LoggerFactory.getLogger(AboutServlet.class);
  
  private ITemplateService templateService;
  
  private ITemplate template;
  
  @Override
  @Reference
  protected void setHttpService(HttpService httpService) {
    super.setHttpService (httpService);
  }
  
  
  @Override
  protected void unsetHttpService(HttpService httpService) {
    super.unsetHttpService (httpService);
  }
  
  
  @Reference
  protected void setTemplateService (ITemplateService templateService) {
    System.out.println("===================>>> setTemplateServicefacgtory " + templateService.getClass());
    this.templateService = templateService;
  }
  
  
  protected void unsetTemplateService (ITemplateService templateService) {
    this.templateService = null;
  }


  @Activate
  public void activate (ComponentContext componentContext) {
    super.activate(componentContext, "/about");
 
    BundleContext bundleContext = componentContext.getBundleContext();
    template = templateService.createTemplate(bundleContext, "templates/about.html");

    Bundle bundle = componentContext.getBundleContext().getBundle();
    URL resourceURL = bundle.getResource("/");
    logger.info("Template resource root {}", resourceURL);
    logger.info("Template class {}", this.getClass().getName());
  }

  
  @Override
  @Deactivate
  public void deactivate () {
    super.deactivate();
  }

  
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (template == null) {
      throw new RuntimeException("No template for this servlet");
    }
    
    template.setParameter("docbase", getAlias().substring(1) + "/");
    Map<String, String> requests = new HashMap<>();
    for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements(); ) {
      String name = e.nextElement();
      String value = request.getParameter(name);
//      response.getWriter().println("---------" + name + " = " + value);
      requests.put(name, value + "");
    }
    template.setParameter("requestParams", requests);
//    template.setParameter("request", request);
    template.merge(response.getWriter());
    response.setStatus(HttpServletResponse.SC_OK);
  }

}
