package org.pennyledger.form.test.model;

import javax.persistence.Embeddable;

import org.pennyledger.form.Occurs;
import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IArrayModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IGroupModel;


public class ArrayGroupTest {

  @Embeddable
  private static class InnerClass {
    private Integer field2 = new Integer(123);
  }
  
  private static class TestClass {
    @Occurs(3)
    private InnerClass[] field1;
  }
  

  private EventCounter eventCounter = new EventCounter();
  
  
  @Test
  public void testBasicForm () {
    TestClass instance = new TestClass();
    IFormModel<TestClass> formModel = new FormModel<TestClass>(instance);
    IArrayModel arrayModel = formModel.selectObjectModel("//field1");
    Assert.assertEquals(3, arrayModel.getArraySize());
    IGroupModel groupModel1 = (IGroupModel)arrayModel.getMember(0);
    IFieldModel fieldModel1 = (IFieldModel)groupModel1.getMember("field2");
    Assert.assertEquals(new Integer(123), fieldModel1.getValue());
    Assert.assertEquals(3, instance.field1.length);
    Assert.assertEquals(new Integer(123), instance.field1[0].field2);
    
    formModel.addEntryModeListener(eventCounter);
    formModel.addFieldEventListener(eventCounter);

    Assert.assertEquals(1, eventCounter.modeChangedCount);
    
    // The following are for three array fields
    Assert.assertEquals(3, eventCounter.valueChangeCount);
    Assert.assertEquals(3, eventCounter.compareEqualityChangeCount);
    Assert.assertEquals(3, eventCounter.compareShowingChangeCount);
    Assert.assertEquals(3, eventCounter.errorClearedCount);
    
    // The following event is fired when a listener is added
    Assert.assertEquals(3, eventCounter.sourceChangeCount);
  }

}
