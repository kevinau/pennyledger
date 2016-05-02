package org.pennyledger.entitydb;

import java.util.List;

import org.pennyledger.entitydb.IEntityDatabaseMapping.MappingEntry;

public interface IEntityDatabaseMappingRegistry {

  public List<MappingEntry> getMappingEntries(String name);

  public String[] getMappingNames();

}