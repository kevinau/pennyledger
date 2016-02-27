package org.pennyledger.form.model.impl;

import java.lang.reflect.Field;

import org.pennyledger.form.model.IClassModel;
import org.pennyledger.form.model.IForm;
import org.pennyledger.form.model.IObjectModel;
import org.pennyledger.form.reflect.IContainerReference;
import org.pennyledger.form.value.impl.DualAccessMap;

public class ClassModel<T> extends ContainerModel<T> {

  private final IForm<?> form;
  private final IContainerReference parent;
  
  private IClassModel<T> model;
  
  private DualAccessMap<String, IObjectModel<?>> memberModels = new DualAccessMap<>();

  public ClassModel(IForm<?> form, IContainerReference parent) {
    super(form, parent);
    this.form = form;
    this.parent = parent;
  }

  @Override
  public void setValue(T newValue) {
    T oldValue = getValue();
    if (oldValue == null) {
      if (newValue == null) {
        // Do nothing
      } else {
        // Build the class model based on the new value, firing creation events as we go.
        parent.setValue(newValue);
        model = createClassModel(newValue);
      }
    }
    // TODO Auto-generated method stub
    
  }

  private IClassModel<T> createClassModel (T value) {
    Class<?> klass = value.getClass();
    Field[] fields = klass.getDeclaredFields();
    for (Field field : fields) {
      Object fieldValue = field.get(value);
      IObjectModel<?> fieldModel = createObjectModel(fieldValue);
      memberModels.put(fieldName, fieldModel);
    }
  }
}
