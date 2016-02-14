package org.pennyledger.form.test.plan;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.Mode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.plan.IFormPlan;
import org.pennyledger.form.plan.IGroupPlan;
import org.pennyledger.form.plan.impl.FormPlan;
import org.pennyledger.form.plan.impl.ObjectPlanFactory;


public class AnnotatedInitialModeTest2 {

  @SuppressWarnings("unused")
  @Mode(EntryMode.VIEW)
  private static class TestClass0 {
    String field0;
  }


  private IGroupPlan groupPlan;

  
  @Before
  public void setup () {
    groupPlan = ObjectPlanFactory.buildGroupPlan(TestClass0.class);
  }

  
//  private EntryMode getStaticMode (String fieldName) {
//    IObjectPlan fieldPlan = groupPlan.getMemberPlan(fieldName);
//    EntryMode Mode = fieldPlan.getStaticMode();
//    return Mode;
//  }


  @Test
  public void checkSetup () {
    Assert.assertEquals (1, groupPlan.getMemberPlans().length);
  }
  
  
  @Test
  public void formMode() {
    IFormPlan formPlan = new FormPlan(TestClass0.class);
    EntryMode Mode = formPlan.getEntryMode();
    Assert.assertEquals(EntryMode.VIEW, Mode);
  }

}
