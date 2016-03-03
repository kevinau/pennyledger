package org.pennyledger.form.value.test;

import javax.persistence.Embedded;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.Optional;
import org.pennyledger.form.factory.Form;
import org.pennyledger.form.value.IFieldModel;
import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IForm;
import org.pennyledger.form.value.IObjectModel;

public class CompoundClassOptionialTest {

  private static class Inner {
    @SuppressWarnings("unused")
    private String field11;
  }
  
  private static class Simple {
    @Embedded
    @Optional
    private Inner field1 = new Inner();
    @SuppressWarnings("unused")
    private String field2;
  }
  
  private IForm<Simple> form;
  private Simple value;
  
  @Before
  public void setup() {
    form = new Form<>(Simple.class);
    value = new Simple();
    value.field1.field11 = "abc";
    value.field2 = "def";
    form.setValue(value);
  }
  
  @Test
  public void simpleClassTest() {
    form.walkFieldModels(new IFieldVisitable() {
      @Override
      public void visit(IFieldModel model) {
        //System.out.println(model.getPlan().getName() + "  " + model.getClass().getSimpleName());
      }
    });
    IObjectModel model1 = form.getFieldModel("field1/field11");
    IObjectModel model2 = form.getFieldModel("field2");

    Assert.assertEquals("abc", model1.getValue());
    Assert.assertEquals("def", model2.getValue());
    
    Simple value3 = new Simple();
    value3.field1 = null;
    value3.field2 = "222";
    form.setValue(value3);
    
    try {
      model1 = form.getObjectModel("field1");
      Assert.fail("Field 1 is null, so this member model should not exist");
    } catch (IllegalArgumentException ex) {
      // This is expected
    }
    model2 = form.getFieldModel("field2");

    Assert.assertEquals("222", model2.getValue());

    Simple value4 = new Simple();
    value4.field1.field11 = null;
    value4.field2 = "333";
    form.setValue(value4);
    
    model1 = form.getFieldModel("field1/field11");
    model2 = form.getFieldModel("field2");

    Assert.assertNull(model1.getValue());
    Assert.assertEquals("333", model2.getValue());
  }

}
