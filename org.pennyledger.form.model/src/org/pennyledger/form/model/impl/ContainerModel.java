package org.pennyledger.form.model.impl;

import org.pennyledger.form.model.IContainerModel;
import org.pennyledger.form.model.IForm;
import org.pennyledger.form.reflect.IContainerReference;

public class ContainerModel<T> extends ObjectModel<T> implements IContainerModel<T> { 

  public ContainerModel(IForm<?> form, IContainerReference parent) {
    super(form, parent);
  }

}
