package org.pennyledger.form.test.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.model.FieldEventAdapter;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;

public class SetValueFromSourceTest {

  private static class TestClass {
    Integer field0 = 0;

    int field1 = 123;
  }

  private IFormModel<TestClass> formModel;
  private TestClass instance;
  private ValueChangeCountListener x;

  private static class ValueChangeCountListener extends FieldEventAdapter {

    private int changeCount = 0;
    private Object lastValue;

    @Override
    public void valueChange(IFieldModel model) {
      changeCount++;
      lastValue = model.getValue();
    }

  }

  @Before
  public void setup() {
    instance = new TestClass();
    formModel = new FormModel<TestClass>(instance);
    x = new ValueChangeCountListener();
  }

  @Test
  public void noDefaultSpecified() {
    IFieldModel fieldModel = formModel.selectFieldModel("/form/field0");
    Assert.assertEquals(new Integer(0), fieldModel.getDefaultValue());
    Assert.assertEquals(new Integer(0), fieldModel.getValue());
    Assert.assertEquals(new Integer(0), instance.field0);
    fieldModel.addFieldEventListener(x);
    Assert.assertEquals(1, x.changeCount);

    fieldModel.setValueFromSource("456");
    Assert.assertEquals(new Integer(456), fieldModel.getValue());
    Assert.assertEquals(new Integer(456), instance.field0);
    Assert.assertEquals(2, x.changeCount);
  }
  
  
  @Test
  public void defaultSpecified() {
    IFieldModel fieldModel = formModel.selectFieldModel("/form/field1");
    Assert.assertEquals(new Integer(123), fieldModel.getDefaultValue());
    Assert.assertEquals(new Integer(123), fieldModel.getValue());
    Assert.assertEquals(123, instance.field1);
    fieldModel.addFieldEventListener(x);
    Assert.assertEquals(1, x.changeCount);

    fieldModel.setValueFromSource("456");
    Assert.assertEquals(new Integer(123), fieldModel.getDefaultValue());
    Assert.assertEquals(new Integer(456), fieldModel.getValue());
    Assert.assertEquals(456, instance.field1);
    Assert.assertEquals(2, x.changeCount);
    Assert.assertEquals(new Integer(456), x.lastValue);
  }
}
