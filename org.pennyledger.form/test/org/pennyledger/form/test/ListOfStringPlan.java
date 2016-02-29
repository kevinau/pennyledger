package org.pennyledger.form.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.plan.IClassPlan;
import org.pennyledger.form.plan.IFieldPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.IRepeatingPlan;
import org.pennyledger.form.plan.PlanKind;
import org.pennyledger.form.plan.impl.ClassPlan;
import org.pennyledger.form.type.IType;
import org.pennyledger.form.type.builtin.StringType;

public class ListOfStringPlan {

  public static class Outer {
    @SuppressWarnings("unused")
    private List<String> field1;
  }
  
  @Test
  public void test() {
    IClassPlan<Outer> plan = new ClassPlan<Outer>(Outer.class);
    IObjectPlan[] members = plan.getMemberPlans();
    Assert.assertEquals(1, members.length);

    IObjectPlan plan2 = members[0];
    Assert.assertEquals(PlanKind.REPEATING, plan2.kind());
    Assert.assertTrue(plan2 instanceof IRepeatingPlan);
    
    IObjectPlan plan3 = ((IRepeatingPlan)plan2).getElementPlan();
    Assert.assertEquals(PlanKind.FIELD , plan3.kind());
    Assert.assertTrue(plan3 instanceof IFieldPlan);
    
    IType<?> type = ((IFieldPlan)plan3).getType();
    Assert.assertTrue(type instanceof StringType);
  }
  
  public static void main (String[] args) {
    ListOfStringPlan instance = new ListOfStringPlan();
    instance.test();
  }
}
