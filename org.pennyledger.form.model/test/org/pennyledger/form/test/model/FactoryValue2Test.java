package org.pennyledger.form.test.model;

import java.util.List;

import javax.persistence.Embeddable;

import org.pennyledger.form.FactoryFor;
import org.pennyledger.form.FormField;
import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.model.ContainerEventListener;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IContainerModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IGroupModel;
import org.pennyledger.form.model.IObjectModel;

public class FactoryValue2Test {

  @Embeddable
  private static class InnerClass {
    private String line = "80 First Ave";
  }
  
  private static class TestClass {
    @FormField(length=10)
    private String name;
    private boolean deliver;
    private InnerClass inner = null;
    
    @FactoryFor("inner")
    private InnerClass newVariant () {
      if (deliver) {
        return new InnerClass();
      } else {
        return null;
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
    
    IFieldModel field1 = formModel.selectFieldModel("//deliver");
    Assert.assertEquals(field1.getValue(), Boolean.FALSE);
    
    // Change the discriminant value
    field1.setValue(true);
    Assert.assertNotNull(instance.inner);
    Assert.assertEquals("80 First Ave", instance.inner.line);
    
    IObjectModel field2 = formModel.selectObjectModel("//inner");
    Assert.assertNotNull(field2);
    Assert.assertTrue(field2 instanceof IGroupModel);
    Object inner = field2.getValue();
    Assert.assertTrue(inner instanceof InnerClass);
    Assert.assertEquals("80 First Ave", ((InnerClass)inner).line);
    
    // Change the discriminant value again
    field1.setValue(false);

    Assert.assertNull(instance.inner);
    IObjectModel field3 = formModel.selectObjectModel("//inner");
    Assert.assertNotNull(field3);
    List<IObjectModel> field2List = formModel.selectObjectModels("//line");
    Assert.assertEquals(0, field2List.size());
  }
    
  
  public int createCount = 0;
  public int destroyCount = 0;
  public int reduceCount = 0;

  private ContainerEventListener eventCounter = new ContainerEventListener() {
    @Override
    public void containerCreate(IContainerModel model) {
      createCount++;
    }

    @Override
    public void containerDestroy(IContainerModel model) {
      destroyCount++;
    }

    @Override
    public void containerOccursReduced(IContainerModel eventSource) {
      reduceCount++;
    }

  };
  
  
  @Test
  public void testEventHandler () {
    TestClass instance = new TestClass();
    IFormModel<TestClass> formModel = new FormModel<TestClass>(instance);
    
    formModel.addContainerEventListener(eventCounter);
    Assert.assertEquals(0, createCount);
    Assert.assertEquals(0, destroyCount);
    
    IFieldModel field1 = formModel.selectFieldModel("//deliver");
    Assert.assertEquals(field1.getValue(), Boolean.FALSE);
    
    // Change the discriminant value
    field1.setValue(true);
    Assert.assertEquals(1, createCount);
    Assert.assertEquals(0, destroyCount);
    
    // Change the discriminant value again
    field1.setValue(false);
    Assert.assertEquals(1, createCount);
    Assert.assertEquals(1, destroyCount);
  }
    
}
