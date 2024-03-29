/* Generated By:JJTree: Do not edit this line. OPositionalParameter.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=true,NODE_PREFIX=O,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package com.orientechnologies.orient.core.sql.parser;

import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultInternal;
import java.util.Map;

public class OPositionalParameter extends OInputParameter {

  protected int paramNumber;

  public OPositionalParameter(int id) {
    super(id);
  }

  public OPositionalParameter(OrientSql p, int id) {
    super(p, id);
  }

  @Override
  public String toString() {
    return "?";
  }

  public void toString(Map<Object, Object> params, StringBuilder builder) {
    Object finalValue = bindFromInputParams(params);
    if (finalValue == this) {
      builder.append("?");
    } else if (finalValue instanceof String) {
      builder.append("\"");
      builder.append(OExpression.encode(finalValue.toString()));
      builder.append("\"");
    } else if (finalValue instanceof SimpleNode) {
      ((SimpleNode) finalValue).toString(params, builder);
    } else {
      builder.append(finalValue);
    }
  }

  public void toGenericStatement(StringBuilder builder) {
    builder.append(PARAMETER_PLACEHOLDER);
  }

  public Object getValue(Map<Object, Object> params) {
    Object result = null;
    if (params != null) {
      result = params.get(paramNumber);
    }
    return result;
  }

  public Object bindFromInputParams(Map<Object, Object> params) {
    if (params != null) {
      Object value = params.get(paramNumber);
      Object result = toParsedTree(value);
      return result;
    }
    return this;
  }

  @Override
  public OPositionalParameter copy() {
    OPositionalParameter result = new OPositionalParameter(-1);
    result.paramNumber = paramNumber;
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    OPositionalParameter that = (OPositionalParameter) o;

    if (paramNumber != that.paramNumber) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return paramNumber;
  }

  public OResult serialize() {
    OResultInternal result = (OResultInternal) super.serialize();
    result.setProperty("paramNumber", paramNumber);
    return result;
  }

  public void deserialize(OResult fromResult) {
    paramNumber = fromResult.getProperty("paramNumber");
  }
}
/* JavaCC - OriginalChecksum=f73bea7d9b3994a9d4e79d2c330d8ba2 (do not edit this line) */
