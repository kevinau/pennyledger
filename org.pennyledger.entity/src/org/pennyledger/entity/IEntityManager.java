package org.pennyledger.entity;

import java.sql.Timestamp;

import org.pennyledger.value.EntityLife;

public interface IEntityManager<T> {

  public void dropAndCreateTableSet();
  
//  public Timestamp add (T entity, long[] id);
//  
//  public Timestamp addOrUpdate (T entity, long[] id);
//  
//  public T fetchUsingId (long id);
//  
//  public T fetchUsingIndex (int index, Object... fields);
//  
//  public T fetchUsingPrimaryIndex ();
//  
//  public void remove (long id);
//  
//  public Timestamp unretire (long id);
//  
//  public Timestamp update (T entity);
//  
//  public Timestamp updateEntityLife (long id, EntityLife entityLife);
  
}
