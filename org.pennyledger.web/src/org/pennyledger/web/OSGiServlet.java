package org.pennyledger.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.pennyledger.osgi.ComponentConfiguration;
import org.pennyledger.osgi.Configurable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OSGiServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private static final int BUFFER_SIZE = 1024 * 16;
  
  private static final Map<String, MimeType> mimeTypes = new HashMap<String, MimeType>();

  static {
    mimeTypes.put(".js", new MimeType("text/javascript", 0));
    mimeTypes.put(".css", new MimeType("text/css", 0));
    mimeTypes.put(".pdf", new MimeType("application/pdf", 0));
  }
  
  
  private static final Logger logger = LoggerFactory.getLogger(OSGiServlet.class);
  
  private BundleContext bundleContext;

  private HttpService httpService;

//  private boolean cacheControl = true;

  @Configurable(required=true)
  private String alias;
  
  @Configurable
  private String resourcesDir = "/resources";
  
  protected void setHttpService(HttpService httpService) {
    // HTTP service is available, register our servlet...
    this.httpService = httpService;
  }
  
  
  protected void unsetHttpService(HttpService httpService) {
    httpService = null;
  }
  
  
  protected void activate (ComponentContext context, String defaultAlias) {
    this.alias = defaultAlias;
    ComponentConfiguration.load(this, context);
    
    if (!alias.startsWith("/")) {
      throw new RuntimeException("'alias' property must start with an slash (/)");
    }
    logger.info("Registering servlet: {}, {}", alias, this.getClass().getSimpleName());
    
    bundleContext = context.getBundleContext();
    
    // Build new dictionary for servlets
    ////Dictionary<String, String> initProps = new Hashtable<String, String>();
    ////for (Map.Entry<String, Object> entry : props.entrySet()) {
    ////  initProps.put(entry.getKey(), entry.getValue().toString());
    ////}
    
    // Register the servlet
    try {
      httpService.registerServlet(alias, this, context.getProperties(), null);
      logger.info("Resistration OK: {}, {}", alias, this.getClass().getSimpleName());
    } catch (ServletException | NamespaceException ex) {
      throw new RuntimeException(ex);
    }  
  }

  
  protected void deactivate() {
    httpService.unregister(alias);
    bundleContext = null;
  }


  @Deprecated
  protected String getInstanceName (Map<String, Object> props) {
    String configFileName = (String)props.get("felix.fileinstall.filename");
    if (configFileName != null) {
      int n1 = configFileName.lastIndexOf('-');
      int n2 = configFileName.lastIndexOf('.');
      return configFileName.substring(n1 + 1, n2);
    } else {
      return null;
    }
  }

  
  public InputStream getResourceFile (String resourcePath) throws FileNotFoundException {
    InputStream resourceStream = null;
    Bundle bundle = bundleContext.getBundle();
    URL resourceURL = bundle.getResource(resourcePath);
    if (resourceURL == null) {
      throw new FileNotFoundException(resourcePath);
    }
    try {
      int n = resourceURL.openConnection().getContentLength();
      System.out.println(resourcePath + "...................." + n);
      resourceStream = resourceURL.openStream();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    return resourceStream;
  }
  
  
//  private void loadMimeTypes () throws FileNotFoundException {
//    InputStream inputStream = OSGiServlet.class.getResourceAsStream("mimetypes.txt");
//    if (inputStream == null) {
//      throw new FileNotFoundException("mimetypes.txt");
//    }
//    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//    try {
//      String line = reader.readLine();
//      while (line != null) {
//        String[] fields = line.split("[\t ]+");
//        int expiry = Integer.parseInt(fields[2]);
//        MimeType mimeType = new MimeType(fields[0], expiry);
//        mimeTypes.put(fields[1], mimeType);
//        line = reader.readLine();
//      }
//      reader.close();
//    } catch (NumberFormatException ex) {
//      throw new RuntimeException(ex);
//    } catch (IOException ex) {
//      throw new RuntimeException(ex);
//    }
//  }

  
//  @Override
//  public void init(ServletConfig config) throws ServletException {
//    super.init(config);
//    try {
//      loadMimeTypes();
//    } catch (FileNotFoundException ex) {
//      throw new ServletException(ex);
//    }
//  }


  public void writeResourceFile (HttpServletResponse response, String resourcePath) throws ServletException {
    Bundle bundle = bundleContext.getBundle();
    URL resourceURL = bundle.getResource(resourcePath);
    if (resourceURL == null) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    try (InputStream resourceStream = resourceURL.openStream()) {
      int j = resourcePath.lastIndexOf('.');
      String extn = resourcePath.substring(j);
      MimeType mimeType = mimeTypes.get(extn);
      if (mimeType != null) {
        response.setContentType(mimeType.getMimeCode());
//      int expiry = mimeType.getExpiry();
//      if (cacheControl && expiry != 0) {
//        ///response.setHeader("Cache-Control", "max-age=" + expiry);
//      }
      }
      
      int fileLength = resourceURL.openConnection().getContentLength();
      response.setContentLength(fileLength);
      
      OutputStream out = response.getOutputStream();
      byte[] buffer = new byte[BUFFER_SIZE];
      int n = resourceStream.read(buffer);
      while (n > 0) {
        out.write(buffer, 0, n);
        n = resourceStream.read(buffer);
      }
      out.flush();
      response.setStatus(HttpServletResponse.SC_OK);
    } catch (IOException ex) {
      throw new ServletException(ex);
    }
  }

  
  @Override
  public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (request.getMethod().equals("GET")) {
      String pathInfo = request.getPathInfo();
      if (pathInfo != null && pathInfo.startsWith("/resources/")) {
        String resourcePath = pathInfo.substring(1);
        writeResourceFile (response, resourcePath);
        return;
      }
    }
    // Let the standard servlet handle it
    super.service(request, response);
  }
  
  
  public String getAlias () {
    return alias;
  }
    
}
