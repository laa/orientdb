/* Generated By:JJTree: Do not edit this line. OBothPathItem.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import com.orientechnologies.orient.core.sql.executor.OResult;
import java.util.Map;

public class OBothPathItem extends OMatchPathItem {
  public OBothPathItem(int id) {
    super(id);
  }

  public OBothPathItem(OrientSql p, int id) {
    super(p, id);
  }

  @Override
  public void toString(Map<Object, Object> params, StringBuilder builder) {
    builder.append("-");
    boolean first = true;
    if (this.method.params != null) {
      for (OExpression exp : this.method.params) {
        if (!first) {
          builder.append(", ");
        }
        builder.append(exp.execute((OResult) null, null));
        first = false;
      }
    }
    builder.append("-");
    if (filter != null) {
      filter.toString(params, builder);
    }
  }

  @Override
  public void toGenericStatement(StringBuilder builder) {
    builder.append("-");
    boolean first = true;
    if (this.method.params != null) {
      for (OExpression exp : this.method.params) {
        if (!first) {
          builder.append(", ");
        }
        exp.toGenericStatement(builder);
        first = false;
      }
    }
    builder.append("-");
    if (filter != null) {
      filter.toGenericStatement(builder);
    }
  }
}
/* JavaCC - OriginalChecksum=061ff26f18cfa0c561ce9b98ef919173 (do not edit this line) */
