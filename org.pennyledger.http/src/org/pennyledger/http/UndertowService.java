package org.pennyledger.http;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.undertow.predicate.Predicates;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PredicateHandler;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.server.handlers.resource.ResourceManager;


public class UndertowService implements BundleActivator {

  private Logger log = LoggerFactory.getLogger(UndertowService.class);

  private UndertowWebServer webServer;


  @Override
  public void start(BundleContext bundleContext) throws Exception {
    webServer = new UndertowWebServer("localhost", 8080);
    webServer.start();

    ServiceListener sl = new ServiceListener() {
      @Override
      public void serviceChanged(ServiceEvent ev) {
        ServiceReference<?> sr = ev.getServiceReference();
        switch (ev.getType()) {
        case ServiceEvent.REGISTERED: {
          try {
            register(bundleContext, sr);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
        }
      }
    };

    String filter = "(objectclass=" + HttpHandler.class.getName() + ")";

    try {
      bundleContext.addServiceListener(sl, filter);
      ServiceReference<?>[] srl = bundleContext.getServiceReferences((String)null, filter);
      for (int i = 0; srl != null && i < srl.length; i++) {
        register(bundleContext, srl[i]);
      }
    } catch (InvalidSyntaxException e) {
      e.printStackTrace();
    }
  }


  private void register(BundleContext bundleContext, ServiceReference<?> sr) {
    HttpHandler handler = (HttpHandler)bundleContext.getService(sr);
    if (handler == null) {
      throw new IllegalArgumentException("Handler for dynamic web page, or static resource, is null");
    }

    Map<String, HttpHandler> handlerSet = new HashMap<>();
    String context = "";

    Class<?> klass = handler.getClass();
    Context contextAnn = klass.getAnnotation(Context.class);
    if (contextAnn != null) {
      context = contextAnn.value();
      if (!context.startsWith("/")) {
        throw new IllegalArgumentException("@Context 'path' must start with a slash (/)");
      }
      handlerSet.put(context, handler);
    }

    Resource[] resourcesAnn = klass.getAnnotationsByType(Resource.class);
    for (Resource resourceAnn : resourcesAnn) {
      String[] extensions = resourceAnn.extensions();
      if (extensions.length == 0) {
        String subPath = resourceAnn.path();
        if (subPath.length() > 0 && !subPath.startsWith("/")) {
          throw new IllegalArgumentException("@Resource 'path' must start with a slash (/)");
        }
        String locationBase = resourceAnn.base();
        String resourceContext = context + subPath;
        log.info("Adding resource: sub-path {}, extensions {}, based at {}", subPath, extensions, locationBase);

        HttpHandler nextHandler = handlerSet.get(resourceContext);
        if (nextHandler == null) {
          nextHandler = PageNotFoundHandler.instance;
        }
        ResourceManager resourceManager = new BundleResourceManager(bundleContext.getBundle(), locationBase);
        HttpHandler resourceHandler = new ResourceHandler(resourceManager, nextHandler);
        handlerSet.put(resourceContext, resourceHandler);
      }
    }
    for (Resource resourceAnn : resourcesAnn) {
      String[] extensions = resourceAnn.extensions();
      if (extensions.length > 0) {
        String subPath = resourceAnn.path();
        String locationBase = resourceAnn.base();
        String resourceContext = context + subPath;
        log.info("Adding resource: sub-path {}, extensions {}, based at {}", subPath, extensions, locationBase);

        HttpHandler nextHandler = handlerSet.get(resourceContext);
        if (nextHandler == null) {
          nextHandler = PageNotFoundHandler.instance;
        }
        ResourceManager resourceManager = new BundleResourceManager(bundleContext.getBundle(), locationBase);
        HttpHandler resourceHandler = new ResourceHandler(resourceManager, PageNotFoundHandler.instance);
        HttpHandler predicateHandler = new PredicateHandler(Predicates.suffixes(extensions), resourceHandler, nextHandler);
        handlerSet.put(resourceContext, predicateHandler);
      }
    }
    for (Map.Entry<String, HttpHandler> entry : handlerSet.entrySet()) {
      context = entry.getKey();
      handler = entry.getValue();
      log.info("Registering: {} using {}", context, handler);
      webServer.register(context, handler);
    }
  }


  private void unregister(BundleContext bundleContext, ServiceReference<?> sr) {
    HttpHandler handler = (HttpHandler)bundleContext.getService(sr);
    if (handler == null) {
      throw new IllegalArgumentException("Handler for dynamic web page, or static resource, is null");
    }

    Class<?> klass = handler.getClass();

    String urlPath;
    Context webDynamicAnn = klass.getAnnotation(Context.class);
    if (webDynamicAnn != null) {
      urlPath = webDynamicAnn.value();
      if (!urlPath.startsWith("/")) {
        throw new IllegalArgumentException("@WebContext path must start with a slash (/)");
      }
    } else {
      Resource webStaticAnn = klass.getAnnotation(Resource.class);
      if (webStaticAnn != null) {
        urlPath = webStaticAnn.path();
        if (!urlPath.startsWith("/")) {
          throw new IllegalArgumentException("@WebResource path must start with a slash (/)");
        }
      } else {
        throw new IllegalArgumentException("No @UWebContext or @WebResource annotation on " + klass.getName());
      }
    }
    webServer.unregister(urlPath);
  }


  @Override
  public void stop(BundleContext bundleContext) throws Exception {
    String filter = "(objectclass=" + HttpHandler.class.getName() + ")";

    ServiceReference<?>[] srl = bundleContext.getServiceReferences((String)null, filter);
    for (int i = 0; srl != null && i < srl.length; i++) {
      unregister(bundleContext, srl[i]);
    }
    webServer.stop();
  }

}
