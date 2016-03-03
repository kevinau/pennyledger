package org.pennyledger.form.test;

import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.plan.IClassPlan;
import org.pennyledger.form.plan.IFieldPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.impl.ClassPlan;
import org.pennyledger.form.type.IType;

public class ClassOfPrimitiveWrappers {

  public static class Klass {
    @SuppressWarnings("unused")
    private Byte field1;
    @SuppressWarnings("unused")
    private Short field2;
    @SuppressWarnings("unused")
    private Integer field3;
    @SuppressWarnings("unused")
    private Long field4;
    @SuppressWarnings("unused")
    private Float field5;
    @SuppressWarnings("unused")
    private Double field6;
    @SuppressWarnings("unused")
    private Boolean field7;
    @SuppressWarnings("unused")
    private Character field8;
  }
  
  @Test
  public void test() {
    IClassPlan<Klass> plan = new ClassPlan<Klass>(Klass.class);
    IObjectPlan[] members = plan.getMemberPlans();
    Assert.assertEquals(8, members.length);
    for (int i = 0; i < members.length; i++) {
      Assert.assertTrue("Field " + (i + 1), members[i] instanceof IFieldPlan);
      
      IFieldPlan fieldPlan = (IFieldPlan)members[i];
      IType<?> type = fieldPlan.getType();
      Assert.assertEquals("Field " + (i + 1), false, type.isPrimitive());
    }
  }

}
