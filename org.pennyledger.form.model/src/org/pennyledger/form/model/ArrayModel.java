package org.pennyledger.form.model;

import java.util.Arrays;
import java.util.Collection;

import org.pennyledger.form.plan.IArrayPlan;
import org.pennyledger.form.plan.IObjectPlan;

public class ArrayModel extends ContainerModel implements IArrayModel {
  
  private final IArrayPlan arrayPlan;
  private Object[] elems;
  
  private IObjectModel[] memberModels = new IObjectModel[0];
  

  public ArrayModel (IFormModel<?> ownerForm, IContainerModel parentModel, Object parentRef, IArrayPlan arrayPlan, Object instance) {
    super (ownerForm, parentModel, parentRef, arrayPlan.getPathName(), arrayPlan);
    this.arrayPlan = arrayPlan;
    setValue (instance);
  }

  
  @Override
  public void setValue(Object instance) {
    System.out.println("aaaa ArrayModel setValue: " + (instance == null ? "null" : "[" + ((Object[])instance).length + "]"));
    IObjectPlan elemPlan = arrayPlan.getElementPlan();
    int newSize = arrayPlan.getMaxSize();
    int oldSize = memberModels.length;
    int n;
    
    if (instance == null) {
      if (newSize == Integer.MAX_VALUE) {
//// TODO OccursFor providers should run before this (ie setSize before setValue)
////        throw new RuntimeException("Not array size specified");
        newSize = 0;
      }
      n = 0;
      elems = (Object[])arrayPlan.newValue();
    } else {
      Object[] ix = (Object[])instance;

      // Calculate the number of elems that are being assigned a value
      if (newSize == Integer.MAX_VALUE) {
        newSize = ix.length;
        n = ix.length;
      } else if (ix.length <= newSize) {
        n = ix.length;
      } else {
        throw new IndexOutOfBoundsException("Supplied value contains " + ix.length + " elements, only " + newSize + " allowed");
      }
      elems = Arrays.copyOf(ix, newSize);
    }
     
    // Clean up soon-to-be-truncated model elements
    if (n < memberModels.length) {
      for (int i = n; i < memberModels.length; i++) {
        memberModels[i].detach();
      }
      fireContainerOccursReduced(this);
    }
    
    // Build a new set of element models
    memberModels = Arrays.copyOf(memberModels, newSize);
    
    for (int i = 0; i < n; i++) {
      // Assign new values to the array element models
      if (i < oldSize) {
        memberModels[i].setValue(elems[i]);
      } else {
        memberModels[i] = elemPlan.createModel(getOwnerForm(), this, i, "elem", elems[i]);
      }
    }
        
    for (int i = n; i < newSize; i++) {
      elems[i] = elemPlan.newValue();
      memberModels[i] = elemPlan.createModel(getOwnerForm(), this, i, "elem", elems[i]);
    }
    //fireContainerAdd(this, memberModels, oldSize);
    
    setUnderlyingValue(elems);
  }
  
  
  @Override
  public Object getValue () {
    return elems;
  }


  public void setArraySize (int size) {
    System.out.println("aaaa ArrayModel setArraySize: " + size);
    IObjectPlan elemPlan = arrayPlan.getElementPlan();

    // Clean up excess model elements
    for (int i = size; i < memberModels.length; i++) {
      memberModels[i].detach();
    }
    
    int n = elems.length;
    if (n != size) {
      int n0 = n;
      elems = Arrays.copyOf(elems, size);
      memberModels = Arrays.copyOf(memberModels, size);
      while (n < size) {
        // Add additional elements if necessary
        Object elemValue = elemPlan.newValue();
        elems[n] = elemValue;
        memberModels[n] = elemPlan.createModel(getOwnerForm(), this, n, "elem", elemValue);
        n++;
      }
      setUnderlyingValue(elems);
      System.out.println("xxxx reduced size from " + n0 + " to " + size);
      if (size < n0) {
        System.out.println("xxxx fire occurs reduced");
        fireContainerOccursReduced(this);
      }
    }
  }
  
  
  public int getArraySize () {
    return memberModels.length;
  }
  
  
  @Override
  public void resetToInitial () {
    for (IObjectModel member : memberModels) {
      member.resetToInitial();
    }
  }
  

  @Override
  public Collection<IObjectModel> getChildren() {
    return Arrays.asList(memberModels);
  }

  
  @Override 
  public IObjectModel getMember (int index) {
    return memberModels[index];
  }

  
  @Override
  public IObjectModel[] getAllMembers () {
    return memberModels;
  }
  
    
  public Object getFieldValue () {
    return elems;
  }
  
  
  @Override
  public void dump(int level) {
    for (int i = 0; i < level; i++) {
      System.out.print("  ");
    }
    if (getFieldValue() == null) {
      System.out.println(getPathName() + " = null");
    } else {
      System.out.println(getPathName() + " {");
    }
    for (IObjectModel model : getChildren()) {
      model.dump(level + 1);
    }
    for (int i = 0; i < level; i++) {
      System.out.print("  ");
    }
    System.out.println("}");
  }


  @Override
  public boolean isArrayModel() {
    return true;
  }


  @Override
  public void fireInitialFieldEvents (FieldEventListener x, boolean isSourceTrigger) {
    for (IObjectModel model : getChildren()) {
      model.fireInitialFieldEvents(x, isSourceTrigger);
    }
  }


  @Override
  public Object getUnderlyingValue(Object parentRef) {
    int index = (Integer)parentRef;
    return elems[index];
  }


  @Override
  public void setUnderlyingValue(Object parentRef, Object value) {
    int index = (Integer)parentRef;
    elems[index] = value;
  }

  
  @Override
  public String getLabel() {
    return arrayPlan.getStaticLabel();
  }

  
  @Override
  public boolean isSimpleArray() {
    return arrayPlan.getElementPlan().isSolitary();
  }


  @Override
  public Object getKey(IObjectModel child) {
    for (int i = 0; i < memberModels.length; i++) {
      if (memberModels[i] == child) {
        return new Integer(i);
      }
    }
    throw new IllegalArgumentException(child.toString());
  }

}
