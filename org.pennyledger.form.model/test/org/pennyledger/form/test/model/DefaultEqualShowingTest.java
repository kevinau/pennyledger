package org.pennyledger.form.test.model;

import org.pennyledger.form.model.EffectiveMode;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.util.UserEntryException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class DefaultEqualShowingTest {
  
  @SuppressWarnings("unused")
  public static class TestData {
    private Integer number;
    private String code;
    
    private void validateCode () throws UserEntryException {
      int cv = Integer.parseInt(code);
      if (cv < 1 || cv > 26) {
        throw new UserEntryException("Code value not in range 0001 to 0026");
      }
    }
  }
  

  private TestData instance;
  private IFormModel<TestData> formModel;
  
  
  @Before
  public void setup () {
    instance = new TestData();
    formModel = new FormModel<TestData>(instance);
  }
  
  
  @Test
  public void errorFieldTest () {
    IFieldModel numberModel = formModel.selectFieldModel("number");

    Assert.assertEquals(EffectiveMode.ENTRY, numberModel.getEffectiveMode());
    Assert.assertEquals(true, numberModel.isComparedValueEqual());
    Assert.assertEquals(true, numberModel.isComparedSourceEqual());
    
    numberModel.setValueFromSource("xxxx");
    Assert.assertEquals(EffectiveMode.ENTRY, numberModel.getEffectiveMode());
    Assert.assertEquals(true, numberModel.isComparedValueEqual());
    Assert.assertEquals(false, numberModel.isComparedSourceEqual());

    numberModel.setValueFromSource("0001");
    Assert.assertEquals(EffectiveMode.ENTRY, numberModel.getEffectiveMode());
    Assert.assertEquals(false, numberModel.isComparedValueEqual());
    Assert.assertEquals(false, numberModel.isComparedSourceEqual());
  }
    
  @Test
  public void errorMethodTest () {
    IFieldModel codeModel = formModel.selectFieldModel("code");

    Assert.assertEquals(EffectiveMode.ENTRY, codeModel.getEffectiveMode());
    Assert.assertEquals(true, codeModel.isComparedValueEqual());
    Assert.assertEquals(true, codeModel.isComparedSourceEqual());
    
    codeModel.setValueFromSource("xxxx");
    Assert.assertEquals(EffectiveMode.ENTRY, codeModel.getEffectiveMode());
    Assert.assertEquals(true, codeModel.isComparedValueEqual());
    Assert.assertEquals(false, codeModel.isComparedSourceEqual());

    codeModel.setValueFromSource("0001");
    Assert.assertEquals(EffectiveMode.ENTRY, codeModel.getEffectiveMode());
    Assert.assertEquals(false, codeModel.isComparedValueEqual());
    Assert.assertEquals(false, codeModel.isComparedSourceEqual());
  }
    
}
