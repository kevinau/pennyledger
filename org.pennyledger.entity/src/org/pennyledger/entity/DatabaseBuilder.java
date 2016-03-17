package org.pennyledger.entity;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.pennyledger.db.IConnection;
import org.pennyledger.db.ITableSet;
import org.pennyledger.entity.impl.IEntityVisitor;
import org.pennyledger.form.factory.EntityPlanFactory;
import org.pennyledger.form.plan.IEntityPlan;
import org.pennyledger.form.plan.IFieldPlan;
import org.pennyledger.form.plan.IObjectPlan;
import org.pennyledger.form.plan.IReferencePlan;
import org.pennyledger.form.plan.PlanKind;
import org.pennyledger.form.type.IType;
import org.pennyledger.sql.dialect.IDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseBuilder {

  private static final Logger logger = LoggerFactory.getLogger(DatabaseBuilder.class);
  
  private final IConnection conn;
  private final IDialect dialect;
  
  public DatabaseBuilder (IConnection conn) {
    this.conn = conn;
    this.dialect = conn.getDialect();
  }
  
  public void dropAndCreateTableSet (ITableSet tableset, IEntityRegistry entityRegistry) {
    List<String> prefixes = tableset.getEntityPrefixes();

    List<String> droppedTables = new ArrayList<>();
    for (String prefix : prefixes) {
      List<Class<?>> classes = entityRegistry.getEntityClasses(prefix);
      for (Class<?> klass : classes) {
        System.out.println("################ " + klass.getCanonicalName());
        IEntityPlan<?> entityPlan = EntityPlanFactory.create(klass);
        dropTable(entityPlan, droppedTables);
      }
    }

    List<String> createdTables = new ArrayList<>();
    for (String prefix : prefixes) {
      List<Class<?>> classes = entityRegistry.getEntityClasses(prefix);
      for (Class<?> klass : classes) {
        System.out.println("################ " + klass.getCanonicalName());
        IEntityPlan<?> entityPlan = EntityPlanFactory.create(klass);
        createTable(entityPlan, createdTables);
      }
    }
  }
  
  
  private void dropTable (IEntityPlan<?> entityPlan, List<String> droppedTables) {
    conn.setAutoCommit(true);
    
    // Drop this entity table, and related tables
    walkPlan (entityPlan, new IEntityVisitor() {
      @Override
      public void processPlan(IEntityPlan<?> plan) {
        String tableName = plan.getEntityName();
        if (!droppedTables.contains(tableName)) {
          droppedTables.add(tableName);
          dropSingleTable (plan);
        }
      }
    }, null);
    
  }
  
  private void createTable (IEntityPlan<?> entityPlan, List<String> createdTables) {
    conn.setAutoCommit(true);
    
    // Build this entity table, and related tables
    walkPlan (entityPlan, null, new IEntityVisitor() {
      @Override
      public void processPlan(IEntityPlan<?> plan) {
        String tableName = plan.getEntityName();
        if (!createdTables.contains(tableName)) {
          createdTables.add(tableName);
          createSingleTable (plan);
        }
      }
    });
    
  }
  
  private void walkPlan (IObjectPlan plan, IEntityVisitor before, IEntityVisitor after) {
    switch (plan.kind()) {
    case FIELD :
      // Do nothing
      break;
    case REFERENCE :
      IReferencePlan<?> refPlan = (IReferencePlan<?>)plan;
      IEntityPlan<?> refEntity = refPlan.getReferencedPlan();
      if (before != null) {
        before.processPlan(refEntity);
      }
      walkPlan (refEntity, before, after);
      if (after != null) {
        after.processPlan(refEntity);
      }
      break;
    case EMBEDDED :
    case CLASS :
      IEntityPlan<?> plan2;
      if (plan instanceof IEntityPlan) {
        // If this plan is already a entity plan, use it.        
        plan2 = (IEntityPlan<?>)plan;
      } else {
        // Otherwise, create an entity plan by wrapping the class plan.        
        // TODO plan2 = new ClassBasedEntityPlan((IClassPlan<?>)plan);
        plan2 = null;
      }
      if (before != null) {
        before.processPlan(plan2);
      }
      for (IObjectPlan memberPlan : plan2.getMemberPlans()) {
        walkPlan (memberPlan, before, after);
      }
      if (after != null) {
        after.processPlan(plan2);
      }
      break;
    case REPEATING :
      // TODO
//      IRepeatingPlan repeatingPlan = (IRepeatingPlan)plan;
//      IObjectPlan elemPlan = repeatingPlan.getElementPlan();
//      IEntityPlan entityPlan3;
//      switch (elemPlan.kind()) {
//      case CLASS:
//      case EMBEDDED:
//        entityPlan3 = new ClassBasedEntityPlan((IClassPlan<?>)elemPlan);
//        break;
//      case FIELD:
//        entityPlan3 = new FieldBasedEntityPlan((IFieldPlan)elemPlan);
//        break;
//      case INTERFACE:
//        entityPlan3 = new EntityPlan((IInterfacePlan)elemPlan);
//        break;
//      case REFERENCE:
//        entityPlan3 = (IReferencePlan)elemPlan;
//        break;
//      case REPEATING:
//        entityPlan3 = new EntityPlan((IRepeatingPlan)elemPlan);
//        break;
//      }
//      if (before != null) {
//        before.processPlan(entityPlan3);
//      }
//      walkPlan (elemPlan, before, after);
//      if (after != null) {
//        after.processPlan(entityPlan3);
//      }
      break;
    case INTERFACE :
      // We don't know what implementations exist, so we have to 
      // defer database table creation until runtime.
      break;
    }
  }
  
  private void dropSingleTable (IEntityPlan<?> plan) {
    String entityName = plan.getEntityName();
    
    // Drop any existing table
    String sql = MessageFormat.format(dialect.dropTableTemplate(), entityName);
    try {
      conn.executeCommand(sql);
    } catch (Exception ex) {
      // Assume the problem is that the table does not exist, but log the error anyway
      logger.info(ex.getMessage());
    }
  }
  
  
  private static final String NL = System.getProperty("line.separator");
  
  
  public void createSingleTable (IEntityPlan<?> plan) {
    String entityName = plan.getEntityName();

    StringBuilder buff = new StringBuilder();
    buff.append(MessageFormat.format(dialect.createTableTemplate(), entityName));
    
    buff.append(" (");
    buff.append(NL);
    // We assume the Id field is called "id" and is an INTEGER
    buff.append(dialect.idColumnTemplate());

    IFieldPlan versionField = plan.getVersionField();
    if (versionField != null) {
      // We assume the version field is Timestamp
      buff.append(",");
      buff.append(NL);
      buff.append(versionField.getName());
      buff.append(" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP");
    }
    
    IFieldPlan entityLifeField = plan.getEntityLifeField();
    if (entityLifeField != null) {
      buff.append(",");
      buff.append(NL);
      addColumn(buff, entityLifeField);
    }
    
    for (IFieldPlan p1 : plan.getKeyFields()) {
      buff.append(",");
      buff.append(NL);
      addColumn(buff, p1);
    }
    
    for (IObjectPlan p2 : plan.getDataFields()) {
      buff.append(",");
      buff.append(NL);
      if (p2.kind() == PlanKind.FIELD) {
        addColumn(buff, (IFieldPlan)p2);
      } else {
        throw new RuntimeException(p2.kind() + " not yet supported");
      }
    }
    
    List<IFieldPlan[]> constraints = plan.getUniqueConstraints();
    if (constraints != null) {
      int c = 1;
      for (IFieldPlan[] constraint : constraints) {
        buff.append(",");
        buff.append(NL);
        buff.append("CONSTRAINT ");
        buff.append(plan.getEntityName());
        buff.append("_UC");
        buff.append(c);
        buff.append(" UNIQUE");
        String separator = "(";
        for (IFieldPlan field : constraint) {
          buff.append(separator);
          buff.append(field.getName());
          separator = ",";
        }
        buff.append(")");
        c++;
      }
    }
    buff.append(")");
    
    // Now create the table using the constructed SQL statement
    String sql = buff.toString();
    conn.executeCommand(sql);
  }
  
  
  private void addColumn (StringBuilder buff, IFieldPlan plan) {
    IType<?> planType = plan.getType();
    String sqlType = planType.getSQLType();
    if (sqlType == null) {
      String[] sqlTypes = planType.getSQLTypes();
      int i = 0;
      for (String sqlType2 : sqlTypes) {
        String name = plan.getName();
        if (i > 0) {
          name += '_' + i;
        }
        addColumn2 (buff, name, sqlType2, plan.isNullable());
        i++;
      }
    } else {
      addColumn2 (buff, plan.getName(), sqlType, plan.isNullable());
    }
  }
  
  
  private void addColumn2 (StringBuilder buff, String name, String sqlType, boolean nullable) {
    buff.append(name);
    buff.append(' ');
    buff.append(sqlType);
    if (!nullable) {
      buff.append(" NOT NULL");
    }
  }
}
