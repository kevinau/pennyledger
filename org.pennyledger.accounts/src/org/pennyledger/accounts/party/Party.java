package org.pennyledger.accounts.party;

import java.net.URL;
import java.sql.Timestamp;

import javax.persistence.Id;
import javax.persistence.Version;

import org.pennyledger.form.Entity;
import org.pennyledger.form.FormField;
import org.pennyledger.form.UniqueConstraint;

@Entity
@UniqueConstraint("code")
public class Party {

  @Id
  private long id;

  @Version
  private Timestamp version;
  
  @FormField(length=6)
  private String code;
  
  private String name;
  
  private URL webPage;
  
  
  public Party (long id, String code, String name, URL webPage) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.webPage = webPage;
  }


  @Id
  public long getId() {
    return id;
  }


  public String getCode() {
    return code;
  }


  public String getName() {
    return name;
  }


  public URL getWebPage() {
    return webPage;
  }


  public void setId(long id) {
    this.id = id;
  }

}
