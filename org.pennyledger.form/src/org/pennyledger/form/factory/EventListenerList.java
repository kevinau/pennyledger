package org.pennyledger.form.factory;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import org.pennyledger.form.path.Path;
import org.pennyledger.form.path.parser.ParseException;
import org.pennyledger.form.path.parser.SimplePathParser;
import org.pennyledger.form.value.IFieldVisitable;
import org.pennyledger.form.value.IFieldModel;
import org.pennyledger.form.value.IObjectVisitable;
import org.pennyledger.form.value.IObjectModel;

public class EventListenerList<T extends EventListener> {

  private List<Path> paths = new ArrayList<>(0);
  private List<T> listeners = new ArrayList<>(0);
  
  public void add(String pathExpr, T x) {
    try {
      Path path = new SimplePathParser(pathExpr).parse();
      paths.add(path);
      listeners.add(x);
    } catch (ParseException ex) {
      throw new IllegalArgumentException(ex);
    }
  }
  
  
  public void remove(String pathExpr, T x) {
    int i = indexOf(pathExpr, x);
    if (i >= 0) {
      paths.remove(i);
      listeners.remove(i);
    }
  }
  
  
  public void fireObjectEvents (IObjectModel soruce, IObjectEvent<T> event) {
    IObjectModel p = soruce;
    while (p.getParent() != null) {
      p = p.getParent();
    }
    for (int i = 0; i < paths.size(); i++) {
      Path path = paths.get(i);
      T x = listeners.get(i);
      path.matches(p, new IObjectVisitable() {
        @Override
        public void visit(IObjectModel model) {
          event.eventFired(model, x);
        }
      });
    }
  }
  
  public void fireFieldEvents (IFieldModel model, IFieldEvent<T> event) {
    IObjectModel p = model;
    while (p.getParent() != null) {
      p = p.getParent();
    }
    for (int i = 0; i < paths.size(); i++) {
      Path path = paths.get(i);
      T x = listeners.get(i);
      path.matches(p, new IFieldVisitable() {
        @Override
        public void visit(IFieldModel model) {
          event.eventFired(model, x);
        }
      });
    }
  }
  
  private int indexOf (String pathExpr, T x) {
    for (int i = 0; i < paths.size(); i++) {
      if (paths.get(i).getSource().equals(pathExpr) && listeners.get(i).equals(x)) {
        return i;
      }
    }
    return -1;
  }
}
