package org.pennyledger.form.type.test;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import org.pennyledger.form.type.IType;
import org.pennyledger.form.type.builtin.LocalDateType;
import org.pennyledger.util.UserEntryException;


public class LocalDateTypeTest {

  @Test
  public void createFromStringTest() throws UserEntryException {
    IType<LocalDate> type = new LocalDateType();
    LocalDate d = type.createFromString("21/5/2011");
    LocalDate d1 = LocalDate.of(2011, 5, 21);
    Assert.assertEquals(d1, d);
  }

  @Test
  public void createFromStringTest2a() throws UserEntryException {
    IType<LocalDate> type = new LocalDateType();
    try {
      type.createFromString("21");
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
      type.createFromString("abcde");
      Assert.fail("Exception expected");
    } catch (UserEntryException ex) {
      Assert.assertEquals(true, ex.isError());
    }
  }

  
  @Test
  public void createFromStringTest3() throws UserEntryException {
    IType<LocalDate> type = new LocalDateType();
    try {
      type.createFromString("");
      Assert.fail("Exception expected");
    } catch (UserEntryException ex) {
      Assert.assertEquals(true, ex.isRequired());
    }
  }

}
