package org.pennyledger.form.type;

import java.text.MessageFormat;

import org.pennyledger.form.TextCase;
import org.pennyledger.util.UserEntryException;


public abstract class StringBasedType<T> extends Type<T> implements ITextBasedInput {

  private int maxSize;
  private TextCase allowedCase = TextCase.MIXED;

  
  public StringBasedType() {
  }

  public StringBasedType(int maxSize) {
    this.maxSize = maxSize;
  }

  public StringBasedType(int maxSize, TextCase allowedCase) {
    this.maxSize = maxSize;
    this.allowedCase = allowedCase;
  }


  @Override
  protected T createFromString(String source, T fillValue) throws UserEntryException {
    int n = source.length();
    if (n > getMaxSize()) {
      String msg = MessageFormat.format("more than {0} characters", Integer.toString(getMaxSize()));
      throw new UserEntryException(msg);
    }
    switch (allowedCase) {
    case UPPER:
      source = source.toUpperCase();
      break;
    case LOWER:
      source = source.toLowerCase();
      break;
    case MIXED :
    case UNSPECIFIED :
      break;
    }
    return createFromString2(source, fillValue);
  }

  
  @Override
  public TextCase getAllowedCase() {
    return allowedCase;
  }

  
  @Override
  public int getFieldSize() {
    return getMaxSize();
  }

  
  public int getMaxSize() {
    return maxSize;
  }
  
  
  @Override
  public String getRequiredMessage () {
    return "must not be blank";
  }

  
//  protected void validateString(String source, boolean creating) throws UserEntryException {
//    int n = source.length();
//    if (n > getMaxSize()) {
//      String msg = MessageFormat.format("more than {0} characters", Integer.toString(getMaxSize()));
//      throw new UserEntryException(msg);
//    } else if (n > 0 && source.charAt(0) == ' ') {
//      if (source.charAt(n - 1) == ' ') {
//        throw new UserEntryException("cannot start or end with a space");
//      } else {
//        throw new UserEntryException("cannot start with a space");
//      }
//    } else if (n > 0 && source.charAt(n - 1) == ' ') {
//      throw new UserEntryException("cannot end with a space", true);
//    }
//  }

  
  protected T createFromString2(String source, T fillValue) throws UserEntryException {
    T value = newInstance(source);
    validate (value);
    return value;
  }


  
  @Override
  public abstract T primalValue();

  
  public void setAllowedCase(TextCase allowedCase) {
    this.allowedCase = allowedCase;
  }

  
  public void setMaxSize(int maxSize) {
    this.maxSize = maxSize;
  }


  @Override
  protected abstract void validate (T value) throws UserEntryException;

}
