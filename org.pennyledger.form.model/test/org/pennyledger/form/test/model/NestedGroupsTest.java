package org.pennyledger.form.test.model;

import java.util.List;

import javax.persistence.Embeddable;

import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IGroupModel;
import org.pennyledger.form.model.IObjectModel;

public class NestedGroupsTest {

  @SuppressWarnings("unused")
  @Embeddable
  private static class InnerClass {
    private Integer field2;
  }
  
  @SuppressWarnings("unused")
  private static class TestClass {
    private Integer field1;
    private InnerClass inner = new InnerClass();
  }
  
  @SuppressWarnings("unused")
  private static class TestClass2 {
    private Integer field1;
    private InnerClass inner;
  }
  
  @Test
  public void testGroupValueSet () {
    TestClass instance = new TestClass();
    IFormModel<TestClass> formModel = new FormModel<TestClass>(instance);
    EventCounter counter = new EventCounter();

    List<IObjectModel> field2List = formModel.selectObjectModels("//field2");
    Assert.assertEquals(1, field2List.size());
    IFieldModel field2Model = (IFieldModel)field2List.get(0);
    Assert.assertEquals("field2", field2Model.getPathName());
    Assert.assertEquals("inner", field2Model.getParent().getPathName());
    
    Assert.assertEquals(0, counter.valueChangeCount);
    field2Model.setValue(new Integer(2));
    Assert.assertEquals(0, counter.valueChangeCount);
    
    formModel.addFieldEventListener(counter);
    Assert.assertEquals(2, counter.valueChangeCount);
    field2Model.setValue(new Integer(4));
    Assert.assertEquals(3, counter.valueChangeCount);
    
    formModel.removeFieldEventListener(counter);
    Assert.assertEquals(3, counter.valueChangeCount);
    field2Model.setValue(new Integer(6));
    Assert.assertEquals(3, counter.valueChangeCount);
  }
  
  
  @Test
  public void testLiveEventListeners () {
    // Note TestClass 2
    TestClass2 instance = new TestClass2();
    IFormModel<TestClass2> formModel = new FormModel<TestClass2>(instance);
    EventCounter counter = new EventCounter();
    formModel.addFieldEventListener(counter);

    List<IObjectModel> field2List = formModel.selectObjectModels("//inner");
    Assert.assertEquals(1, field2List.size());
    IGroupModel groupModel = (IGroupModel)field2List.get(0);
    Assert.assertEquals("inner", groupModel.getPathName());
    
    Assert.assertEquals(1, counter.valueChangeCount);
    
    InnerClass innerValue = new InnerClass();
    innerValue.field2 = new Integer(100);
    groupModel.setValue(innerValue);
    Assert.assertEquals(2, counter.valueChangeCount);
    
    IFieldModel field2Model = formModel.selectFieldModel("//field2");
    Assert.assertNotNull (field2Model);
    Assert.assertEquals(new Integer(100), field2Model.getValue());
    
    field2Model.setValue(new Integer(200));
    Assert.assertEquals(3, counter.valueChangeCount);

    groupModel.setValue(null);
    // field2Model is now detached
    field2Model.setValue(new Integer(300));
    Assert.assertEquals(3, counter.valueChangeCount);
  }
  
  
}
