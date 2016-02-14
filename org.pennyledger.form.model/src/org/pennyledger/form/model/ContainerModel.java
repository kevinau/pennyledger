package org.pennyledger.form.model;

import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.util.FastAccessList;


public abstract class ContainerModel extends ObjectModel implements IContainerModel {

  private final FastAccessList<ContainerEventListener> containerEventListenerList = new FastAccessList<ContainerEventListener>(ContainerEventListener.class);

  
  public ContainerModel(IFormModel<?> ownerForm, IContainerModel parentModel, Object parentRef, String pathName, IObjectPlan objectPlan) {
    super(ownerForm, parentModel, parentRef, pathName, objectPlan);
  }


  @Override
  public void addContainerEventListener (ContainerEventListener x) {
    containerEventListenerList.add(x);
  }

  
  @Override
  public void removeContainerEventListener (ContainerEventListener x) {
    containerEventListenerList.remove(x);
  }
  
  
  @Override
  public void fireContainerCreate (IContainerModel eventSource) {
    for (ContainerEventListener x : containerEventListenerList) {
      x.containerCreate(eventSource);
    }
    if (parentModel != null) {
      parentModel.fireContainerCreate(eventSource);
    }
  }

  
//  @Override
//  public void fireContainerAdd (IObjectModel eventSource) {
//    for (ContainerEventListener x : containerEventListenerList) {
//      x.containerAdd(eventSource);
//    }
//    if (parentModel != null) {
//      parentModel.fireContainerAdd(eventSource);
//    }
//  }

  
  @Override
  public void fireContainerDestroy (IContainerModel eventSource) {
    for (ContainerEventListener x : containerEventListenerList) {
      x.containerDestroy(eventSource);
    }
    if (parentModel != null) {
      parentModel.fireContainerDestroy(eventSource);
    }
  }

  
  @Override
  public void fireContainerOccursReduced (IContainerModel eventSource) {
    for (ContainerEventListener x : containerEventListenerList) {
      x.containerOccursReduced(eventSource);
    }
    if (parentModel != null) {
      parentModel.fireContainerOccursReduced(eventSource);
    }
  }

  
  @Override
  public void setEventsActive (boolean fireEvents) {
    super.setEventsActive(fireEvents);
    for (IObjectModel childModel : getChildren()) {
      childModel.setEventsActive(fireEvents);
    }
  }
  
  
  @Override
  public boolean isContainerModel() {
    return true;
  }
  
}
