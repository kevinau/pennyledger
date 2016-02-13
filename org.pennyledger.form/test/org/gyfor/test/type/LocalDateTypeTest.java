package org.gyfor.test.type;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.type.IType;
import org.pennyledger.form.type.LocalDateType;
import org.pennyledger.util.UserEntryException;


public class LocalDateTypeTest {

  @Test
  public void createFromStringTest() throws UserEntryException {
    IType<LocalDate> type = new LocalDateType();
    LocalDate d = type.createFromString("21/5/2011", null, false, false);
    LocalDate d1 = LocalDate.of(2011, 5, 21);
    Assert.assertEquals(d1, d);
  }

  @Test
  public void createFromStringTest2a() throws UserEntryException {
    IType<LocalDate> type = new LocalDateType();
    try {
      type.createFromString("21", null, false, false);
      Assert.fail("Exception expected");
    } catch (UserEntryException ex) {
      System.out.println(ex);
      Assert.assertEquals(false, ex.isRequired());
      Assert.assertEquals(true, ex.isIncomplete());
    }
  }

  
  @Test
  public void createFromStringTest2() throws UserEntryException {
    IType<LocalDate> type = new LocalDateType();
    try {
      type.createFromString("abcde", null, false, false);
      Assert.fail("Exception expected");
    } catch (UserEntryException ex) {
      Assert.assertEquals(true, ex.isError());
    }
  }

  
  @Test
  public void createFromStringTest3() throws UserEntryException {
    IType<LocalDate> type = new LocalDateType();
    LocalDate d = type.createFromString("", null, true, false);
    Assert.assertEquals(null, d);
  }

  
  @Test
  public void createFromStringTest4() throws UserEntryException {
    IType<LocalDate> type = new LocalDateType();
    try {
      type.createFromString("", null, false, false);
      Assert.fail("Exception expected");
    } catch (UserEntryException ex) {
      Assert.assertEquals(true, ex.isRequired());
    }
  }

}
