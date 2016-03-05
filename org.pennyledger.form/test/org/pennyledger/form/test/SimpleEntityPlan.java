package org.pennyledger.form.test;

import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.Entity;
import org.pennyledger.form.factory.Form;
import org.pennyledger.form.plan.IEntityPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.value.IForm;

public class SimpleEntityPlan {

  @Entity
  public static class Klass1 {
    @SuppressWarnings("unused")
    private String field1;
    @SuppressWarnings("unused")
    private String field2;
  }
  
  @Test
  public void testUnAnnotatedClass () {
    IForm<Klass1> form = new Form<>(Klass1.class);
    
    IObjectPlan plan = form.getPlan();
    Assert.assertTrue(plan instanceof IEntityPlan);
  }
}
