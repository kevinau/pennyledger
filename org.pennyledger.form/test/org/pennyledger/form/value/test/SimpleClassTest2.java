package org.pennyledger.form.value.test;

import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.factory.Form;
import org.pennyledger.form.value.IFieldModel;
import org.pennyledger.form.value.IForm;

public class SimpleClassTest2 {

  public static class TestClass {
    private String field1;
    
    public String getField1() {
      return field1;
    }
  }
  
  @Test
  public void test() {
    IForm<TestClass> form = new Form<>(TestClass.class);
    
    TestClass instance1 = new TestClass();
    instance1.field1 = "abc";
    form.setValue(instance1);
    
    IFieldModel field1Model = form.getFieldWrapper("field1");
    Assert.assertNotNull(field1Model);
    Object v1 = field1Model.getValue();
    Assert.assertEquals("abc", v1);
    
    TestClass instance2 = new TestClass();
    instance2.field1 = "ABC";
    form.setValue(instance2);
    
    v1 = field1Model.getValue();
    Assert.assertEquals("ABC", v1);
  }

}
