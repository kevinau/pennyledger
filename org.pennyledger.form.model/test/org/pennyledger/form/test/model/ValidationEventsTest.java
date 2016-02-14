package org.pennyledger.form.test.model;

import org.pennyledger.form.FormField;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.util.UserEntryException;
import org.junit.Test;

public class ValidationEventsTest {

  @SuppressWarnings("unused")
  public static class TestData {
    private String code; 
    private String description;
    private int primitiveInt;
    @FormField(nullable=true)
    private int primitiveIntOptional;
    @FormField(nullable=false)
    private Integer intObjectRequired;
    @FormField(nullable=false)
    private Integer intObjectWithDefault = new Integer(0);
    @FormField(nullable=true)
    private Integer intObjectOptional;
    
    private void validateCode () throws UserEntryException {
      int cv = Integer.parseInt(code);
      if (cv < 1 || cv > 26) {
        throw new UserEntryException("Code value not in range 0001 to 0026");
      }
    }
  }
  
  
  @Test
  public void testFailedValidation () {
    TestData instance = new TestData();
    IFormModel<TestData> formModel = new FormModel<TestData>(instance);
    IFieldModel codeModel = formModel.selectFieldModel("//code");
    IFieldModel intModel0 = formModel.selectFieldModel("//primitiveInt");
    IFieldModel intModel1 = formModel.selectFieldModel("//intObjectRequired");
    IFieldModel intModel2 = formModel.selectFieldModel("//intObjectWithDefault");
    EventCounter counter = new EventCounter();
    intModel0.addFieldEventListener(counter);
    intModel1.addFieldEventListener(counter);
    intModel2.addFieldEventListener(counter);
    codeModel.setValueFromSource("0001");
    counter.print();
    codeModel.setValueFromSource("");
    counter.print();
  }
  
  
  @Test
  public void testValueChangeAttempted () {
    TestData instance = new TestData();
    IFormModel<TestData> formModel = new FormModel<TestData>(instance);
    IFieldModel codeModel = formModel.selectFieldModel("//code");
    IFieldModel intModel0 = formModel.selectFieldModel("//primitiveInt");
    IFieldModel intModel1 = formModel.selectFieldModel("//intObjectRequired");
    IFieldModel intModel2 = formModel.selectFieldModel("//intObjectWithDefault");
    EventCounter counter = new EventCounter();
    intModel0.addFieldEventListener(counter);
    intModel1.addFieldEventListener(counter);
    intModel2.addFieldEventListener(counter);
    codeModel.setValueFromSource("0001", null);
    counter.print();
    codeModel.setValueFromSource("", null);
    counter.print();
    codeModel.setValue(null);
    counter.print();
  }
}
