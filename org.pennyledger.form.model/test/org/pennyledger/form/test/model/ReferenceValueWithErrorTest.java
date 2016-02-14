package org.pennyledger.form.test.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pennyledger.form.model.ComparisonBasis;
import org.pennyledger.form.model.FieldEventAdapter;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;

public class ReferenceValueWithErrorTest {

  @SuppressWarnings("unused")
  private static class TestClass {
    Integer field0 = 123;

  }

  private IFormModel<TestClass> formModel;
  private IFieldModel fieldModel;
  private ReferenceChangeCountListener x;

  private static class ReferenceChangeCountListener extends FieldEventAdapter {

    private int equalCount = 0;
    private int notEqualCount = 0;
    private int showingCount = 0;
    private int notShowingCount = 0;

    @Override
    public void compareEqualityChange(IFieldModel model) {
      if (model.isComparedValueEqual()) {
        equalCount++;
      } else {
        notEqualCount++;
      }
    }
    
    @Override
    public void compareShowingChange(IFieldModel model, boolean isSourceTrigger) {
      if (model.isComparedSourceEqual()) {
        showingCount++;
      } else {
        notShowingCount++;
      }
    }
  }

  @Before
  public void setup() {
    formModel = new FormModel<TestClass>(TestClass.class);
    fieldModel = formModel.selectFieldModel("/form/field0");
    x = new ReferenceChangeCountListener();
    fieldModel.addFieldEventListener(x);
    fieldModel.setValueFromDefault();
  }

  @Test
  public void initialState() {
    Assert.assertEquals(1, x.equalCount);
    Assert.assertEquals(0, x.notEqualCount);
    Assert.assertEquals(1, x.showingCount);
    Assert.assertEquals(0, x.notShowingCount);
    Assert.assertEquals(true, fieldModel.isComparedValueEqual());
    Assert.assertEquals(true, fieldModel.isComparedSourceEqual());
  }
  
  
  @Test
  public void initialState2() {
    fieldModel.setCompareBasis(ComparisonBasis.REFERENCE);
    Assert.assertEquals(null, fieldModel.getReferenceValue());
    Assert.assertEquals(1, x.equalCount);
    Assert.assertEquals(1, x.notEqualCount);
    Assert.assertEquals(1, x.showingCount);
    Assert.assertEquals(1, x.notShowingCount);
    Assert.assertEquals(false, fieldModel.isComparedValueEqual());
    Assert.assertEquals(false, fieldModel.isComparedSourceEqual());
  }
  
  
  @Test
  public void fieldReferenceChange() {
    fieldModel.setCompareBasis(ComparisonBasis.REFERENCE);
    Assert.assertEquals(null, fieldModel.getReferenceValue());
    Assert.assertEquals(1, x.equalCount);
    Assert.assertEquals(1, x.notEqualCount);
    Assert.assertEquals(1, x.showingCount);
    Assert.assertEquals(1, x.notShowingCount);
    Assert.assertEquals(false, fieldModel.isComparedValueEqual());
    Assert.assertEquals(false, fieldModel.isComparedSourceEqual());

    fieldModel.setReferenceValue (new Integer(123));
    Assert.assertEquals(new Integer(123), fieldModel.getReferenceValue());
    Assert.assertEquals(2, x.equalCount);
    Assert.assertEquals(1, x.notEqualCount);
    Assert.assertEquals(2, x.showingCount);
    Assert.assertEquals(1, x.notShowingCount);
    Assert.assertEquals(true, fieldModel.isComparedValueEqual());
    Assert.assertEquals(true, fieldModel.isComparedSourceEqual());

    fieldModel.setValueFromSource ("xx");
    Assert.assertEquals(2, x.equalCount);
    Assert.assertEquals(1, x.notEqualCount);
    Assert.assertEquals(2, x.showingCount);
    Assert.assertEquals(2, x.notShowingCount);
    Assert.assertEquals(true, fieldModel.isComparedValueEqual());
    Assert.assertEquals(false, fieldModel.isComparedSourceEqual());

    fieldModel.setValueFromSource ("123");
    Assert.assertEquals(2, x.equalCount);
    Assert.assertEquals(1, x.notEqualCount);
    Assert.assertEquals(3, x.showingCount);
    Assert.assertEquals(2, x.notShowingCount);
    Assert.assertEquals(true, fieldModel.isComparedValueEqual());
    Assert.assertEquals(true, fieldModel.isComparedSourceEqual());

    fieldModel.setValueFromSource ("456");
    Assert.assertEquals(2, x.equalCount);
    Assert.assertEquals(2, x.notEqualCount);
    Assert.assertEquals(3, x.showingCount);
    Assert.assertEquals(3, x.notShowingCount);
    Assert.assertEquals(false, fieldModel.isComparedValueEqual());
    Assert.assertEquals(false, fieldModel.isComparedSourceEqual());
  }

}
