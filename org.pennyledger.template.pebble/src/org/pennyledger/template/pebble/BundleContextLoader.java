package org.pennyledger.template.pebble;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.mitchellbosecke.pebble.error.LoaderException;
import com.mitchellbosecke.pebble.loader.Loader;

public class BundleContextLoader implements Loader<String> {

  private final BundleContext bundleContext;
  private String charset = StandardCharsets.UTF_8.name();
  
  public BundleContextLoader (BundleContext bundleContext) {
    this.bundleContext = bundleContext;
  }
  
  @Override
  public String createCacheKey(String templatePath) {
    return templatePath;
  }

  @Override
  public Reader getReader(String templatePath) throws LoaderException {
    Bundle bundle = bundleContext.getBundle();
    
    URL templateURL = bundle.getResource(templatePath);
    if (templateURL == null) {
      throw new IllegalArgumentException("Template '" + templatePath + "' not found in bundle: " + bundle.getSymbolicName());
    }
    BufferedReader reader;
    try {
      InputStream is = templateURL.openStream();
      InputStreamReader isr = new InputStreamReader(is, charset);
      reader = new BufferedReader(isr);
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
    return reader;
  }

  @Override
  public String resolveRelativePath(String arg0, String arg1) {
    throw new RuntimeException("Relative paths cannot be used in the BundleContextLoader");
  }

  @Override
  public void setCharset(String charset) {
    this.charset = charset;
  }

  @Override
  public void setPrefix(String arg0) {
    throw new RuntimeException("Prefixes cannot be used in the BundleContextLoader");
  }

  @Override
  public void setSuffix(String arg0) {
    throw new RuntimeException("Suffixes cannot be used in the BundleContextLoader");
  }

}
