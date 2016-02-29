package org.pennyledger.form.type.builtin;

import org.pennyledger.form.TextCase;
import org.pennyledger.form.type.ICaseSettable;
import org.pennyledger.util.UserEntryException;


public class CharacterType extends Type<Character> implements ICaseSettable {

  private TextCase allowedCase;

  
  public CharacterType() {
    this.allowedCase = TextCase.MIXED;
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
  public Character createFromString (Character fillValue, boolean optional, boolean creating, String source) throws UserEntryException {
    source = source.trim();
    if (source.length() == 0) {
      if (optional) {
        return null;
      } else {
        // A character can be blank
      }
    }
    return createFromString(source);
  }
  

  @Override
  public Character createFromString(String source) throws UserEntryException {
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
    String s = source.trim();
    int n = s.length();
    if (n > 1) {
      throw new UserEntryException("more than 1 character");
    }
    if (n == 0) {
      return '\0';
    } else {
      return s.charAt(0);
    }
  }

  
  @Override
  public void setAllowedCase (TextCase allowedCase) {
    this.allowedCase = allowedCase;
  }

  
  @Override
  public String getRequiredMessage () {
    return "must not be blank";
  }

  
  @Override
  public Character newInstance (String source) {
    return source.charAt(0);
  }

  
  @Override
  public Character primalValue() {
    return ' ';
  }

  
  @Override
  protected void validate (Character value) throws UserEntryException {
    // No further validation required
  }

  
  @Override
  public int getFieldSize() {
    return 1;
  }

}