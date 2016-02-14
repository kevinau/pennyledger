package org.pennyledger.form.test.model;

import java.util.List;

import org.pennyledger.form.model.FieldEventAdapter;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IObjectModel;

public class MemoryUI {

  private class UIControl extends FieldEventAdapter {
    
    private Object value;
    
    UIControl (IFieldModel fieldModel) {
      fieldModel.addFieldEventListener(this);
    }
    
    @Override
    public void valueChange(IFieldModel model) {
      Object value = model.getValue();
      boolean isSame = (this.value == null ? value == null : this.value.equals(value));
      if (!isSame) {
        this.value = value;
        model.setSourceFromValue(value);
      }
    }
  }
  
  
  public MemoryUI (IFormModel<?> form) {
    List<IObjectModel> objectModels = form.selectObjectModels("//*");
    for (IObjectModel objectModel : objectModels) {
      if (objectModel.isFieldModel()) {
        new UIControl((IFieldModel)objectModel);
      }
    }
  }
  
}
