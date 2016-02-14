package org.pennyledger.form.test.plan;

import java.util.List;

import org.pennyledger.form.EntryMode;
import org.pennyledger.form.ModeFor;
import org.pennyledger.form.NotFormField;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.plan.IGroupPlan;
import org.pennyledger.form.plan.IRuntimeModeProvider;
import org.pennyledger.form.plan.impl.ObjectPlanFactory;


/**
 * Tests instance fields and methods that provide ControlMode information.
 * 
 * @author Kevin Holloway
 *
 */
public class AnnotatedRuntimeModeTest {

  @SuppressWarnings("unused")
  private static class TestClass {
    
    static int flag = 0;
    
    @NotFormField
    int state;
    
    String field1;

    EntryMode field1Mode() {
      if (state == 0) {
        return EntryMode.VIEW;
      } else {
        return EntryMode.NA; 
      }
    }
    
    String field2;

    @ModeFor("field2")
    static EntryMode field2ModeField = EntryMode.VIEW; 

    String field3;

    @ModeFor("field3")
    EntryMode field3ModeMethod() {
      if (state == 0) {
        return EntryMode.VIEW;
      } else {
        return EntryMode.NA; 
      }
    }

    String field4;

    @ModeFor("field4")
    static EntryMode field4ModeMethod() {
      if (flag == 0) {
        return EntryMode.VIEW;
      } else {
        return EntryMode.NA;
      }
    }
  }


  private IGroupPlan groupPlan;
  private List<IRuntimeModeProvider> providers;
  
  
  @Before
  public void setup () {
    groupPlan = ObjectPlanFactory.buildGroupPlan(TestClass.class);
    providers = groupPlan.getRuntimeModeProviders();
  }

  
  @Test
  public void checkSetup () {
    Assert.assertEquals (4, groupPlan.getMemberPlans().length);
    Assert.assertEquals (4, providers.size());
  }
  
  
  @Test
  public void staticProviders () {
    TestClass instance = new TestClass();
    instance.state = 0;
    for (IRuntimeModeProvider provider : providers) {
      EntryMode mode = provider.getEntryMode(instance);
      Assert.assertEquals(EntryMode.VIEW, mode);
    }
  }

  @Test
  public void runtimeProviders () {
    TestClass instance = new TestClass();
    instance.state = 10;
    for (IRuntimeModeProvider provider : providers) {
      if (provider.isRuntime()) {
        EntryMode mode = provider.getEntryMode(instance);
        Assert.assertEquals(EntryMode.NA, mode);
      }
    }
  }

}
