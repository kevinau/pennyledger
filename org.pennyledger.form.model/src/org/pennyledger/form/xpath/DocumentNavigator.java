package org.pennyledger.form.xpath;

import java.util.Iterator;

import org.jaxen.DefaultNavigator;
import org.jaxen.FunctionCallException;
import org.jaxen.JaxenConstants;
import org.jaxen.NamedAccessNavigator;
import org.jaxen.Navigator;
import org.jaxen.XPath;
import org.jaxen.util.SingleObjectIterator;
import org.pennyledger.form.model.FormXPath;
import org.pennyledger.form.model.IArrayModel;
import org.pennyledger.form.model.IContainerModel;
import org.pennyledger.form.model.IFieldModel;
import org.pennyledger.form.model.IFormModel;
import org.pennyledger.form.model.IGroupModel;
import org.pennyledger.form.model.IListModel;
import org.pennyledger.form.model.IObjectModel;


/**
 * Interface for navigating around a JavaBean object model.
 * 
 * <p>
 * This class is not intended for direct usage, but is used by the Jaxen engine
 * during evaluation.
 * </p>
 * 
 * @see XPath
 * 
 */
public class DocumentNavigator extends DefaultNavigator implements NamedAccessNavigator {

  /**
     * 
     */
  private static final long serialVersionUID = -1768605107626726499L;

  /**
   * Singleton implementation.
   */
  private static final DocumentNavigator instance = new DocumentNavigator();

  /**
   * Retrieve the singleton instance of this <code>DocumentNavigator</code>.
   */
  public static Navigator getInstance() {
    return instance;
  }

  @Override
  public boolean isElement(Object obj) {
    return (obj instanceof IObjectModel);
  }

  @Override
  public boolean isComment(Object obj) {
    return false;
  }

  @Override
  public boolean isText(Object obj) {
    return (obj instanceof String);
  }

  @Override
  public boolean isAttribute(Object obj) {
    return false;
  }

  @Override
  public boolean isProcessingInstruction(Object obj) {
    return false;
  }

  @Override
  public boolean isDocument(Object obj) {
    return false;
  }

  @Override
  public boolean isNamespace(Object obj) {
    return false;
  }

  @Override
  public String getElementName(Object obj) {
    IObjectModel model = (IObjectModel)obj;
    IContainerModel p = model.getParent();
    if (p == null) {
      return "form";
    } else if (p instanceof IArrayModel || p instanceof IListModel) {
      return "elem";
    } else {
      return p.getKey(model).toString();
    }
  }

  
  @Override
  public String getElementNamespaceUri(Object obj) {
    return "";
  }

  
  @Override
  public String getElementQName(Object obj) {
    return "";
  }
  

