package org.pennyledger.form.test;

import javax.persistence.Embedded;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.Optional;
import org.pennyledger.form.factory.Form;
import org.pennyledger.form.plan.IClassPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.impl.ClassPlan;
import org.pennyledger.form.value.IForm;

public class OptionalClassMemberTest {

  private static class Inner {
    @SuppressWarnings("unused")
    @Optional
    private String field11;
  }
  
  private static class Outer {
    @Embedded
    @Optional
    private Inner field1 = new Inner();
    @SuppressWarnings("unused")
    private String field2;
  }
  
  private IForm<Outer> form;
  private Outer value;
  
  @Before
  public void setup() {
    form = new Form<>(Outer.class);
    value = new Outer();
    value.field1.field11 = "abc";
    value.field2 = "def";
    form.setValue(value);
  }
  

  @Test
  public void test() {
    IClassPlan<Outer> plan = new ClassPlan<>(Outer.class);
    IObjectPlan[] members = plan.getMemberPlans();
    Assert.assertEquals(2, members.length);

    Assert.assertEquals(true, members[0].isOptional());
    Assert.assertEquals(false, members[1].isOptional());
    
    Assert.assertTrue(members[0] instanceof IClassPlan);
    IClassPlan<?> field1Plan = (IClassPlan<?>)members[0];
    IObjectPlan[] innerMembers = field1Plan.getMemberPlans();
    Assert.assertEquals(1, innerMembers.length);

    Assert.assertEquals(true, innerMembers[0].isOptional());
  }

}
