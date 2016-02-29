package org.pennyledger.form.test;

import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.Occurs;
import org.pennyledger.form.plan.IClassPlan;
import org.pennyledger.form.plan.IFieldPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.IRepeatingPlan;
import org.pennyledger.form.plan.PlanKind;
import org.pennyledger.form.plan.impl.ClassPlan;

public class ArrayArrayOfStringPlan {

  public static class Outer {
    @Occurs(max={5,10})
    private String[][] field1;
  }
  
  @Test
  public void test() {
    IClassPlan<Outer> plan = new ClassPlan<Outer>(Outer.class);
    IObjectPlan[] members = plan.getMemberPlans();
    Assert.assertEquals(1, members.length);

    IObjectPlan plan1 = members[0];
    Assert.assertEquals(PlanKind.REPEATING, plan1.kind());
    Assert.assertTrue(plan1 instanceof IRepeatingPlan);
    Assert.assertEquals(5, ((IRepeatingPlan)plan1).getMaxOccurs());
    
    IObjectPlan plan2 = ((IRepeatingPlan)plan1).getElementPlan();
    Assert.assertEquals(PlanKind.REPEATING, plan2.kind());
    Assert.assertTrue(plan2 instanceof IRepeatingPlan);
    Assert.assertEquals(10, ((IRepeatingPlan)plan2).getMaxOccurs());
    
    IObjectPlan plan3 = ((IRepeatingPlan)plan2).getElementPlan();
    Assert.assertEquals(PlanKind.FIELD , plan3.kind());
    Assert.assertTrue(plan3 instanceof IFieldPlan);
  }
}
