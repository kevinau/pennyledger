package org.pennyledger.form.test.model;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.ModeFor;
import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.model.EffectiveMode;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IGroupModel;
import org.pennyledger.form.model.IObjectModel;

public class FormWithDynamicModeTest {

  private static class TestClass {
    private Integer field1 = new Integer(0);
    
    @ModeFor("field1")
    private EntryMode field1EntryMode () {
      if (field1.intValue() == 0) {
        return EntryMode.ENTRY;
      } else {
        return EntryMode.VIEW;
      }
    }
  }
  
  @Test
  public void test1() {
    TestClass instance = new TestClass();
    IFormModel<TestClass> formModel = new FormModel<TestClass>(instance);
    
    Assert.assertEquals("Test class", formModel.getFormName());
    
    IGroupModel groupModel = formModel.getRootModel();
    Assert.assertEquals(EffectiveMode.ENTRY, groupModel.getEffectiveMode());

    IObjectModel objectModel = groupModel.getMember("field1");
    Assert.assertTrue(objectModel instanceof IFieldModel);
    IFieldModel fieldModel = (IFieldModel)objectModel;
    
    ModeChangeCounter x = new ModeChangeCounter();
    Assert.assertEquals(0, x.modeChangedCount);
    fieldModel.addEffectiveModeListener(x);
    Assert.assertEquals(1, x.modeChangedCount);
    
    Assert.assertEquals("field1", fieldModel.getPathName());
    Assert.assertEquals(EffectiveMode.ENTRY, fieldModel.getEffectiveMode());
    
    fieldModel.setValue(new Integer(123));
    Assert.assertEquals(EffectiveMode.VIEW, fieldModel.getEffectiveMode());
    Assert.assertEquals(2, x.modeChangedCount);
    Assert.assertEquals(EffectiveMode.VIEW, x.mode);
    
    fieldModel.setValue(new Integer(0));
    Assert.assertEquals(EffectiveMode.ENTRY, fieldModel.getEffectiveMode());
    Assert.assertEquals(3, x.modeChangedCount);
    Assert.assertEquals(EffectiveMode.ENTRY, x.mode);

  }

}
