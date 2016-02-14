package org.pennyledger.form.test.model;

import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;

@SuppressWarnings("unused")
public class GroupModelTest {

  private static class TestClass {
    private Integer field1 = 100;
    private Integer field2 = 200;
  }
  
  
  private EventCounter eventCounter = new EventCounter();
  
  
  @Test
  public void testBasicForm () {
    TestClass instance = new TestClass();
    IFormModel<TestClass> formModel = new FormModel<TestClass>(instance);
    formModel.addEntryModeListener(eventCounter);
    formModel.addFieldEventListener(eventCounter);
    
    IFieldModel fieldModel1 = formModel.selectFieldModel("//field1");
    Assert.assertEquals(new Integer(100), fieldModel1.getValue());
    Assert.assertEquals(new Integer(100), instance.field1);
    
    Assert.assertEquals(1, eventCounter.modeChangedCount);
    
    // The following are for two fields
    eventCounter.print();
    Assert.assertEquals(2, eventCounter.sourceChangeCount);
    Assert.assertEquals(2, eventCounter.valueChangeCount);
    Assert.assertEquals(2, eventCounter.compareEqualityChangeCount);
    Assert.assertEquals(2, eventCounter.compareShowingChangeCount);
    Assert.assertEquals(2, eventCounter.errorClearedCount);
  }

}
