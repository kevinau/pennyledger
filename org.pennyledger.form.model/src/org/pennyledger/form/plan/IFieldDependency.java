package org.pennyledger.form.plan;

import java.util.List;

public interface IFieldDependency {

  public List<String> getDependencies (String className, String methodName);

}
