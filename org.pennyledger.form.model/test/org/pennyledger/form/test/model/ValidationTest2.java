package org.pennyledger.form.test.model;

import org.pennyledger.form.FormField;
import org.pennyledger.form.model.FieldEventAdapter;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.util.UserEntryException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class ValidationTest2 {

  @SuppressWarnings("unused")
  private static class TestClass {
    @FormField
    Integer field0 = 0;
    
    @FormField
    Integer field1 = 0;
    
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
  }

  
  private TestClass instance;
  private IFormModel<TestClass> formModel;
  
  private IFieldModel model0;
  private IFieldModel model1;
  
  private int errorNoted = 0;
  
  
  @Before
  public void setup () {
    instance = new TestClass();
    formModel = new FormModel<TestClass>(instance);
    
    model0 = formModel.selectFieldModel("/*/field0");
    model1 = formModel.selectFieldModel("/*/field1");
    
    model0.addFieldEventListener(new FieldEventAdapter() {
      @Override
      public void errorCleared(IFieldModel model) {
      }

      @Override
      public void errorNoted(IFieldModel model, UserEntryException ex) {
        errorNoted++;
      }
    });
    model1.addFieldEventListener(new FieldEventAdapter() {
      @Override
      public void errorCleared(IFieldModel model) {
      }

      @Override
      public void errorNoted(IFieldModel model, UserEntryException ex) {
        errorNoted++;
      }
    });
  }

  
  @Test
  public void checkSetup () {
    Assert.assertTrue (model0 != null);
    Assert.assertTrue (model1 != null);
    
    Assert.assertEquals (0, (int)instance.field0);
    Assert.assertEquals (0, (int)instance.field1);
    Assert.assertEquals (0, errorNoted);
    
    model1.setValue(new Integer(-100));
    System.out.println(model0.getStatus() + " " + model0.getStatusMessage());
    System.out.println(model1.getStatus() + " " + model1.getStatusMessage());
    System.out.println();
    
    model0.setValue(new Integer(124));
    System.out.println(model0.getStatus() + " " + model0.getStatusMessage());
    System.out.println(model1.getStatus() + " " + model1.getStatusMessage());
    System.out.println();
    
    model0.setValue(new Integer(123));
    System.out.println(model0.getStatus() + " " + model0.getStatusMessage());
    System.out.println(model1.getStatus() + " " + model1.getStatusMessage());
    System.out.println();
  }
  
}
