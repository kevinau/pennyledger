package org.pennyledger.form.test;

import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.plan.IClassPlan;
import org.pennyledger.form.plan.IFieldPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.impl.ClassPlan;

public class SimpleClassPlan {

  public static class Klass {
    @SuppressWarnings("unused")
    private String field0;
    @SuppressWarnings("unused")
    private byte field1;
    @SuppressWarnings("unused")
    private short field2;
    @SuppressWarnings("unused")
    private int field3;
    @SuppressWarnings("unused")
    private long field4;
    @SuppressWarnings("unused")
    private float field5;
    @SuppressWarnings("unused")
    private double field6;
    @SuppressWarnings("unused")
    private boolean field7;
    @SuppressWarnings("unused")
    private char field8;
  }
  
  @Test
  public void test() {
    IClassPlan<Klass> plan = new ClassPlan<Klass>(Klass.class);
    IObjectPlan[] members = plan.getMemberPlans();
    Assert.assertEquals(9, members.length);
    for (int i = 0; i < members.length; i++) {
      Assert.assertTrue(members[i] instanceof IFieldPlan);
    }
  }

}
