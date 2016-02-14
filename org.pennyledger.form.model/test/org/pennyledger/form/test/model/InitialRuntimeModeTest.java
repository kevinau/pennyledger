package org.pennyledger.form.test.model;

import java.util.List;

import javax.persistence.Embedded;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.Mode;
import org.pennyledger.form.ModeFor;
import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IObjectModel;


public class InitialRuntimeModeTest {

  @SuppressWarnings("unused")
  private static class TestClass {
    @Mode(EntryMode.ENTRY)
    String field0;
    @Mode(EntryMode.VIEW)
    String field1;
    String field2;
    
    @ModeFor({"field0", "field1", "field2"})
    EntryMode fieldMode () {
      return EntryMode.NA;
    }
  }

  @Mode(EntryMode.VIEW)
  private static class ComplexClass {
    @Mode(EntryMode.VIEW)
    String field3;
    @Embedded
    @Mode(EntryMode.ENTRY)
    TestClass nested = new TestClass();
    
    @ModeFor("field3")
    EntryMode fieldMode() {
      return EntryMode.NA;
    }
  }

  static Object[] testResults = new Object[] {
      "/form", EntryMode.VIEW,
      "/form/field3", EntryMode.NA,
      "/form/nested", EntryMode.ENTRY,
      "/form/nested/field0", EntryMode.NA,
      "/form/nested/field1", EntryMode.NA,
      "/form/nested/field2", EntryMode.NA,
  };
  

  @Test
  public void testInitialModes () throws Exception {
    ComplexClass instance = new ComplexClass();
    IFormModel<ComplexClass> formModel = new FormModel<ComplexClass>(instance);
    
    for (int i = 0; i < testResults.length; i += 2) {
      String p1 = (String)testResults[i];
      List<IObjectModel> nodes = formModel.selectObjectModels(p1);
      Assert.assertEquals(1, nodes.size());
      IObjectModel om = nodes.get(0);
      Assert.assertEquals(testResults[i + 1], om.getEntryMode());
    }
  }
}
