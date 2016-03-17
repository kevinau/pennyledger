package org.pennyledger.entity.impl;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;
import org.pennyledger.entity.IEntityManager;
import org.pennyledger.entity.IEntityManagerFactory;
import org.pennyledger.form.plan.IEntityPlan;

//@Component
public class EntityManagerFactory implements IEntityManagerFactory {

  private BundleContext bundleContext;
  private BundleTracker<Foo> bundleTracker;
  
  @Activate
  protected void activate (BundleContext bundleContext) {
    this.bundleContext = bundleContext;
    
    this.bundleTracker = new BundleTracker<>(context, Bundle.ACTIVE, 
        new BundleTrackerCustomizer<Foo>(){

                    @Override
                    public Foo addingBundle(Bundle bundle, BundleEvent event) {
                        //...
                    }

                    @Override
                    public void modifiedBundle(Bundle bundle, BundleEvent event, Foo foo) {
                        //...
                    }

                    @Override
                    public void removedBundle(Bundle bundle, BundleEvent event, Foo foo) {
                        //...
                    }
                });
  }
  
  
  @Deactivate
  protected void deactivate () {
  }
  
  @Override
  public <T> IEntityManager<T> createEntityManager(IEntityPlan<T> plan) {
    // TODO Auto-generated method stub
    return null;
  }

}
