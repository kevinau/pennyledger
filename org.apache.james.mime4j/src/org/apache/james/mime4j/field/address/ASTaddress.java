/* Generated By:JJTree: Do not edit this line. ASTaddress.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=org.apache.james.mime4j.field.address.BaseNode,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package org.apache.james.mime4j.field.address;

public
class ASTaddress extends SimpleNode {
  public ASTaddress(int id) {
    super(id);
  }

  public ASTaddress(AddressListParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(AddressListParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=609111073783c376bce3a52d2fd7d8fe (do not edit this line) */
