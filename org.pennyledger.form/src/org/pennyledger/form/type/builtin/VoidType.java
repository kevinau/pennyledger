package org.pennyledger.form.type.builtin;

import org.pennyledger.form.type.IType;
import org.pennyledger.util.UserEntryException;

public class VoidType implements IType<Void> {

  @Override
  public Void createFromString(String source) throws UserEntryException {
    throw new RuntimeException("There should be no user input for a VoidType field");
  }

  @Override
  public Void createFromString(Void fillValue, boolean optional, boolean creating, String source) throws UserEntryException {
    throw new RuntimeException("There should be no user input for a VoidType field");
  }

  @Override
  public Void newInstance(String source) {
    return null;
  }

  @Override
  public String toDescriptionString(Void value) {
    return null;
  }

  @Override
  public String toEntryString(Void value, Void fillValue) {
    return "";
  }

  @Override
  public Void primalValue() {
    return null;
  }

  @Override
  public void validate(Void value, boolean optional) throws UserEntryException {
  }

  @Override
  public String getRequiredMessage() {
    return null;
  }

  @Override
  public int getFieldSize() {
    return 0;
  }

}