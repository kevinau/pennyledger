package org.pennyledger.form.test.field;


import org.pennyledger.form.EntryMode;
import org.pennyledger.form.FormField;
import org.pennyledger.form.model.ComparisonBasis;
import org.pennyledger.form.model.EffectiveMode;
import org.pennyledger.form.model.FieldEventListener;
import org.pennyledger.form.model.FormModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IGroupModel;
import org.pennyledger.form.model.IObjectModel;
import org.pennyledger.util.UserEntryException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class EventsTest {

  private static final int MODE = 0;
  private static final int BASIS = 1;
  private static final int SOURCE = 2;
  private static final int SOURCE_EQUALITY = 3;
  private static final int VALUE = 4;
  private static final int VALUE_EQUALITY = 5;
  private static final int ERROR_NOTED = 6;
  private static final int ERROR_CLEARED = 7;
  
  private static final String[] countNames = {
    "effectiveMode",
    "comparisonBasis",
    "source",
    "sourceEquality",
    "value",
    "valueEquality",
    "errorNoted",
    "errorCleared",
  };
  
  private static class TestClass {
    @FormField(nullable=false)
    private Integer field1;
  }

  private int[] counts = new int[8];
  
  private FieldEventListener fieldEventListener = new FieldEventListener() {

    @Override
    public void modeChange(IObjectModel model) {
      counts[MODE]++;
    }

    @Override
    public String getOrigin() {
      return null;
    }

    @Override
    public void compareEqualityChange(IFieldModel model) {
      counts[VALUE_EQUALITY]++;
    }

    @Override
    public void compareShowingChange(IFieldModel model, boolean isDataTrigger) {
      counts[SOURCE_EQUALITY]++;
    }

    @Override
    public void valueChange(IFieldModel model) {
      counts[VALUE]++;
    }

    @Override
    public void errorCleared(IFieldModel model) {
      counts[ERROR_CLEARED]++;
    }

    @Override
    public void errorNoted(IFieldModel model, UserEntryException ex) {
      counts[ERROR_NOTED]++;
    }

    @Override
    public void sourceChange(IFieldModel model) {
      counts[SOURCE]++;
    }

    @Override
    public void comparisonBasisChange(IFieldModel model) {
      counts[BASIS]++;
    }
  };
  
  private void check (int... names) {
    int fail = 0;
    for (int i = 0; i < counts.length; i++) {
      int expected = 0;
      for (int name : names) {
        if (name == i) {
          expected = 1;
          break;
        }
      }
      if (counts[i] != expected) {
        System.out.println(countNames[i] + ": expected " + expected + ", found " + counts[i]);
        fail++;
      }
    }
    System.out.println();
    if (fail > 0) {
      Assert.fail(fail + " event counts wrong");
    }
    for (int i = 0; i < counts.length; i++) {
      counts[i] = 0;
    }
  };
  
  
  private void reset () {
    for (int i = 0; i < counts.length; i++) {
      counts[i] = 0;
    }
  }


  private IFormModel<TestClass> formModel;
  private IFieldModel fieldModel;
  
  @Before 
  public void init() {
    TestClass instance = new TestClass();
    formModel = new FormModel<TestClass>(instance);
    
    IGroupModel groupModel = formModel.getRootModel();
    fieldModel = groupModel.getMember("field1");
    fieldModel.addFieldEventListener(fieldEventListener);
  }

  
  @Test
  public void initialCondition() {
    check(MODE, BASIS, SOURCE, SOURCE_EQUALITY, VALUE_EQUALITY, ERROR_NOTED);
  }

  @Test
  public void setMode() {
    reset();
    Assert.assertEquals(EffectiveMode.ENTRY, fieldModel.getEffectiveMode());
    fieldModel.setMode(EntryMode.NA);
    check(MODE, ERROR_CLEARED);
    Assert.assertEquals(EffectiveMode.NA, fieldModel.getEffectiveMode());
  }
  
  @Test
  public void setMode2() {
    reset();
    Assert.assertEquals(true, fieldModel.isInError());
    fieldModel.setValueFromSource("123");
    Assert.assertEquals(false, fieldModel.isInError());
    check(SOURCE, ERROR_CLEARED, SOURCE_EQUALITY, VALUE, VALUE_EQUALITY);
    fieldModel.setMode(EntryMode.NA);
    check(MODE, SOURCE_EQUALITY, VALUE_EQUALITY);
  }
 
  
  @Test
  public void setValidSource() {
    reset();
    fieldModel.setValueFromSource("123");
    check(SOURCE, ERROR_CLEARED, SOURCE_EQUALITY, VALUE, VALUE_EQUALITY);
    reset();
    fieldModel.setValueFromSource("1234");
    check(SOURCE, VALUE);
    reset();
    fieldModel.setValueFromSource("01234");
    check(SOURCE);
  }

  @Test
  public void setComparisonBasis() {
    reset();
    fieldModel.setCompareBasis(ComparisonBasis.DEFAULT);
    check();
    fieldModel.setValueFromSource("123");
    check(SOURCE, ERROR_CLEARED, SOURCE_EQUALITY, VALUE, VALUE_EQUALITY);
    fieldModel.setDefaultValue(new Integer(123));
    check(SOURCE_EQUALITY, VALUE_EQUALITY);
  }
  
  @Test 
  public void setErrorConditions () {
    reset();
    fieldModel.setValueFromSource("123");
    check(SOURCE, ERROR_CLEARED, SOURCE_EQUALITY, VALUE, VALUE_EQUALITY);
    Assert.assertEquals(false, fieldModel.isComparedSourceEqual());
    Assert.assertEquals(false, fieldModel.isComparedValueEqual());
    Assert.assertEquals(false, fieldModel.isInError());
    Assert.assertEquals(UserEntryException.Type.OK, fieldModel.getStatus());
    
    fieldModel.setValueFromSource("");
    check(SOURCE, SOURCE_EQUALITY, VALUE_EQUALITY, ERROR_NOTED);
    Assert.assertEquals(true, fieldModel.isComparedSourceEqual());
    Assert.assertEquals(true, fieldModel.isComparedValueEqual());
    Assert.assertEquals(true, fieldModel.isInError());
    Assert.assertEquals(UserEntryException.Type.REQUIRED, fieldModel.getStatus());
    Assert.assertEquals(true, fieldModel.getStatusMessage().startsWith("number required "));
    
    fieldModel.setValueFromSource("+");
    check(SOURCE, SOURCE_EQUALITY, ERROR_NOTED);
    Assert.assertEquals(false, fieldModel.isComparedSourceEqual());
    Assert.assertEquals(true, fieldModel.isComparedValueEqual());
    Assert.assertEquals(true, fieldModel.isInError());
    Assert.assertEquals(UserEntryException.Type.INCOMPLETE, fieldModel.getStatus());
    Assert.assertEquals("not a signed number", fieldModel.getStatusMessage());
    
    fieldModel.setValueFromSource("+abc");
    check(SOURCE, ERROR_NOTED);
    Assert.assertEquals(false, fieldModel.isComparedSourceEqual());
    Assert.assertEquals(true, fieldModel.isComparedValueEqual());
    Assert.assertEquals(true, fieldModel.isInError());
    Assert.assertEquals(UserEntryException.Type.ERROR, fieldModel.getStatus());
    Assert.assertEquals("not a signed number", fieldModel.getStatusMessage());

    fieldModel.setValueFromSource("123");
    check(SOURCE, VALUE, VALUE_EQUALITY, ERROR_CLEARED);
    Assert.assertEquals(false, fieldModel.isComparedSourceEqual());
    Assert.assertEquals(false, fieldModel.isComparedValueEqual());
    Assert.assertEquals(false, fieldModel.isInError());
    Assert.assertEquals(UserEntryException.Type.OK, fieldModel.getStatus());
}

}
