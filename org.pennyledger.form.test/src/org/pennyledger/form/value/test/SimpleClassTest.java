package org.pennyledger.form.value.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.model.Form;
import org.pennyledger.form.model.IForm;
import org.pennyledger.form.value.IFieldModel;
import org.pennyledger.form.value.IObjectModel;

public class SimpleClassTest {

  private static class Simple {
    private String field1;
    
    private String field2;
    
  }

  private IForm<Simple> form;
  private Simple value;
  
  @Before
  public void setup() {
    form = new Form<>(Simple.class);
    
    value = new Simple();
    value.field1 = "abc";
    value.field2 = "def";
    form.setValue(value);
  }
  
  @Test
  public void simpleClassTest() {
    IFieldModel wrapper1 = form.getFieldWrapper("field1");
    IObjectModel wrapper2 = form.getFieldWrapper("field2");

    Assert.assertEquals("abc", wrapper1.getValue());
    Assert.assertEquals("def", wrapper2.getValue());
    
    wrapper1.setValue("ABC");
    wrapper2.setValue("DEF");

    Assert.assertEquals("ABC", value.field1);
    Assert.assertEquals("DEF", value.field2);
    
    Simple value2 = new Simple();
    value2.field1 = "aaa";
    value2.field2 = "ddd";
    form.setValue(value2);
    
    wrapper1 = form.getFieldWrapper("field1");
    wrapper2 = form.getFieldWrapper("field2");

    Assert.assertEquals("aaa", wrapper1.getValue());
    Assert.assertEquals("ddd", wrapper2.getValue());
    
    Simple value3 = new Simple();
    value3.field1 = "111";
    value3.field2 = null;
    form.setValue(value3);
    
    wrapper1 = form.getFieldWrapper("field1");
    wrapper2 = form.getFieldWrapper("field2");

    Assert.assertEquals("111", wrapper1.getValue());
    Assert.assertNull(wrapper2.getValue());
  }

  @Test
  public void selectAll () {
    List<IFieldModel> found = form.getFieldWrappers();
    Assert.assertEquals(2, found.size());
  }
  
  @Test
  public void wildcardSelect () {
    List<IFieldModel> found = form.getFieldWrappers("*");
    Assert.assertEquals(2, found.size());
  }
  
  @Test
  public void descendentSelect () {
    List<IFieldModel> found = form.getFieldWrappers("//");
    Assert.assertEquals(2, found.size());
  }
  
  
  public static void main(String[] args) {
    SimpleClassTest test = new SimpleClassTest();
    test.simpleClassTest();
  }
}
