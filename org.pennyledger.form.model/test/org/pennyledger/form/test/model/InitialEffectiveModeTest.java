package org.pennyledger.form.test.model;

import java.util.List;

import javax.persistence.Embedded;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.Mode;
import org.pennyledger.form.ModeFor;
import org.pennyledger.form.NotFormField;
import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.model.EffectiveMode;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IObjectModel;


public class InitialEffectiveModeTest {

  @SuppressWarnings("unused")
  private static class TestClass {
    @Mode(EntryMode.NA)
    String field0;
    @Mode(EntryMode.VIEW)
    String field1;
    String field2;
  }

  @Mode(EntryMode.ENTRY)
  private static class ComplexClass {
    
    @NotFormField
    private final int flag;
    
    ComplexClass () {
      this(0);
    }
    
    ComplexClass (int flag) {
      this.flag = flag;
    }
    
    @Mode(EntryMode.VIEW)
    String field3;
    @Embedded
    @Mode(EntryMode.ENTRY)
    TestClass nested = new TestClass();
    
    @ModeFor("field3")
    EntryMode setMode2 () {
      switch (flag) {
      case 0 :
        return EntryMode.ENTRY;
      case 1 :
        return EntryMode.VIEW;
      default :
        return EntryMode.NA;
      }
    }
    
    @ModeFor("nested")
    EntryMode setMode3 () {
      switch (flag) {
      case 0 :
        return EntryMode.ENTRY;
      case 1 :
        return EntryMode.VIEW;
      default :
        return EntryMode.NA;
      }
    }
  }

  static Object[] testResults = new Object[] {
    "/form", EffectiveMode.ENTRY, EffectiveMode.ENTRY, EffectiveMode.ENTRY, 
    "/form/field3", EffectiveMode.ENTRY, EffectiveMode.VIEW, EffectiveMode.NA,
    "/form/nested", EffectiveMode.ENTRY, EffectiveMode.VIEW, EffectiveMode.NA,
    "/form/nested/field0", EffectiveMode.NA, EffectiveMode.NA, EffectiveMode.NA,
    "/form/nested/field1", EffectiveMode.VIEW, EffectiveMode.VIEW, EffectiveMode.NA,
    "/form/nested/field2", EffectiveMode.ENTRY, EffectiveMode.VIEW, EffectiveMode.NA,
  };
  

  @Test
  public void testInitialModes () throws Exception {
    ComplexClass instance = new ComplexClass();
    FormModel<ComplexClass> formModel = new FormModel<ComplexClass>(instance);
    
    for (int i = 0; i < testResults.length; i += 4) {
      String p1 = (String)testResults[i];
      List<IObjectModel> nodes = formModel.selectObjectModels(p1);
      Assert.assertEquals(1, nodes.size());
      IObjectModel om = nodes.get(0);
      Assert.assertEquals((EffectiveMode)testResults[i + 1], om.getEffectiveMode());
    }
    
    instance = new ComplexClass(1);
    formModel = new FormModel<ComplexClass>(instance);

    for (int i = 0; i < testResults.length; i += 4) {
      String p1 = (String)testResults[i];
      List<IObjectModel> nodes = formModel.selectObjectModels(p1);
      Assert.assertEquals(1, nodes.size());
      IObjectModel om = nodes.get(0);
      Assert.assertEquals((EffectiveMode)testResults[i + 2], om.getEffectiveMode());
    }
    
    instance = new ComplexClass(2);
    formModel = new FormModel<ComplexClass>(instance);

    for (int i = 0; i < testResults.length; i += 4) {
      String p1 = (String)testResults[i];
      List<IObjectModel> nodes = formModel.selectObjectModels(p1);
      Assert.assertEquals(1, nodes.size());
      IObjectModel om = nodes.get(0);
      Assert.assertEquals((EffectiveMode)testResults[i + 3], om.getEffectiveMode());
    }
    
  }

}
