package org.pennyledger.entity.impl;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;
import org.pennyledger.entity.IEntityRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(immediate = true)
public class EntityRegistry implements IEntityRegistry {

  private static final Logger logger = LoggerFactory.getLogger(EntityRegistry.class);

  // TODO remove these print methods.
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
  private Map<Long, List<Class<?>>> entityMap = new HashMap<>();


  @Activate
  public void activate(BundleContext context) throws Exception {
    System.out.println("Starting entity tracker");
    int trackStates = Bundle.ACTIVE | Bundle.STOPPING; // | Bundle.RESOLVED | Bundle.INSTALLED | Bundle.UNINSTALLED;
    bundleTracker = new EntityTracker(context, trackStates, null);
    bundleTracker.open();
    
    // Get all existing bundles, and check those for a PennyLedger-Entity header.
    Bundle[] bundles = context.getBundles();
    for (Bundle bundle : bundles) {
      if (bundle.getState() == Bundle.ACTIVE) {
        System.out.println("startup");
        print(bundle, null);
        checkForEntityHeader(bundle);
      }
    }
  }


  @Deactivate
  public void deactivate(BundleContext context) throws Exception {
    System.out.println("Stopping entity tracker");
    Long bundleId = context.getBundle().getBundleId();
    entityMap.remove(bundleId);
    
    bundleTracker.close();
    bundleTracker = null;
  }

  
  private Object checkForEntityHeader (Bundle bundle) {
    Dictionary<String, String> headers = bundle.getHeaders();
    String entityHeader = headers.get("PennyLedger-Entity");
    if (entityHeader != null) {
      logger.info("Found PennyLedger-Entity: {}", entityHeader);
      String[] entities = entityHeader.split(",");
      List<Class<?>> classList = new ArrayList<>(entities.length);
      
      for (String entityName : entities) {
        System.out.println(">>>>>>>>>>>>>>> " + entityName);
        try {
          Class<?> entityClass = bundle.loadClass(entityName);
          classList.add(entityClass);
          System.out.println(entityClass);
        } catch (ClassNotFoundException ex) {
          logger.warn("Bundle: " + bundle.getBundleId(), ex);
        }
      }
      System.out.println(bundle);
      System.out.println(classList);
      System.out.println(bundle.getBundleId());
      entityMap.put(bundle.getBundleId(), classList);
      return bundle;
    } else {
      return null;
    }
  }

  private static void print(Bundle bundle, BundleEvent event) {
    String symbolicName = bundle.getSymbolicName();
    if (symbolicName.startsWith("org.pennyledger.")) {
      String state = stateAsString(bundle);
      String type = typeAsString(event);
      System.out.println("[BT] " + symbolicName + ", state: " + state + ", event.type: " + type);
    }
  }

  private final class EntityTracker extends BundleTracker<Object> {

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

  @Override
  public List<Class<?>> getEntityClasses(String packagePrefix) {
    if (packagePrefix.endsWith("*")) {
      packagePrefix = packagePrefix.substring(0, packagePrefix.length() - 1);
    }
    List<Class<?>> entityClasses = new ArrayList<>();
    for (List<Class<?>> bundleClasses : entityMap.values()) {
      for (Class<?> entityClass : bundleClasses) {
        if (entityClass.getCanonicalName().startsWith(packagePrefix)) {
          entityClasses.add(entityClass);
        }
      }
    }
    return entityClasses;
  }
}
