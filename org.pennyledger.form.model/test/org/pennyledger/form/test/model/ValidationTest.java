package org.pennyledger.form.test.model;

import java.util.Set;

import org.pennyledger.form.FormField;
import org.pennyledger.form.model.FieldEventAdapter;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.GroupModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IGroupModel;
import org.pennyledger.form.plan.IGroupPlan;
import org.pennyledger.form.plan.IValidationMethod;
import org.pennyledger.util.UserEntryException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class ValidationTest {

  @SuppressWarnings("unused")
  private static class TestClass {
    @FormField
    Integer field0 = new Integer(0);
    
    @FormField
    Integer field1 = new Integer(0);
    
    public void field0Positive () throws UserEntryException {
      System.out.println("Field 0 positive: " + field0);
      if (field0 < 0) {
        System.out.println("  Exception thrown");
        throw new UserEntryException("Number must be zero or positive");
      }
    }
    
    public void field0Even () throws UserEntryException {
      System.out.println("Field 0 even: " + field0);
      if (field0 % 2 != 0) {
        System.out.println("  Exception thrown");
        throw new UserEntryException("Number must be even");
      }
    }
    
    public void sumZero () throws UserEntryException {
      System.out.println("Sum zero: " + field0 + " " + field1 + ": " + (field0 + field1));
      if (field0 + field1 != 0) {
        System.out.println("  Exception thrown");
        throw new UserEntryException("Numbers must add up to zero");
      }
    }

    
    public void field1Negative () throws UserEntryException {
      System.out.println("Field 1 negative: " + field1);
      if (field1 > 0) {
        System.out.println("  Exception thrown");
        throw new UserEntryException("Number must be zero or negative");
      }
    }
    
  }

  
  private TestClass instance;
  private IFormModel<TestClass> formModel;
  
  private IFieldModel model0;
  private IFieldModel model1;
  
  private int error0Noted = 0;
  private int error0Cleared = 0;
  
  private int error1Noted = 0;
  private int error1Cleared = 0;
  
  
  @Before
  public void setup () {
    instance = new TestClass();
    formModel = new FormModel<TestClass>(instance);
    
    model0 = formModel.selectFieldModel("/*/field0");
    model1 = formModel.selectFieldModel("/*/field1");
    
    model0.addFieldEventListener(new FieldEventAdapter() {
      @Override
      public void errorCleared(IFieldModel model) {
        error0Cleared++;
      }

      @Override
      public void errorNoted(IFieldModel model, UserEntryException ex) {
        error0Noted++;
      }
    });
    model1.addFieldEventListener(new FieldEventAdapter() {
      @Override
      public void errorCleared(IFieldModel model) {
        error1Cleared++;
      }

      @Override
      public void errorNoted(IFieldModel model, UserEntryException ex) {
        error1Noted++;
      }
    });
  }

  
  @Test
  public void checkSetup () {
    System.out.println("check setup -------------------------");
    Assert.assertTrue (model0 != null);
    Assert.assertTrue (model1 != null);
    
    Assert.assertEquals (0, (int)instance.field0);
    Assert.assertEquals (0, (int)instance.field1);
    Assert.assertEquals (0, error0Noted);
    Assert.assertEquals (0, error1Noted);
  }

  
  @Test
  public void orderOfValidations () {
    IGroupModel groupModel = formModel.getRootModel();
    IGroupPlan groupPlan = ((GroupModel)groupModel).getPlan();
    Set<IValidationMethod> validationMethods = groupPlan.getValidationMethods();
    Assert.assertEquals (4, validationMethods.size());
    
    int n = 0;
    System.out.println("order of validations");
    for (IValidationMethod vm : validationMethods) {
      System.out.println(vm.getMethodName() + ": " + vm.getOrder());
      Assert.assertTrue(vm.getOrder() >= n);
      n = vm.getOrder();
    }
  }

  
  @Test
  public void noError () {
    System.out.println("noError -------------------------");
    model0.setValueFromSource("0");
    //model1.setValueFromSource("0");
    
    Assert.assertEquals (0, (int)instance.field0);
    Assert.assertEquals (0, (int)instance.field1);
    Assert.assertEquals (0, error0Noted);
    Assert.assertEquals (0, error1Noted);
    Assert.assertEquals (1, error0Cleared);
    //Assert.assertEquals (1, error1Cleared);
  }

  
  @Test
  public void singleError () {
    System.out.println("singleError -------------------------");
    model0.setValueFromSource("0");
    model1.setValueFromSource("123");
    
    Assert.assertEquals (0, (int)instance.field0);
    Assert.assertEquals (123, (int)instance.field1);
    Assert.assertEquals (0, error0Noted);
    Assert.assertEquals (1, error1Noted);
    Assert.assertEquals (1, error0Cleared);
    Assert.assertEquals (1, error1Cleared);
  }
  
  
  @Test
  public void twoErrorsOnOneField () {
    System.out.println("two errors on one field -------------------------");
    model0.setValueFromSource("-123");
    model1.setValueFromSource("0");
    
    Assert.assertEquals (-123, (int)instance.field0);
    Assert.assertEquals (0, (int)instance.field1);
    Assert.assertEquals (2, error0Noted);
    Assert.assertEquals (0, error1Noted);
    Assert.assertEquals (1, error0Cleared);
    Assert.assertEquals (1, error1Cleared);
  }  


  @Test
  public void levelTwoWithNoError () {
    System.out.println("level two with no error -------------------------");
    model0.setValueFromSource("124");
    model1.setValueFromSource("-124");
    
    Assert.assertEquals (124, (int)instance.field0);
    Assert.assertEquals (-124, (int)instance.field1);
    // An error "does not add up to zero" is thrown when the model0 value is set,
    // and reported on both fields.  The error is cleared when the model1 value
    // is set, and this is also reported on both fields.
    Assert.assertEquals (1, error0Noted);
    Assert.assertEquals (1, error1Noted);
    Assert.assertEquals (2, error0Cleared);
    Assert.assertEquals (2, error1Cleared);
  }  

  
  @Test
  public void levelTwoError () {
    System.out.println("level two errors -------------------------");
    model0.setValueFromSource("124");
    model1.setValueFromSource("-125");
    
    Assert.assertEquals (124, (int)instance.field0);
    Assert.assertEquals (-125, (int)instance.field1);
    // There is one error when the model0 value is set, and one when the model1 
    // error is set; and each error is reported on both fields.
    Assert.assertEquals (2, error0Noted);
    Assert.assertEquals (2, error1Noted);
    Assert.assertEquals (1, error0Cleared);
    Assert.assertEquals (1, error1Cleared);
  }  
  

  @Test
  public void levelTwoErrorPlusFix () {
    System.out.println("level two error plus fix -------------------------");
    model0.setValueFromSource("124");
    model1.setValueFromSource("-125");
    model1.setValueFromSource("-124");
    
    Assert.assertEquals (124, (int)instance.field0);
    Assert.assertEquals (-124, (int)instance.field1);
    // There is one error when the model0 value is set, and one when the model1 
    // error is set; and each error is reported on both fields.
    Assert.assertEquals (2, error0Noted);
    Assert.assertEquals (2, error1Noted);
    // The clearing of the error is reported on both fields.
    Assert.assertEquals (2, error0Cleared);
    Assert.assertEquals (2, error1Cleared);
  }  

}
