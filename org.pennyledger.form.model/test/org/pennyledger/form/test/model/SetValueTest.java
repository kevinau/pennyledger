package org.pennyledger.form.test.model;

import org.pennyledger.form.model.FieldEventAdapter;
import org.pennyledger.form.model.FieldModel;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.util.UserEntryException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SetValueTest {

  private static class TestClass {
    Integer field0;
    Integer field1 = new Integer(1);
  }

  private IFormModel<TestClass> formModel;
  private ValueChangeCountListener x;

  private static class ValueChangeCountListener extends FieldEventAdapter {

    private int valueChangeCount = 0;
    private int sourceChangeCount = 0;
    private int errorNoted = 0;
    private int errorCleared = 0;
    private int compareDefaultValueEqual = 0;
    private int compareDefaultSourceEqual = 0;

    @Override
    public void valueChange(IFieldModel model) {
      valueChangeCount++;
    }

    @Override
    public void sourceChange(IFieldModel model) {
      sourceChangeCount++;
    }

    @Override
    public void errorNoted(IFieldModel model, UserEntryException ex) {
      errorNoted++;
    }

    @Override
    public void errorCleared(IFieldModel model) {
      errorCleared++;
    }

    @Override
    public void compareEqualityChange(IFieldModel model) {
      compareDefaultValueEqual++;
    }

    @Override
    public void compareShowingChange(IFieldModel model, boolean isSourceTrigger) {
      compareDefaultSourceEqual++;
    }

    public void reset () {
      valueChangeCount = 0;
      sourceChangeCount = 0;
      errorNoted = 0;
      errorCleared = 0;
      compareDefaultValueEqual = 0;
      compareDefaultSourceEqual = 0;
    }
  }

  @Before
  public void setup() {
    formModel = new FormModel<TestClass>(TestClass.class);
    new MemoryUI(formModel);
    
    x = new ValueChangeCountListener();
  }

  @Test
  public void noDefaultSpecified() {
    TestClass instance = formModel.getInstance();
    IFieldModel fieldModel = formModel.selectFieldModel("/form/field0");
    fieldModel.addFieldEventListener(x);
    x.reset();
    
    Assert.assertEquals(null, fieldModel.getDefaultValue());
    Assert.assertEquals(null, fieldModel.getValue());
    Assert.assertEquals(null, instance.field0);
    Assert.assertEquals(true, ((FieldModel)fieldModel).isComparedValueEqual());
    Assert.assertEquals(0, x.valueChangeCount);
    Assert.assertEquals(0, x.sourceChangeCount);

    fieldModel.setValue(null);
    Assert.assertEquals(null, fieldModel.getValue());
    Assert.assertEquals(null, instance.field0);
    Assert.assertEquals(0, x.valueChangeCount);
    Assert.assertEquals(0, x.sourceChangeCount);

    fieldModel.setValue(new Integer(123));
    Assert.assertEquals(null, fieldModel.getDefaultValue());
    Assert.assertEquals(new Integer(123), fieldModel.getValue());
    Assert.assertEquals(new Integer(123), instance.field0);
    Assert.assertEquals(1, x.valueChangeCount);
    Assert.assertEquals(1, x.sourceChangeCount);
    Assert.assertEquals(false, ((FieldModel)fieldModel).isComparedValueEqual());

    fieldModel.setValue(new Integer(123));
    Assert.assertEquals(1, x.valueChangeCount);
    Assert.assertEquals(1, x.sourceChangeCount);

    fieldModel.setValue(null);
    Assert.assertEquals(null, fieldModel.getValue());
    Assert.assertEquals(null, instance.field0);
    Assert.assertEquals(2, x.valueChangeCount);
    Assert.assertEquals(2, x.sourceChangeCount);
    Assert.assertEquals(true, ((FieldModel)fieldModel).isComparedValueEqual());
  }
  
  
  @Test
  public void defaultSpecified() {
    TestClass instance = formModel.getInstance();
    IFieldModel fieldModel = formModel.selectFieldModel("/form/field1");
    fieldModel.addFieldEventListener(x);
    x.reset();

    Assert.assertEquals(new Integer(1), fieldModel.getDefaultValue());
    Assert.assertEquals(new Integer(1), fieldModel.getValue());
    Assert.assertEquals(new Integer(1), instance.field1);
    Assert.assertEquals(true, ((FieldModel)fieldModel).isComparedValueEqual());

    fieldModel.setValue(new Integer(1));
    Assert.assertEquals(new Integer(1), fieldModel.getValue());
    Assert.assertEquals(new Integer(1), instance.field1);
    Assert.assertEquals(0, x.valueChangeCount);
    Assert.assertEquals(0, x.sourceChangeCount);

    fieldModel.setValue(new Integer(123));
    Assert.assertEquals(new Integer(1), fieldModel.getDefaultValue());
    Assert.assertEquals(new Integer(123), fieldModel.getValue());
    Assert.assertEquals(new Integer(123), instance.field1);
    Assert.assertEquals(1, x.valueChangeCount);
    Assert.assertEquals(1, x.sourceChangeCount);
    Assert.assertEquals(false, ((FieldModel)fieldModel).isComparedValueEqual());

    fieldModel.setValue(new Integer(123));
    Assert.assertEquals(1, x.valueChangeCount);
    Assert.assertEquals(1, x.sourceChangeCount);

    fieldModel.setValue(new Integer(1));
    Assert.assertEquals(new Integer(1), fieldModel.getValue());
    Assert.assertEquals(new Integer(1), instance.field1);
    Assert.assertEquals(2, x.valueChangeCount);
    Assert.assertEquals(2, x.sourceChangeCount);
    Assert.assertEquals(true, ((FieldModel)fieldModel).isComparedValueEqual());
  }
  
  @Test
  public void setBadValues() {
    TestClass instance = formModel.getInstance();
    IFieldModel fieldModel = formModel.selectFieldModel("/form/field0");
    fieldModel.addFieldEventListener(x);
    x.reset();

    Assert.assertEquals(null, fieldModel.getValue());
    Assert.assertEquals(null, instance.field0);
    Assert.assertEquals(0, x.errorNoted);
    Assert.assertEquals(0, x.errorCleared);
    Assert.assertEquals(true, ((FieldModel)fieldModel).isComparedValueEqual());
    Assert.assertEquals(true, ((FieldModel)fieldModel).isComparedSourceEqual());
    Assert.assertEquals(0, x.compareDefaultValueEqual);
    Assert.assertEquals(0, x.compareDefaultSourceEqual);
    Assert.assertEquals(0, x.sourceChangeCount);

    fieldModel.setValueFromSource("xyz");
    Assert.assertEquals(1, x.errorNoted);
    Assert.assertEquals(0, x.errorCleared);
    Assert.assertEquals(UserEntryException.Type.ERROR, fieldModel.getStatus());
    Assert.assertEquals(IFieldModel.UNKNOWN, fieldModel.getValue());
    Assert.assertEquals(null, instance.field0);
    Assert.assertEquals(true, ((FieldModel)fieldModel).isComparedValueEqual());
    Assert.assertEquals(false, ((FieldModel)fieldModel).isComparedSourceEqual());
    Assert.assertEquals(0, x.compareDefaultValueEqual);
    Assert.assertEquals(1, x.compareDefaultSourceEqual);
    Assert.assertEquals(1, x.sourceChangeCount);

    fieldModel.setValueFromSource("+");
    Assert.assertEquals(2, x.errorNoted);
    Assert.assertEquals(0, x.errorCleared);
    Assert.assertEquals(UserEntryException.Type.INCOMPLETE, fieldModel.getStatus());
    Assert.assertEquals(IFieldModel.UNKNOWN, fieldModel.getValue());
    Assert.assertEquals(null, instance.field0);
    Assert.assertEquals(true, ((FieldModel)fieldModel).isComparedValueEqual());
    Assert.assertEquals(false, ((FieldModel)fieldModel).isComparedSourceEqual());
    Assert.assertEquals(0, x.compareDefaultValueEqual);
    Assert.assertEquals(1, x.compareDefaultSourceEqual);
    Assert.assertEquals(2, x.sourceChangeCount);

    fieldModel.setValueFromSource("0");
    Assert.assertEquals(2, x.errorNoted);
    Assert.assertEquals(1, x.errorCleared);
    Assert.assertEquals(UserEntryException.Type.OK, fieldModel.getStatus());
    Assert.assertEquals(new Integer(0), fieldModel.getValue());
    Assert.assertEquals(new Integer(0), instance.field0);
    Assert.assertEquals(false, ((FieldModel)fieldModel).isComparedValueEqual());
    Assert.assertEquals(false, ((FieldModel)fieldModel).isComparedSourceEqual());
    Assert.assertEquals(1, x.compareDefaultValueEqual);
    Assert.assertEquals(1, x.compareDefaultSourceEqual);
    Assert.assertEquals(3, x.sourceChangeCount);
  }

  @Test
  public void setBadValues2() {
    TestClass instance = formModel.getInstance();
    IFieldModel fieldModel = formModel.selectFieldModel("/form/field1");
    fieldModel.addFieldEventListener(x);
    x.reset();
    
    Assert.assertEquals(new Integer(1), fieldModel.getValue());
    Assert.assertEquals(new Integer(1), instance.field1);
    Assert.assertEquals(0, x.errorNoted);
    Assert.assertEquals(0, x.errorCleared);
    Assert.assertEquals(true, ((FieldModel)fieldModel).isComparedValueEqual());
    Assert.assertEquals(true, ((FieldModel)fieldModel).isComparedSourceEqual());
    Assert.assertEquals(0, x.compareDefaultValueEqual);
    Assert.assertEquals(0, x.compareDefaultSourceEqual);
    Assert.assertEquals(0, x.sourceChangeCount);

    fieldModel.setValue("xyz");
    Assert.assertEquals(1, x.errorNoted);
    Assert.assertEquals(0, x.errorCleared);
    Assert.assertEquals(UserEntryException.Type.ERROR, fieldModel.getStatus());
    Assert.assertEquals(IFieldModel.UNKNOWN, fieldModel.getValue());
    Assert.assertEquals(new Integer(1), instance.field1);
    Assert.assertEquals(true, ((FieldModel)fieldModel).isComparedValueEqual());
    Assert.assertEquals(false, ((FieldModel)fieldModel).isComparedSourceEqual());
    Assert.assertEquals(0, x.compareDefaultValueEqual);
    Assert.assertEquals(1, x.compareDefaultSourceEqual);
    Assert.assertEquals(1, x.sourceChangeCount);

    fieldModel.setValue("+");
    Assert.assertEquals(2, x.errorNoted);
    Assert.assertEquals(0, x.errorCleared);
    Assert.assertEquals(UserEntryException.Type.INCOMPLETE, fieldModel.getStatus());
    Assert.assertEquals(IFieldModel.UNKNOWN, fieldModel.getValue());
    Assert.assertEquals(new Integer(1), instance.field1);
    Assert.assertEquals(true, ((FieldModel)fieldModel).isComparedValueEqual());
    Assert.assertEquals(false, ((FieldModel)fieldModel).isComparedSourceEqual());
    Assert.assertEquals(0, x.compareDefaultValueEqual);
    Assert.assertEquals(1, x.compareDefaultSourceEqual);
    Assert.assertEquals(2, x.sourceChangeCount);

    fieldModel.setValue("1");
    Assert.assertEquals(2, x.errorNoted);
    Assert.assertEquals(1, x.errorCleared);
    Assert.assertEquals(UserEntryException.Type.OK, fieldModel.getStatus());
    Assert.assertEquals(new Integer(1), fieldModel.getValue());
    Assert.assertEquals(new Integer(1), instance.field1);
    Assert.assertEquals(true, ((FieldModel)fieldModel).isComparedValueEqual());
    Assert.assertEquals(true, ((FieldModel)fieldModel).isComparedSourceEqual());
    Assert.assertEquals(0, x.compareDefaultValueEqual);
    Assert.assertEquals(2, x.compareDefaultSourceEqual);
    Assert.assertEquals(3, x.sourceChangeCount);
  }

}
