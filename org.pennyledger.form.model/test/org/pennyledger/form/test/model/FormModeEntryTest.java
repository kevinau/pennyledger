package org.pennyledger.form.test.model;

import org.pennyledger.form.EntryMode;
import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.model.EffectiveMode;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.GroupModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IObjectModel;

public class FormModeEntryTest {

  @SuppressWarnings("unused")
  private static class TestClass {
    private Integer field1;
  }
  
  @Test
  public void test1() {
    TestClass instance = new TestClass();
    IFormModel<TestClass> formModel = new FormModel<TestClass>(instance);
    
    Assert.assertEquals("Test class", formModel.getFormName());
    
    IObjectModel groupModel = formModel.getRootModel();
    Assert.assertEquals(EffectiveMode.ENTRY, groupModel.getEffectiveMode());

    IObjectModel objectModel = ((GroupModel)groupModel).getMember("field1");
    Assert.assertTrue(objectModel instanceof IFieldModel);
    IFieldModel fieldModel = (IFieldModel)objectModel;
    Assert.assertEquals("field1", fieldModel.getPathName());
    Assert.assertEquals(EffectiveMode.ENTRY, fieldModel.getEffectiveMode());
    
    groupModel.setMode(EntryMode.VIEW);
    Assert.assertEquals(EffectiveMode.VIEW, groupModel.getEffectiveMode());
    Assert.assertEquals(EffectiveMode.VIEW, fieldModel.getEffectiveMode());
  }

}
