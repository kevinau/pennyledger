package org.pennyledger.form.test;

import javax.persistence.Embedded;

import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.plan.IClassPlan;
import org.pennyledger.form.plan.IFieldPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.impl.ClassPlan;

public class EmbeddedClassPlan {

  public static class Inner {
    private String field21;
  }
  
  public static class Outer {
    private String field1;
    @Embedded
    private Inner field2;
  }
  
  @Test
  public void test() {
    IClassPlan<Outer> plan = new ClassPlan<Outer>(Outer.class);
    IObjectPlan[] members = plan.getMemberPlans();
    Assert.assertEquals(2, members.length);
    Assert.assertTrue(members[0] instanceof IFieldPlan);
    Assert.assertTrue(members[1] instanceof IClassPlan);
    
    IClassPlan<?> plan2 = (IClassPlan<?>)members[1];
    IObjectPlan[] members2 = plan2.getMemberPlans();
    Assert.assertEquals(1, members2.length);
    Assert.assertTrue(members2[0] instanceof IFieldPlan);
  }

}
