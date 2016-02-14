package org.pennyledger.form.test.plan;

import java.util.List;

import org.pennyledger.form.LabelFor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.plan.IGroupPlan;
import org.pennyledger.form.plan.IRuntimeLabelProvider;
import org.pennyledger.form.plan.impl.ObjectPlanFactory;


/**
 * Tests instance fields and methods that provide label information.
 * 
 * @author Kevin Holloway
 *
 */
public class AnnotatedRuntimeLabelTest {

  @SuppressWarnings("unused")
  private static class TestClass {
    
    String field2;

    @LabelFor("field2")
    static String field2LabelField = "Field two"; 

    String field4;

    @LabelFor("field4")
    static String field4LabelMethod() {
      return "Field four";
    }
  }


  private IGroupPlan groupPlan;
  private List<IRuntimeLabelProvider> providers;
  
  
  @Before
  public void setup () {
    groupPlan = ObjectPlanFactory.buildGroupPlan(TestClass.class);
    providers = groupPlan.getRuntimeLabelProviders();
  }

  
  @Test
  public void checkSetup () {
    Assert.assertEquals (2, groupPlan.getMemberPlans().length);
    Assert.assertEquals (2, providers.size());
  }
  
  
  @Test
  public void staticProviders () {
    IRuntimeLabelProvider provider;
    provider = providers.get(0);
    String label = provider.getLabel();
    Assert.assertEquals("Field two", label);
    provider = providers.get(1);
    label = provider.getLabel();
    Assert.assertEquals("Field four", label);
  }

}
