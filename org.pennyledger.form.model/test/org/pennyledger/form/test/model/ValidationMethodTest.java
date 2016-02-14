package org.pennyledger.form.test.model;

import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.GroupModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.plan.IGroupPlan;
import org.pennyledger.form.plan.IValidationMethod;
import org.pennyledger.util.UserEntryException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@SuppressWarnings("unused")
public class ValidationMethodTest {

  private static class TestClass1 {
    
    String field0;
    
    TestClass1 () {
      this.field0 = "xyz";
    }
    
    TestClass1 (String field0) {
      this.field0 = field0;
    }
    
    void validate1 () throws UserEntryException {
      if (!"abc".equals(field0)) {
        throw new UserEntryException("Field 0 not 'abc'");
      }
    }
    
    
    static void validate2 () throws UserEntryException {
    }

    void validate3 () throws Exception {
    }

    void validate4 (String arg1) throws UserEntryException {
    }

    String validate5 () throws UserEntryException {
      return "";
    }

  }


  private IGroupPlan groupPlan;
  
  
  @Before
  public void setup () {
    IFormModel<TestClass1> formModel = new FormModel<TestClass1>(TestClass1.class);
    GroupModel groupModel = (GroupModel)formModel.getRootModel();
    groupPlan = groupModel.getPlan();
  }
  
  
  @Test
  public void ignoredMethods () {
    Assert.assertEquals (1, groupPlan.getValidationMethods().size());
  }
  
  
  @Test
  public void simpleFailure () {
    IValidationMethod validationMethod = groupPlan.getValidationMethods().iterator().next();
    TestClass1 instance = new TestClass1("xyz");
    try {
      validationMethod.validate(instance);
      Assert.fail("Expecting DataEntryException");
    } catch (UserEntryException ex) {
   }
  }

  
  @Test
  public void simpleSucess () {
    IValidationMethod validationMethod = groupPlan.getValidationMethods().iterator().next();
    TestClass1 instance = new TestClass1("abc");
    try {
      validationMethod.validate(instance);
    } catch (UserEntryException ex) {
      Assert.fail("Not expecting DataEntryException");
    }
  }

}
