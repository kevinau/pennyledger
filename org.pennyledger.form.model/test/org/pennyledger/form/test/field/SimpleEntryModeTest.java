package org.pennyledger.form.test.field;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.Mode;
import org.pennyledger.form.ModeFor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.model.EffectiveMode;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IGroupModel;

public class SimpleEntryModeTest {

  @SuppressWarnings("unused")
  public static class TestClass {
    private Integer field0 = 0;
    
    @Mode(EntryMode.UNSPECIFIED)
    private Integer field1;
    
    @Mode(EntryMode.ENTRY)
    private Integer field2;
    
    @Mode(EntryMode.VIEW)
    private Integer field3;
    
    @Mode(EntryMode.NA)
    private Integer field4;
    
    private Integer field5;
    
    private Integer field6;
    
    @ModeFor("field5")
    private static EntryMode field5Mode () {
      return EntryMode.VIEW;
    }

    @ModeFor("field6")
    private EntryMode field6Mode () {
      if (field0 == 0) {
        return EntryMode.VIEW;
      } else {
        return EntryMode.ENTRY;
      }
    }
  }

  private IFormModel<TestClass> formModel;
  private IFieldModel fieldModel0;
  private IFieldModel fieldModel1;
  private IFieldModel fieldModel2;
  private IFieldModel fieldModel3;
  private IFieldModel fieldModel4;
  
  @Before 
  public void init() {
    TestClass instance = new TestClass();
    formModel = new FormModel<TestClass>(instance);
    
    IGroupModel groupModel = formModel.getRootModel();
    fieldModel0 = groupModel.getMember("field0");
    fieldModel1 = groupModel.getMember("field1");
    fieldModel2 = groupModel.getMember("field2");
    fieldModel3 = groupModel.getMember("field3");
    fieldModel4 = groupModel.getMember("field4");
  }

  
  @Test
  public void initialCondition() {
    Assert.assertEquals(EntryMode.UNSPECIFIED, fieldModel0.getEntryMode());
    Assert.assertEquals(EntryMode.UNSPECIFIED, fieldModel1.getEntryMode());
    Assert.assertEquals(EntryMode.ENTRY, fieldModel2.getEntryMode());
    Assert.assertEquals(EntryMode.VIEW, fieldModel3.getEntryMode());
    Assert.assertEquals(EntryMode.NA, fieldModel4.getEntryMode());

    Assert.assertEquals(EffectiveMode.ENTRY, fieldModel0.getEffectiveMode());
    Assert.assertEquals(EffectiveMode.ENTRY, fieldModel1.getEffectiveMode());
    Assert.assertEquals(EffectiveMode.ENTRY, fieldModel2.getEffectiveMode());
    Assert.assertEquals(EffectiveMode.VIEW, fieldModel3.getEffectiveMode());
    Assert.assertEquals(EffectiveMode.NA, fieldModel4.getEffectiveMode());
  }

  @Test 
  public void staticModeFor () {
    IFieldModel fieldModel = formModel.selectFieldModel("//field5");
    Assert.assertEquals("field5", fieldModel.getPathName());
    Assert.assertEquals(EntryMode.VIEW, fieldModel.getEntryMode());
  }

  @Test 
  public void runtimeModeFor () {
    IFieldModel fieldModel = formModel.selectFieldModel("//field6");
    Assert.assertEquals("field6", fieldModel.getPathName());
    Assert.assertEquals(EntryMode.VIEW, fieldModel.getEntryMode());
    fieldModel0.setValue(123);
    Assert.assertEquals(EntryMode.ENTRY, fieldModel.getEntryMode());
  }

}