  @Override
  public String getAttributeName(Object obj) {
    return "";
  }

  
  @Override
  public String getAttributeNamespaceUri(Object obj) {
    return "";
  }

  
  @Override
  public String getAttributeQName(Object obj) {
    return "";
  }

  
  @Override
  public Iterator<?> getChildAxisIterator(Object contextNode) {
    if (contextNode instanceof IContainerModel) {
      return ((IContainerModel)contextNode).getChildren().iterator();
    } else if (contextNode instanceof IFormModel) {
      return new SingleObjectIterator(((IFormModel<?>)contextNode).getRootModel());
    }
    return JaxenConstants.EMPTY_ITERATOR;
  }

  
  /**
   * Retrieves an <code>Iterator</code> over the child elements that match the
   * supplied name.
   * 
   * @param contextNode
   *          the origin context node
   * @param localName
   *          the local name of the children to return, always present
   * @param namespacePrefix
   *          the prefix of the namespace of the children to return
   * @param namespaceURI
   *          the namespace URI of the children to return
   * @return an Iterator that traverses the named children, or null if none
   */
  @Override
  public Iterator<?> getChildAxisIterator(Object contextNode, String localName, String namespacePrefix, String namespaceURI) {
    if (contextNode instanceof IGroupModel) {
      IObjectModel model = ((IGroupModel)contextNode).getMember(localName);
      if (model != null) {
        return new SingleObjectIterator(model);
      } else {
        return JaxenConstants.EMPTY_ITERATOR;
      }
    } else if (contextNode instanceof IFormModel) {
      if (localName.equals(IFormModel.ROOT_NAME)) {
        return new SingleObjectIterator(((IFormModel<?>)contextNode).getRootModel());
      } else {
        return JaxenConstants.EMPTY_ITERATOR;
      }
    }
    return JaxenConstants.EMPTY_ITERATOR;
  }

  
  @Override
  public Iterator<?> getParentAxisIterator(Object contextNode) {
    if (contextNode instanceof IFormModel) {
      return JaxenConstants.EMPTY_ITERATOR;
    } else {
      IObjectModel model = (IObjectModel)contextNode;
      IObjectModel parentModel = model.getParent();
      if (parentModel == null) {
        return JaxenConstants.EMPTY_ITERATOR;
      } else {
        return new SingleObjectIterator(parentModel);
      }
    }
  }

  
  @Override
  public Iterator<?> getAttributeAxisIterator(Object contextNode) {
    return JaxenConstants.EMPTY_ITERATOR;
  }

  
  /**
   * Retrieves an <code>Iterator</code> over the attribute elements that match
   * the supplied name.
   * 
   * @param contextNode
   *          the origin context node
   * @param localName
   *          the local name of the attributes to return, always present
   * @param namespacePrefix
   *          the prefix of the namespace of the attributes to return
   * @param namespaceURI
   *          the namespace URI of the attributes to return
   * @return an Iterator that traverses the named attributes, not null
   */
  @Override
  public Iterator<?> getAttributeAxisIterator(Object contextNode, String localName, String namespacePrefix, String namespaceURI) {
    return JaxenConstants.EMPTY_ITERATOR;
  }

  
  @Override
  public Iterator<?> getNamespaceAxisIterator(Object contextNode) {
    return JaxenConstants.EMPTY_ITERATOR;
  }

  
  @Override
  public Object getDocumentNode(Object contextNode) {
    if (contextNode instanceof IFormModel) {
      return contextNode;
    } else {
      return ((IObjectModel)contextNode).getOwnerForm();
    }
  }

  
  @Override
  public Object getParentNode(Object contextNode) {
    if (contextNode instanceof IFormModel) {
      return null;
    } else {
      return ((IObjectModel)contextNode).getParent();
    }
  }

  
  @Override
  public String getTextStringValue(Object obj) {
    return obj.toString();
  }

  
  @Override
  public String getElementStringValue(Object obj) {
    if (obj instanceof IFieldModel) {
      return ((IFieldModel)obj).toString();
    }
    return obj.toString();
  }

  
  @Override
  public String getAttributeStringValue(Object obj) {
    return obj.toString();
  }

  
  @Override
  public String getNamespaceStringValue(Object obj) {
    return obj.toString();
  }

  
  @Override
  public String getNamespacePrefix(Object obj) {
    return null;
  }

  
  @Override
  public String getCommentStringValue(Object obj) {
    return null;
  }

  
  @Override
  public String translateNamespacePrefixToUri(String prefix, Object context) {
    return null;
  }

  
  @Override
  public short getNodeType(Object node) {
    return 0;
  }

  
  @Override
  public Object getDocument(String uri) throws FunctionCallException {
    return null;
  }

  
  @Override
  public String getProcessingInstructionTarget(Object obj) {
    return null;
  }

  
  @Override
  public String getProcessingInstructionData(Object obj) {
    return null;
  }

  
  @Override
  public XPath parseXPath(String xpath) throws org.jaxen.saxpath.SAXPathException {
    return new FormXPath(xpath);
  }

  
  protected String javacase(String name) {
    if (name.length() == 0) {
      return name;
    } else if (name.length() == 1) {
      return name.toUpperCase();
    }

    return name.substring(0, 1).toUpperCase() + name.substring(1);
  }
}
