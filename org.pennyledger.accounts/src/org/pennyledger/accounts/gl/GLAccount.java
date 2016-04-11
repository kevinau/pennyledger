package org.pennyledger.accounts.gl;

import org.pennyledger.form.Entity;
import org.pennyledger.form.FormField;
import org.pennyledger.form.UniqueConstraint;

@Entity
@UniqueConstraint("code")
public class GLAccount {

  @FormField(length=10)
  private String code;
  
  private String description;
  
  private GLAccountType accountType;
  
  public GLAccount (String code, String description, GLAccountType accountType) {
    this.code = code;
    this.description = description;
    this.accountType = accountType;
  }
  
  
  public String getCode() {
    return code;
  }
  
  
  public String getDescription() {
    return description;
  }
  
  
  public GLAccountType getAccountType() {
    return accountType;
  }
  
}
