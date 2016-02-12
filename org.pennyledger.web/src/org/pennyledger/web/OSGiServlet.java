package org.pennyledger.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
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
    
    // Build new dictionary for servlets
    ////Dictionary<String, String> initProps = new Hashtable<String, String>();
    ////for (Map.Entry<String, Object> entry : props.entrySet()) {
    ////  initProps.put(entry.getKey(), entry.getValue().toString());
    ////}
    
    // Register the servlet
    logger.info("Registering servlet: {}, {}", alias, this.getClass().getSimpleName());
    try {
      httpService.registerServlet(alias, this, context.getProperties(), null);
      System.err.println("/about registered");
    } catch (ServletException | NamespaceException ex) {
      throw new RuntimeException(ex);
    }  
  }

  
  protected void deactivate() {
    httpService.unregister(alias);
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

  
  public File getResourceFile (String resourcePath) throws FileNotFoundException {
    File resourceFile = null;
    Bundle bundle = bundleContext.getBundle();
    URL resourceURL = bundle.getResource(resourcePath);
    if (resourceURL == null) {
      throw new FileNotFoundException(resourcePath);
    }
    try {
      resourceFile = new File(resourceURL.toURI());
    } catch (URISyntaxException ex) {
      throw new RuntimeException(ex);
    }
    return resourceFile;
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


  public static void writeResourceFile (HttpServletResponse response, File resourceFile) throws ServletException {
    try {
      if (!resourceFile.exists()) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return;
      }
      InputStream inputStream = new FileInputStream(resourceFile);
      
      String resourceName = resourceFile.getName();
      int j = resourceName.lastIndexOf('.');
      String extn = resourceName.substring(j);
      MimeType mimeType = mimeTypes.get(extn);
      if (mimeType != null) {
        response.setContentType(mimeType.getMimeCode());
//      int expiry = mimeType.getExpiry();
//      if (cacheControl && expiry != 0) {
//        ///response.setHeader("Cache-Control", "max-age=" + expiry);
//      }
      }
      
      int fileLength = (int)resourceFile.length();
//      int written = 0;
      response.setContentLength(fileLength);
      
      OutputStream out = response.getOutputStream();
      byte[] buffer = new byte[BUFFER_SIZE];
      int n = inputStream.read(buffer);
      while (n > 0) {
        out.write(buffer, 0, n);
//          written += n;
        n = inputStream.read(buffer);
      }
      out.flush();
      inputStream.close();

      response.setStatus(HttpServletResponse.SC_OK);
      return;
    } catch (FileNotFoundException ex) {
      // Do nothing.  Let the standard servlet handle it
    } catch (IOException ex) {
      throw new ServletException(ex);
    }
  }

  
  @Override
  public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    if (request.getMethod().equals("GET")) {
      String pathInfo = request.getPathInfo();
      if (pathInfo != null && pathInfo.startsWith("/resources/")) {
        // It is a resource type
        String resourceName = pathInfo.substring(1);
        File resourceFile = getResourceFile(resourceName);
        writeResourceFile (response, resourceFile);
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
