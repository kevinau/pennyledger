package org.pennyledger.form.plan;

import org.pennyledger.form.plan.impl.FormPlan;

public class FormPlanFactory {

  public static IFormPlan create(Class<?> klass) {
    return new FormPlan(klass);
  }
  
}
