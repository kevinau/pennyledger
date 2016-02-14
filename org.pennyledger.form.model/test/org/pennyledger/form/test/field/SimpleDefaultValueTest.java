package org.pennyledger.form.test.field;

import org.pennyledger.form.DefaultFor;
import org.pennyledger.form.FormField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IGroupModel;

public class SimpleDefaultValueTest {

  public static class TestClass {
    private Integer field0;
    
    @FormField(nullable=false)
    private Integer field1 = 0;
    
    @FormField(nullable=false)
    private Integer field2 = 123;
    
    @FormField(nullable=false)
    private Integer field3 = 123;
    
    @DefaultFor("field3")
    private static Integer defaultForField3() {
      return 456;
    }

    @FormField(nullable=false)
    private Integer field4 = 123;
    
    @DefaultFor("field4")
    private Integer defaultForField4() {
      // TODO The following causes an error when calculating dependencies
      //   return 789
      // The following does not cause an error (and neither does the static version above)
      return new Integer(789);
    }
  }

  private IFormModel<TestClass> formModel;
  private IFieldModel fieldModel0;
  private IFieldModel fieldModel1;
  private IFieldModel fieldModel2;
  private IFieldModel fieldModel3;
  private IFieldModel fieldModel4;
  
  @Before 
  public void init() {
    TestClass instance = new TestClass();
    formModel = new FormModel<TestClass>(instance);
    
    IGroupModel groupModel = formModel.getRootModel();
    fieldModel0 = groupModel.getMember("field0");
    fieldModel1 = groupModel.getMember("field1");
    fieldModel2 = groupModel.getMember("field2");
    fieldModel3 = groupModel.getMember("field3");
    fieldModel4 = groupModel.getMember("field4");
  }

  
  @Test
  public void initialCondition() {
    Assert.assertEquals(null, fieldModel0.getDefaultValue());
    Assert.assertEquals(new Integer(0), fieldModel1.getDefaultValue());
    Assert.assertEquals(new Integer(123), fieldModel2.getDefaultValue());
    Assert.assertEquals(new Integer(456), fieldModel3.getDefaultValue());
    Assert.assertEquals(new Integer(789), fieldModel4.getDefaultValue());
    
    Assert.assertEquals(null, fieldModel0.getValue());
    Assert.assertEquals(new Integer(0), fieldModel1.getValue());
    Assert.assertEquals(new Integer(123), fieldModel2.getValue());
    Assert.assertEquals(new Integer(456), fieldModel3.getValue());
    Assert.assertEquals(new Integer(789), fieldModel4.getValue());

    TestClass instance = formModel.getInstance();
    Assert.assertEquals(null, instance.field0);
    Assert.assertEquals(new Integer(0), instance.field1);
    Assert.assertEquals(new Integer(123), instance.field2);
    Assert.assertEquals(new Integer(456), instance.field3);
    Assert.assertEquals(new Integer(789), instance.field4);
  }


  @Test
  public void setValue() {
    fieldModel0.setValue(222);
    fieldModel1.setValue(222);
    fieldModel2.setValue(222);
    fieldModel3.setValue(222);
    fieldModel4.setValue(222);
    
    Assert.assertEquals(new Integer(222), fieldModel0.getValue());
    Assert.assertEquals(new Integer(222), fieldModel1.getValue());
    Assert.assertEquals(new Integer(222), fieldModel2.getValue());
    Assert.assertEquals(new Integer(222), fieldModel3.getValue());
    Assert.assertEquals(new Integer(222), fieldModel4.getValue());
    
    TestClass instance = formModel.getInstance();
    Assert.assertEquals(new Integer(222), instance.field0);
    Assert.assertEquals(new Integer(222), instance.field1);
    Assert.assertEquals(new Integer(222), instance.field2);
    Assert.assertEquals(new Integer(222), instance.field3);
    Assert.assertEquals(new Integer(222), instance.field4);
  }
}
