package org.gyfor.reportdb;

public class GLAccount implements IGLAccount {

  private final String commonName;
  
  private final String description;
  
  private final GLAccountType accountType;
  
  
  public GLAccount (String commonName, String description, GLAccountType accountType) {
    if (commonName == null) {
      throw new IllegalArgumentException("Common name parameter cannot be null");
    }
    if (description == null) {
      throw new IllegalArgumentException("Description parameter cannot be null");
    }
    if (accountType == null) {
      throw new IllegalArgumentException("Account type parameter cannot be null");
    }
    this.commonName = commonName;
    this.description = description;
    this.accountType = accountType;
  }
  
  
  @Override
  public String getCommonName() {
    return commonName;
  }
  
  
  @Override
  public String getDescription() {
    return description;
  }
  
  
  @Override
  public GLAccountType getAccountType() {
    return accountType;
  }
  

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + accountType.hashCode();
    result = prime * result + commonName.hashCode();
    result = prime * result + description.hashCode();
    return result;
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    GLAccount other = (GLAccount)obj;
    if (accountType != other.accountType) {
      return false;
    }
    if (!commonName.equals(other.commonName)) {
      return false;
    }
    if (!description.equals(other.description)) {
      return false;
    }
    return true;
  }

}
