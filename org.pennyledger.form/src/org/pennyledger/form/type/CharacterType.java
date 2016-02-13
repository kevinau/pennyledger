package org.pennyledger.form.type;

import org.pennyledger.form.TextCase;
import org.pennyledger.util.UserEntryException;


public class CharacterType extends Type<Character> implements ITextBasedInput {

  private TextCase allowedCase = TextCase.MIXED;

  
  public CharacterType() {
  }

  public CharacterType(TextCase allowedCase) {
    this.allowedCase = allowedCase;
  }


  @Override
  public String toEntryString(Character value, Character fillValue) {
    if (value == null) {
      return "";
    }
    if (value.charValue() == 0) {
      return "";
    } else {
      return value.toString();
    }
  }
  

  @Override
  public Character createFromString (String source, Character fillValue, boolean optional, boolean creating) throws UserEntryException {
    if (source.length() == 0) {
      if (optional) {
        return null;
      } else {
        throw new UserEntryException(getRequiredMessage(), UserEntryException.Type.REQUIRED);
      }
    }
    return createFromString(source, fillValue);
  }
  

  @Override
  protected Character createFromString(String source, Character fillValue) throws UserEntryException {
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
    int n = source.length();
    if (n > 1) {
      throw new UserEntryException("Only 1 character allowed");
    }
    if (n == 0) {
      return 0;
    } else {
      return source.charAt(0);
    }
  }

  
  @Override
  public TextCase getAllowedCase() {
    return allowedCase;
  }

  
  @Override
  public int getFieldSize() {
    return 1;
  }

  
  @Override
  public String getRequiredMessage () {
    return "Must not be empty";
  }

  
  @Override
  public Character newInstance (String source) {
    return source.charAt(0);
  }

  
  @Override
  public Character primalValue() {
    return 0;
  }

  
  public void setAllowedCase(TextCase allowedCase) {
    this.allowedCase = allowedCase;
  }

  
  @Override
  protected void validate (Character value) throws UserEntryException {
    if (value.charValue() == 0) {
      throw new UserEntryException("Must not be empty");
    }
  }

}
