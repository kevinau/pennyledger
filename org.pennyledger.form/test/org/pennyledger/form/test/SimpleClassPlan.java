package org.pennyledger.form.test;

import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.plan.IClassPlan;
import org.pennyledger.form.plan.IFieldPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.impl.ClassPlan;

public class SimpleClassPlan {

  public static class Klass {
    private String field0;
    private byte field1;
    private short field2;
    private int field3;
    private long field4;
    private float field5;
    private double field6;
    private boolean field7;
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
