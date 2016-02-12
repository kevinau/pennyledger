package org.pennyledger.web;

public class MimeType {

  private final String mimeCode;
  private final int expiry;
  
  public MimeType (String mimeCode, int expiry) {
    this.mimeCode = mimeCode;
    this.expiry = expiry;
  }

  
  public String getMimeCode () {
    return mimeCode;
  }
  
  
  public int getExpiry () {
    return expiry;
  }


  @Override
  public String toString() {
    return "MimeType [mimeCode=" + mimeCode + ", expiry=" + expiry + "]";
  }
  
}
