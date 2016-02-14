package org.pennyledger.form.test.model;

import javax.persistence.Embeddable;

import org.junit.Assert;

import org.pennyledger.form.Occurs;
import org.junit.Test;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IArrayModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IGroupModel;
import org.pennyledger.form.model.IObjectModel;

@SuppressWarnings("unused")
public class ArrayModelTest2 {

  @Embeddable
  private static class InnerClass {
    private Integer field2;
  }
  
  private static class TestClass0 {
    private InnerClass[] array = new InnerClass[3];
  }
  
  
  private static class TestClass1 {
    @Occurs(4)
    private InnerClass[] array;
  }
  
  
  @Test
  public void testBasicForm () {
    InnerClass instance = new InnerClass();
    IFormModel<InnerClass> formModel = new FormModel<InnerClass>(instance);
    IObjectModel objectModel = formModel.selectObjectModel("/*");
    Assert.assertTrue("Object is group", objectModel instanceof IGroupModel);
  }
  
  
  @Test
  public void testBasicForm2 () {
    TestClass1 instance = new TestClass1();
    IFormModel<TestClass1> formModel = new FormModel<TestClass1>(instance);
    IObjectModel objectModel = formModel.selectObjectModel("/*");
    Assert.assertTrue("Object is group", objectModel instanceof IGroupModel);
  }
  
  
  @Test
  public void testSettingArraySize () {
    TestClass1 instance = new TestClass1();
    IFormModel<TestClass1> formModel = new FormModel<TestClass1>(instance);
    IArrayModel arrayModel = formModel.selectObjectModel("/form/*");
    Assert.assertEquals("array", arrayModel.getPathName());
    arrayModel.setArraySize(4);
    Assert.assertEquals(4, arrayModel.getAllMembers().length);
    Assert.assertNotNull(instance.array);
    Assert.assertEquals(4, instance.array.length);    
  }
}
