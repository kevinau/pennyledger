package org.pennyledger.form.test.model;

import javax.persistence.Embedded;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.Mode;
import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.model.EffectiveMode;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IObjectModel;


public class GroupModelTest2 {

  @SuppressWarnings("unused")
  private static class TestClass {
    String field1;
  }

  @Mode(EntryMode.VIEW)
  private static class ComplexClass {
    @Embedded
    @Mode(EntryMode.ENTRY)
    TestClass inner = new TestClass();
  }


  @Test
  public void testInitialModes () throws Exception {
    ComplexClass instance = new ComplexClass();
    IFormModel<ComplexClass> formModel = new FormModel<ComplexClass>(instance);

    Assert.assertEquals(EffectiveMode.VIEW, formModel.getRootModel().getEffectiveMode());
    
    IObjectModel innerModel = formModel.selectObjectModel("//inner");
    Assert.assertEquals(EffectiveMode.VIEW, innerModel.getEffectiveMode());
  }
}
