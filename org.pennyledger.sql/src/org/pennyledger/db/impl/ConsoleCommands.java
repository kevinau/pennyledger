package org.pennyledger.db.impl;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.pennyledger.db.IDatabaseRegistry;

@Component(
    property = { "osgi.command.scope:String=db",
                 "osgi.command.function:String=list" }, 
    service = Object.class)
public class ConsoleCommands {

  private IDatabaseRegistry databaseRegistry;
  
  @Reference
  public void setDatabaseRegistry (IDatabaseRegistry databaseRegistry) {
    this.databaseRegistry = databaseRegistry;
  }
  
  public void unsetDatabaseRegistry (IDatabaseRegistry databaseRegistry) {
    this.databaseRegistry = null;
  }
  
  public Object list() {
    String[] dbnames = databaseRegistry.getDatabaseNames();
    for (String dbname : dbnames) {
      System.out.println(dbname);
    }
    return null;
  }

}
