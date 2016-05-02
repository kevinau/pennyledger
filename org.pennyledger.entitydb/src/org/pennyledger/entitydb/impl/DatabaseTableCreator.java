package org.pennyledger.entitydb.impl;

import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.pennyledger.db.IConnection;
import org.pennyledger.object.EntityPlanFactory;
import org.pennyledger.object.plan.IClassPlan;
import org.pennyledger.object.plan.IEntityPlan;
import org.pennyledger.object.plan.IFieldPlan;
import org.pennyledger.object.plan.IObjectPlan;
import org.pennyledger.object.plan.IReferencePlan;
import org.pennyledger.object.plan.IRepeatingPlan;
import org.pennyledger.object.type.IType;
import org.pennyledger.sql.dialect.IDialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DatabaseTableCreator {

  private static final Logger logger = LoggerFactory.getLogger(DatabaseTableCreator.class);

  private final IConnection conn;
  private final IDialect dialect;


  public DatabaseTableCreator(IConnection conn) {
    this.conn = conn;
    this.dialect = conn.getDialect();
  }


  public void dropAndCreateTableSet(PrintStream log, String packagePrefix, String schema,
      IEntityRegistry entityRegistry) {

    List<Class<?>> classes = entityRegistry.getEntityClasses(packagePrefix);
    log.println("Dropping and creating " + classes.size() + " table sets");

    List<SchemaTableName> droppedTables = new ArrayList<>();
    for (Class<?> klass : classes) {
      log.println("Dropping table " + klass.getSimpleName());
      IEntityPlan<?> entityPlan = EntityPlanFactory.getEntityPlan(klass);
      dropTable(entityPlan, schema, droppedTables);
    }

    List<SchemaTableName> createdTables = new ArrayList<>();
    for (Class<?> klass : classes) {
      log.println("Creating table " + klass.getSimpleName());
      IEntityPlan<?> entityPlan = EntityPlanFactory.getEntityPlan(klass);
      // TODO remove
      entityPlan.dump();
      
      createTable(entityPlan, schema, createdTables);
    }
  }


  private void dropTable(IEntityPlan<?> entityPlan, String schema, List<SchemaTableName> droppedTables) {
    SchemaTableName fqTableName = new SchemaTableName(schema, entityPlan.getEntityName());
    if (!droppedTables.contains(fqTableName)) {
      conn.setAutoCommit(true);
      
      droppedTables.add(fqTableName);
      dropSingleTable(fqTableName);

      for (IObjectPlan plan : entityPlan.getMemberPlans()) {
        switch (plan.kind()) {
        case FIELD:
          // Nothing to do
          break;
        case EMBEDDED:
        case CLASS:
          // Nothing to do
          break;
        case REFERENCE:
          IReferencePlan<?> refPlan = (IReferencePlan<?>)plan;
          IEntityPlan<?> refEntity = refPlan.getReferencedPlan();
          dropTable(refEntity, schema, droppedTables);
          break;
        case REPEATING:
          IRepeatingPlan repeatingPlan = (IRepeatingPlan)plan;
          IObjectPlan elemPlan = repeatingPlan.getElementPlan();
          if (elemPlan instanceof IEntityPlan) {
            // If this plan is already a entity plan, use it.
            dropTable((IEntityPlan<?>)elemPlan, schema, droppedTables);
          } else if (elemPlan instanceof IReferencePlan) {
            IReferencePlan<?> refPlan2 = (IReferencePlan<?>)elemPlan;
            dropTable(refPlan2.getReferencedPlan(), schema, droppedTables);
          } else {
            // Otherwise, do nothing
          }
          break;
        case INTERFACE:
          // We don't know what implementations exist, so we have to
          // defer database table creation until runtime.
          break;
        }
      }
    }
  }


  private void createTable(IEntityPlan<?> entityPlan, String schema, IEntityPlan<?> parentPlan, List<SchemaTableName> createdTables) {
    SchemaTableName fqTableName = new SchemaTableName(schema, entityPlan.getEntityName());
    if (!createdTables.contains(fqTableName)) {
      conn.setAutoCommit(true);

      createdTables.add(fqTableName);
      
      for (IObjectPlan plan : entityPlan.getMemberPlans()) {
        switch (plan.kind()) {
        case FIELD:
          // No additional table
          break;
        case EMBEDDED:
        case CLASS:
          // No additional table
          break;
        case REFERENCE:
          IReferencePlan<?> refPlan = (IReferencePlan<?>)plan;
          IEntityPlan<?> refEntity = refPlan.getReferencedPlan();
          createTable(refEntity, schema, entityPlan, createdTables);
          break;
        case REPEATING:
          System.out.println("==========>> repeating plan: " + plan.getName());
          IRepeatingPlan repeatingPlan = (IRepeatingPlan)plan;
          IObjectPlan elemPlan = repeatingPlan.getElementPlan();
          System.out.println("==========>> elem plan: " + elemPlan.kind());
          switch (elemPlan.kind()) {
          case FIELD:
            createTable((IFieldPlan)elemPlan, schema, createdTables);
            break;
          case CLASS:
          case EMBEDDED:
            System.out.println("==========>> embedded plan: " + elemPlan.kind());
            if (elemPlan instanceof IEntityPlan) {
              // If this plan is already a entity plan, use it.
              createTable((IEntityPlan<?>)elemPlan, schema, entityPlan, createdTables);
            } else {
              createTable((IClassPlan<?>)elemPlan, schema, parentPlan, createdTables);
            }
            break;
          case REFERENCE:
            IReferencePlan<?> refPlan2 = (IReferencePlan<?>)elemPlan;
            createTable(refPlan2.getReferencedPlan(), schema, entityPlan, createdTables);
            break;
          case REPEATING:
            // TODO 
            break;
          case INTERFACE:
            // TODO 
            break;
          }
          break;
        case INTERFACE:
          // We don't know what implementations exist, so we have to
          // defer database table creation until runtime.
          break;
        }
      }
      System.out.println("==========>> create single table: " + fqTableName);
      createSingleTable(entityPlan, fqTableName);
    }
  }


//  private void findTable(IClassPlan<?> classPlan, String schema, IEntityPlan<?> parentPlan, List<SchemaTableName> createdTables) {
//    conn.setAutoCommit(true);
//
//    SchemaTableName fqTableName = new SchemaTableName(schema, classPlan.getName());
//    if (!createdTables.contains(fqTableName)) {
//      createdTables.add(fqTableName);
//      
//      for (IObjectPlan plan : classPlan.getMemberPlans()) {
//        switch (plan.kind()) {
//        case FIELD:
//          // Do nothing
//          break;
//        case EMBEDDED:
//        case CLASS:
//          if (plan instanceof IEntityPlan) {
//            // If this plan is already a entity plan, use it.
//            findTable((IEntityPlan<?>)plan, schema, createdTables);
//          } else {
//            // Otherwise, do nothing
//          }
//          break;
//        case REFERENCE:
//          IReferencePlan<?> refPlan = (IReferencePlan<?>)plan;
//          IEntityPlan<?> refEntity = refPlan.getReferencedPlan();
//          findTable(refEntity, schema, createdTables);
//          break;
//        case REPEATING:
//          System.out.println("==========>> repeating plan: " + plan.getName());
//          IRepeatingPlan repeatingPlan = (IRepeatingPlan)plan;
//          IObjectPlan elemPlan = repeatingPlan.getElementPlan();
//          System.out.println("==========>> elem plan: " + elemPlan.kind());
//          switch (elemPlan.kind()) {
//          case CLASS:
//          case EMBEDDED:
//            System.out.println("==========>> embedded plan: " + elemPlan.kind());
//            if (elemPlan instanceof IEntityPlan) {
//              // If this plan is already a entity plan, use it.
//              findTable((IEntityPlan<?>)elemPlan, schema, createdTables);
//            } else {
//              findTable((IClassPlan<?>)elemPlan, schema, parentPlan, createdTables);
//            }
//            break;
//          case FIELD:
//            findTable((IFieldPlan)elemPlan, schema, createdTables);
//            break;
//          case REFERENCE:
//            IReferencePlan<?> refPlan2 = (IReferencePlan<?>)elemPlan;
//            findTable(refPlan2.getReferencedPlan(), schema, createdTables);
//            break;
//          case REPEATING:
//            // TODO 
//            break;
//          case INTERFACE:
//            // TODO 
//            break;
//          }
//          break;
//        case INTERFACE:
//          // We don't know what implementations exist, so we have to
//          // defer database table creation until runtime.
//          break;
//        }
//      }
//      System.out.println("==========>> create single table: " + fqTableName);
//      createSingleTable(classPlan, fqTableName, parentPlan);
//    }
//  }


  private void dropSingleTable(SchemaTableName fqTableName) {
    // Drop any existing table
    String sql = MessageFormat.format(dialect.dropTableTemplate(), fqTableName);
    try {
      conn.executeCommand(sql);
    } catch (Exception ex) {
      // Assume the problem is that the table does not exist, but log the error
      // anyway
      logger.info(ex.getMessage());
    }
  }

  private static final String NL = System.getProperty("line.separator");


  public void createSingleTable(IEntityPlan<?> plan, String schema) {
    SchemaTableName fqTableName = new SchemaTableName(schema, plan.getEntityName());
    
    StringBuilder buff = new StringBuilder();
    buff.append(MessageFormat.format(dialect.createTableTemplate(), fqTableName));

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
      addObjectColumn(entityLifeField, schema, buff, "_");
    }

    for (IFieldPlan p1 : plan.getKeyFields()) {
      addObjectColumn(p1, schema, buff, "_");
    }

    for (IObjectPlan p2 : plan.getDataFields()) {
      addObjectColumn(p2, schema, buff, "_");
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


  public void createSingleTable(IClassPlan<?> plan, String schema, IEntityPlan<?> parentPlan) {
    SchemaTableName fqTableName = new SchemaTableName(schema, plan.getName());
    
    StringBuilder buff = new StringBuilder();
    buff.append(MessageFormat.format(dialect.createTableTemplate(), fqTableName));

    buff.append(" (");
    buff.append(NL);
    
    buff.append(parentPlan.getEntityName());
    buff.append("Id");
    buff.append(" INTEGER NOT NULL");
    buff.append(",");
    buff.append(NL);

    for (IObjectPlan p1 : plan.getMemberPlans()) {
      addObjectColumn (p1, schema, buff, "_");
    }

    // Now create the table using the constructed SQL statement
    String sql = buff.toString();
    conn.executeCommand(sql);
  }

  
  public void createSingleTable(IFieldPlan plan, String schema, IEntityPlan<?> parentPlan) {
    SchemaTableName fqTableName = new SchemaTableName(schema, plan.getName());
    
    StringBuilder buff = new StringBuilder();
    buff.append(MessageFormat.format(dialect.createTableTemplate(), fqTableName));

    buff.append(" (");
    buff.append(NL);
    
    buff.append(parentPlan.getEntityName());
    buff.append("Id");
    buff.append(" INTEGER NOT NULL");
    buff.append(",");
    buff.append(NL);

    addObjectColumn (plan, schema, buff, "_");

    // Now create the table using the constructed SQL statement
    String sql = buff.toString();
    conn.executeCommand(sql);
  }

  
  private void addObjectColumn (IObjectPlan plan, String schema, StringBuilder buff, String partialName) {
    switch (plan.kind()) {
    case FIELD:
      buff.append(",");
      buff.append(NL);
      addColumn(buff, partialName, (IFieldPlan)plan);
      break;
    case CLASS:
    case EMBEDDED:
      IClassPlan<?> classPlan = (IClassPlan<?>)plan;
      for (IObjectPlan p : classPlan.getMemberPlans()) {
        addObjectColumn(p, buff, partialName + "_" + plan.getName());
      }
      break;
    case INTERFACE:
      // TODO Not yet figured out
      break;
    case REFERENCE:
      IReferencePlan<?> referencePlan = (IReferencePlan<?>)plan;
      IEntityPlan<?> refEntityPlan = referencePlan.getReferencedPlan();
      
      buff.append(",");
      buff.append(NL);
      buff.append(refEntityPlan.getEntityName());
      buff.append("Id INTEGER NOT NULL REFERENCES ");
      buff.append(refEntityPlan.getEntityName());
      
      createSingleTable(refEntityPlan, schema, parentPlan);
      break;
    case REPEATING:
      IRepeatingPlan repeatingPlan = (IRepeatingPlan)plan;
      IObjectPlan elemPlan = repeatingPlan.getElementPlan();
      
      // No column in the parent table

      switch (elemPlan.kind()) {
      case FIELD:
        IObjectPlan[] fx1 = {
            elemPlan,
        };
        createRepeatingTable(fx1, plan.getName(), schema, parentPlan);
        break;
      case CLASS:
      case EMBEDDED:
        IObjectPlan[] fx2 = ((IClassPlan<?>)elemPlan).getMemberPlans();
        createRepeatingTable(fx2, plan.getName(), schema, parentPlan);
        break;
      case INTERFACE:
        // TODO figure this out
        break;
      case REFERENCE:
        IObjectPlan[] fx3 = {
            elemPlan,
        };
        createRepeatingTable(fx3, schema, parentPlan);
        break;
      case REPEATING:
        IObjectPlan[] fx4 = new IObjectPlan[0];
        createRepeatingTable(fx4, schema, parentPlan);
      }
      break;
    }
  }
  
  
  private void addColumn(StringBuilder buff, String partialName, IFieldPlan plan) {
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
        addColumn2(buff, name, sqlType2, plan.isNullable());
        i++;
      }
    } else {
      addColumn2(buff, plan.getName(), sqlType, plan.isNullable());
    }
  }


  private void addColumn2(StringBuilder buff, String name, String sqlType, boolean nullable) {
    buff.append(name);
    buff.append(' ');
    buff.append(sqlType);
    if (!nullable) {
      buff.append(" NOT NULL");
    }
  }
}
