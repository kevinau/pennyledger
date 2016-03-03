package org.pennyledger.form.value.test;

import java.util.List;

import javax.persistence.Embedded;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.factory.Form;
import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IFieldWrapper;
import org.pennyledger.form.value.IForm;
import org.pennyledger.form.value.IObjectWrapper;

public class CompoundClassTest {

  private static class Inner {
    private String field11;
  }
  
  private static class Simple {
    @Embedded
    private Inner field1 = new Inner();
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
    form.walkFieldWrappers(new IFieldVisitable() {
      @Override
      public void visit(IFieldWrapper model) {
        //System.out.println(model.getPlan().getName() + "  " + model.getClass().getSimpleName());
      }
    });
    IObjectWrapper wrapper1 = form.getFieldWrapper("field1/field11");
    IObjectWrapper wrapper2 = form.getFieldWrapper("field2");

    Assert.assertEquals("abc", wrapper1.getValue());
    Assert.assertEquals("def", wrapper2.getValue());
    
    wrapper1.setValue("ABC");
    wrapper2.setValue("DEF");

    Assert.assertEquals("ABC", value.field1.field11);
    Assert.assertEquals("DEF", value.field2);
    
    Simple value2 = new Simple();
    value2.field1.field11 = "aaa";
    value2.field2 = "ddd";
    form.setValue(value2);
    
    wrapper1 = form.getFieldWrapper("field1/field11");
    wrapper2 = form.getFieldWrapper("field2");

    Assert.assertEquals("aaa", wrapper1.getValue());
    Assert.assertEquals("ddd", wrapper2.getValue());
    
//    Simple value3 = new Simple();
//    value3.field1 = null;
//    value3.field2 = "222";
//    form.setValue(value3);
//    
//    wrapper1 = form.getObjectWrapper("field1");
//    wrapper2 = form.getFieldWrapper("field2");
//
//    Assert.assertNull(wrapper1.getValue());
//    Assert.assertEquals("222", wrapper2.getValue());
//
//    Simple value4 = new Simple();
//    value4.field1.field11 = null;
//    value4.field2 = "333";
//    form.setValue(value4);
//    
//    wrapper1 = form.getFieldWrapper("field1/field11");
//    wrapper2 = form.getFieldWrapper("field2");
//
//    Assert.assertNull(wrapper1.getValue());
//    Assert.assertEquals("333", wrapper2.getValue());
  }

  @Test
  public void selectAll () {
    List<IFieldWrapper> found = form.getFieldWrappers();
    Assert.assertEquals(2, found.size());
  }
  
  @Test
  public void wildcardSelect () {
    List<IFieldWrapper> found = form.getFieldWrappers("*");
    Assert.assertEquals(1, found.size());
  }
  
  @Test
  public void wildcard2Select () {
    List<IFieldWrapper> found = form.getFieldWrappers("*/*");
    Assert.assertEquals(1, found.size());
  }
  
  @Test
  public void descendentSelect () {
    List<IFieldWrapper> found = form.getFieldWrappers("//");
    Assert.assertEquals(2, found.size());
  }
  

  public static void main(String[] args) {
    CompoundClassTest test = new CompoundClassTest();
    test.setup();
    //test.form.dump();
    test.simpleClassTest();
  }
}
