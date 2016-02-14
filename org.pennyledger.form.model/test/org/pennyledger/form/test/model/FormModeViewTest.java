package org.pennyledger.form.test.model;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.Mode;
import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.model.EffectiveMode;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IGroupModel;
import org.pennyledger.form.model.IObjectModel;

public class FormModeViewTest {

  @SuppressWarnings("unused")
  @Mode(EntryMode.VIEW)
  private static class TestClass {
    private Integer field1;
  }
  
  @Test
  public void test1() {
    TestClass instance = new TestClass();
    IFormModel<TestClass> formModel = new FormModel<TestClass>(instance);
    
    Assert.assertEquals("Test class", formModel.getFormName());
    
    IGroupModel groupModel = formModel.getRootModel();
    Assert.assertEquals(EffectiveMode.VIEW, groupModel.getEffectiveMode());

    IObjectModel objectModel = groupModel.getMember("field1");
    Assert.assertTrue(objectModel instanceof IFieldModel);
    IFieldModel fieldModel = (IFieldModel)objectModel;
    Assert.assertEquals("field1", fieldModel.getPathName());
    Assert.assertEquals(EffectiveMode.VIEW, fieldModel.getEffectiveMode());
  }

}
