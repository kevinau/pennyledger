package org.pennyledger.form.test.plan;

import java.util.List;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.FormField;
import org.pennyledger.form.Mode;
import org.pennyledger.form.ModeFor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.plan.IGroupPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.IRuntimeModeProvider;
import org.pennyledger.form.plan.impl.ObjectPlanFactory;


public class AnnotatedInitialModeTest {

  @SuppressWarnings("unused")
  private static class TestClass1 {
    String field0;
    
    @Mode(EntryMode.ENTRY)
    String field1;
    
    static EntryMode field2Mode = EntryMode.VIEW;
    
    String field2;
    
    static EntryMode field3Mode() {
      return EntryMode.VIEW;
    }
    
    String field3;

    @ModeFor("field4")
    static EntryMode field4ModeDefn = EntryMode.NA;
    
    String field4;

    @ModeFor("field5")
    static EntryMode field5ModeDefn() {
      return EntryMode.NA;
    }
    
    String field5;

    @FormField ()
    String field6;
    
    String field7;
    
    @ModeFor("field7")
    EntryMode field7ModeDefn() {
      return EntryMode.NA;
    }

    EntryMode field8Mode() {
      return EntryMode.NA;
    }
    
    String field8;

  }


  private IGroupPlan groupPlan;

  
  @Before
  public void setup () {
    groupPlan = ObjectPlanFactory.buildGroupPlan(TestClass1.class);
  }

  
  private EntryMode getStaticMode (String fieldName) {
    IObjectPlan fieldPlan = groupPlan.getMemberPlan(fieldName);
    EntryMode Mode = fieldPlan.getStaticMode();
    return Mode;
  }


  @Test
  public void checkSetup () {
    Assert.assertEquals (9, groupPlan.getMemberPlans().length);
  }
  
  
  @Test
  public void noModeSpecified () {
    EntryMode Mode = getStaticMode("field0");
    Assert.assertEquals(EntryMode.UNSPECIFIED, Mode);
  }

  @Test
  public void formFieldMode () {
    EntryMode Mode = getStaticMode("field1");
    Assert.assertEquals(EntryMode.ENTRY, Mode);
  }

  @Test
  public void conventionModeFieldMode () {
    EntryMode Mode = getStaticMode("field2");
    Assert.assertEquals(EntryMode.VIEW, Mode);
  }

  @Test
  public void conventionModeMethodMode () {
    EntryMode Mode = getStaticMode("field3");
    Assert.assertEquals(EntryMode.VIEW, Mode);
  }

  @Test
  public void ModeForFieldMode () {
    EntryMode Mode = getStaticMode("field4");
    Assert.assertEquals(EntryMode.UNSPECIFIED, Mode);
  }

  @Test
  public void ModeForMethodMode () {
    EntryMode Mode = getStaticMode("field5");
    Assert.assertEquals(EntryMode.UNSPECIFIED, Mode);
  }
  
  @Test
  public void emptyFormFieldMode () {
    EntryMode Mode = getStaticMode("field6");
    Assert.assertEquals(EntryMode.UNSPECIFIED, Mode);
  }
  
  @Test
  public void classMethodByConvention () {
    EntryMode Mode = getStaticMode("field8");
    Assert.assertEquals(EntryMode.UNSPECIFIED, Mode);
  }
  
  @Test
  public void runtimeProviders () {
    // For fields 4, 5, 7 and 8
    List<IRuntimeModeProvider> providers = groupPlan.getRuntimeModeProviders();
    Assert.assertEquals(4, providers.size());
    Object x = new TestClass1();
    for (IRuntimeModeProvider provider : providers) {
      EntryMode m = provider.getEntryMode(x);
      Assert.assertEquals(EntryMode.NA, m);
    }
  }

}
