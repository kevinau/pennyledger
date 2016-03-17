package org.pennyledger.accounts;

import java.time.LocalDate;

import org.pennyledger.accounts.gl.GLEntry;
import org.pennyledger.accounts.gl.IGLEntryDocument;
import org.pennyledger.accounts.party.IPartySourcedDocument;
import org.pennyledger.accounts.party.Party;

public class SimpleInvoice implements IGLEntryDocument, IPartySourcedDocument {

  private final String documentId;
  private LocalDate date;
  private String reference;
  private Party party;
  private String narrative;
  private GLEntry glEntry;
  
  
  public SimpleInvoice(String documentId) {
     this.documentId = documentId;
  }


  public String getDocumentId() {
    return documentId;
  }
  
  
  public LocalDate getDate() {
    return date;
  }
  
  
  public String getReference() {
    return reference;
  }
  
  
  @Override
  public Party getParty() {
    return party;
  }
  

  public String getNarrative() {
    return narrative;
  }
  
  
  @Override
  public GLEntry getGLEntry() {
    return glEntry;
  }
  
}
