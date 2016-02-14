package org.pennyledger.form.test.model;

import java.util.List;

import javax.persistence.Embeddable;

import org.pennyledger.form.FactoryFor;
import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IGroupModel;
import org.pennyledger.form.model.IObjectModel;

public class FactoryValueTest {

  @SuppressWarnings("unused")
  @Embeddable
  private static class InnerClass {
    private Integer field2 = new Integer(10);
  }
  
  private static class TestClass {
    private Integer field1 = new Integer(0);
    private InnerClass inner = null;
    
    @FactoryFor("inner")
    private InnerClass newVariant () {
      if (field1.intValue() % 2 == 0) {
        return null;
      } else {
        return new InnerClass();
      }
    }
  }
    
  @Test
  public void testInitialState () {
    TestClass instance = new TestClass();
    IFormModel<TestClass> formModel = new FormModel<TestClass>(instance);

    List<IObjectModel> field2List = formModel.selectObjectModels("//inner");
    Assert.assertEquals(1, field2List.size());
    IGroupModel groupModel = (IGroupModel)field2List.get(0);
    Assert.assertEquals("inner", groupModel.getPathName());
    Assert.assertEquals(null, instance.inner);
  }
    
  
  @Test
  public void testVariantState () {
    TestClass instance = new TestClass();
    Assert.assertNull(instance.inner);

    IFormModel<TestClass> formModel = new FormModel<TestClass>(instance);
    
    IFieldModel field1 = formModel.selectFieldModel("//field1");
    Assert.assertEquals(field1.getValue(), new Integer(0));
    
    // Change the discriminant value
    field1.setValue(new Integer(1));
    Assert.assertNotNull(instance.inner);
    IFieldModel field2 = formModel.selectFieldModel("//field2");
    Assert.assertNotNull(field2);
    Assert.assertEquals(new Integer(10), field2.getValue());
    
    // Change the discriminant value again
    field1.setValue(new Integer(2));

    Assert.assertNull(instance.inner);
    List<IObjectModel> field2List = formModel.selectObjectModels("//field2");
    Assert.assertEquals(0, field2List.size());
  }
    
}
