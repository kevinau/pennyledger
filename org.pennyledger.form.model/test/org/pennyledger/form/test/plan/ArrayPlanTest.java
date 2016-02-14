package org.pennyledger.form.test.plan;

import javax.persistence.Embeddable;

import org.pennyledger.form.Occurs;
import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.plan.IArrayPlan;
import org.pennyledger.form.plan.IGroupPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.impl.ObjectPlanFactory;

@SuppressWarnings("unused")
public class ArrayPlanTest {

  @Embeddable
  private static class InnerClass {
    private Integer field2;
  }
  
  private static class TestClass0 {
    private InnerClass[] array = new InnerClass[3];
  }
  
  
  private static class TestClass1 {
    @Occurs(4)
    private InnerClass[] array;
  }
  
  
  @Test
  public void testInstanceArray () {
    IGroupPlan groupPlan = ObjectPlanFactory.buildGroupPlan(TestClass0.class);
    IObjectPlan objectPlan = groupPlan.getMemberPlan("array");
    Assert.assertNotNull(objectPlan);
    Assert.assertTrue("instance of IArrayPlan", objectPlan instanceof IArrayPlan);
    IArrayPlan arrayPlan = (IArrayPlan)objectPlan;
    Assert.assertEquals(Integer.MAX_VALUE, arrayPlan.getMaxSize());
  }


  @Test
  public void testStaticOccursSize () {
    IGroupPlan groupPlan = ObjectPlanFactory.buildGroupPlan(TestClass1.class);
    IArrayPlan arrayPlan = (IArrayPlan)groupPlan.getMemberPlan("array");
    Assert.assertEquals(4, arrayPlan.getMaxSize());
  }
  
}