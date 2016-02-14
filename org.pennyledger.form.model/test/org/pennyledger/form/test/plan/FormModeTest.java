package org.pennyledger.form.test.plan;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.Form;
import org.pennyledger.form.Mode;
import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.plan.impl.FormPlan;


public class FormModeTest {

  @Form
  public static class Test0 {
  }

  @Mode (EntryMode.ENTRY)
  public static class Test1 {
  }

  @Test
  public void modeTest() {
    FormPlan plan = new FormPlan(Test0.class);
    Assert.assertEquals(EntryMode.UNSPECIFIED, plan.getRootPlan().getStaticMode());
  }
  
  @Test
  public void modeTest2() {
    FormPlan plan = new FormPlan(Test1.class);
    Assert.assertEquals(EntryMode.ENTRY, plan.getRootPlan().getStaticMode());
  }
  
  
}
