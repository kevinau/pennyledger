package org.pennyledger.form.model;

import org.jaxen.BaseXPath;
import org.jaxen.JaxenException;
import org.pennyledger.form.xpath.DocumentNavigator;

/**
 * An XPath implementation for the FormModel model
 * 
 * <p>
 * This is the main entry point for matching an XPath against a DOM tree. You
 * create a compiled XPath object, then match it against one or more context
 * nodes using the {@link #selectNodes(Object)} method, as in the following
 * example:
 * </p>
 * 
 * <pre>
 * Node node = ...;
 * XPath path = new FormXPath("a/b/c");
 * List results = path.selectNodes(node);
 * </pre>
 * 
 * @see BaseXPath
 */
public class FormXPath extends BaseXPath {
  /**
     * 
     */
  private static final long serialVersionUID = -75510941087659775L;

  /**
   * Construct given an XPath expression string.
   * 
   * @param xpathExpr
   *          the XPath expression
   * 
   * @throws JaxenException
   *           if there is a syntax error while parsing the expression
   */
  public FormXPath(String xpathExpr) throws JaxenException {
    super(xpathExpr, DocumentNavigator.getInstance());
  }
}
