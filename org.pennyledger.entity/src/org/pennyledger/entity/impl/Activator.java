package org.pennyledger.entity.impl;

import java.util.Dictionary;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//@Component(immediate=true)
public class Activator {

  private static final Logger logger = LoggerFactory.getLogger(Activator.class);


  private static String stateAsString(Bundle bundle) {
    if (bundle == null) {
      return "null";
    }
    int state = bundle.getState();
    switch (state) {
    case Bundle.ACTIVE:
      return "ACTIVE";
    case Bundle.INSTALLED:
      return "INSTALLED";
    case Bundle.RESOLVED:
      return "RESOLVED";
    case Bundle.STARTING:
      return "STARTING";
    case Bundle.STOPPING:
      return "STOPPING";
    case Bundle.UNINSTALLED:
      return "UNINSTALLED";
    default:
      return "unknown bundle state: " + state;
    }
  }


  private static String typeAsString(BundleEvent event) {
    if (event == null) {
      return "null";
    }
    int type = event.getType();
    switch (type) {
    case BundleEvent.INSTALLED:
      return "INSTALLED";
    case BundleEvent.LAZY_ACTIVATION:
      return "LAZY_ACTIVATION";
    case BundleEvent.RESOLVED:
      return "RESOLVED";
    case BundleEvent.STARTED:
      return "STARTED";
    case BundleEvent.STARTING:
      return "Starting";
    case BundleEvent.STOPPED:
      return "STOPPED";
    case BundleEvent.UNINSTALLED:
      return "UNINSTALLED";
    case BundleEvent.UNRESOLVED:
      return "UNRESOLVED";
    case BundleEvent.UPDATED:
      return "UPDATED";
    default:
      return "unknown event type: " + type;
    }
  }

  private EntityTracker bundleTracker;


  @Activate
  public void activate(BundleContext context) throws Exception {
    System.out.println("Starting Bundle Tracker");
    int trackStates = Bundle.STARTING | Bundle.STOPPING | Bundle.RESOLVED | Bundle.INSTALLED | Bundle.UNINSTALLED;
    bundleTracker = new EntityTracker(context, trackStates, null);
    bundleTracker.open();
    
    // Get all existing bundles, and check those for a PennyLedger-Entity header.
    Bundle[] bundles = context.getBundles();
    for (Bundle bundle : bundles) {
      System.out.println("startup");
      print(bundle, null);
      checkForEntityHeader(bundle);
    }
  }


  @Deactivate
  public void deactivate(BundleContext context) throws Exception {
    System.out.println("Stopping Bundle Tracker");
    bundleTracker.close();
    bundleTracker = null;
  }

  
  private static Object checkForEntityHeader (Bundle bundle) {
    Dictionary<String, String> headers = bundle.getHeaders();
    String entityHeader = headers.get("PennyLedger-Entity");
    if (entityHeader != null) {
      String[] entities = entityHeader.split(",");
      for (String entityName : entities) {
        System.out.println(">>>>>>>>>>>>>>> " + entityName);
        try {
          Class<?> entityClass = bundle.loadClass(entityName);
          System.out.println(entityClass);
        } catch (ClassNotFoundException ex) {
          logger.warn("Bundle: " + bundle.getBundleId(), ex);
        }
      }
      return bundle;
    } else {
      return null;
    }
  }

  private static void print(Bundle bundle, BundleEvent event) {
    String symbolicName = bundle.getSymbolicName();
    String state = stateAsString(bundle);
    String type = typeAsString(event);
    System.out.println("[BT] " + symbolicName + ", state: " + state + ", event.type: " + type);
  }

  private static final class EntityTracker extends BundleTracker<Object> {

    public EntityTracker(BundleContext context, int stateMask, BundleTrackerCustomizer<Object> customizer) {
      super(context, stateMask, customizer);
    }


    @Override
    public Object addingBundle(Bundle bundle, BundleEvent event) {
      System.out.println("addingBundle");
      print(bundle, event);
      return checkForEntityHeader(bundle);
    }


    @Override
    public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
      System.out.println("removedBundle");
      print(bundle, event);
      checkForEntityHeader(bundle);
    }


    @Override
    public void modifiedBundle(Bundle bundle, BundleEvent event, Object object) {
      System.out.println("modifiedBundle");
      print(bundle, event);
      checkForEntityHeader(bundle);
    }
  }
}
