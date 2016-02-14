package org.pennyledger.form.test.model;

import java.util.List;

import javax.persistence.Embedded;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.Mode;
import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.model.EffectiveMode;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IObjectModel;


public class InitialDirectModeTest {

  @SuppressWarnings("unused")
  private static class InnerClass {
    @Mode(EntryMode.ENTRY)
    String field0;
    @Mode(EntryMode.VIEW)
    String field1;
    String field2;
  }

  @Mode(EntryMode.VIEW)
  private static class ComplexClass {
    @Mode(EntryMode.NA)
    String field3;
    @Embedded
    @Mode(EntryMode.ENTRY)
    InnerClass nested = new InnerClass();
  }

  static Object[] testResults = new Object[] {
      "/form", EntryMode.VIEW, EffectiveMode.VIEW,
      "/form/field3", EntryMode.NA, EffectiveMode.NA,
      "/form/nested", EntryMode.ENTRY, EffectiveMode.VIEW,
      "/form/nested/field0", EntryMode.ENTRY, EffectiveMode.VIEW,
      "/form/nested/field1", EntryMode.VIEW, EffectiveMode.VIEW,
      "/form/nested/field2", EntryMode.UNSPECIFIED, EffectiveMode.VIEW,
  };
  

  @Test
  public void testInitialModes () throws Exception {
    ComplexClass instance = new ComplexClass();
    FormModel<ComplexClass> formModel = new FormModel<ComplexClass>(instance);
    
    for (int i = 0; i < testResults.length; i += 3) {
      List<IObjectModel> nodes = formModel.selectObjectModels((String)testResults[i]);
      Assert.assertEquals(1, nodes.size());
      IObjectModel om = nodes.get(0);
      System.out.println("..." + om.getPathName() + " " + om.getClass() + " " + om.getEntryMode() + " " + om.getEffectiveMode());
      //Assert.assertEquals((String)testResults[i], testResults[i + 1], ((ObjectModel)om).getEntryMode());
      //Assert.assertEquals((String)testResults[i], testResults[i + 2], ((ObjectModel)om).getEffectiveMode());
    }
  }
}
