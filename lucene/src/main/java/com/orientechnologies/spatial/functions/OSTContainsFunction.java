/**
 * Copyright 2010-2016 OrientDB LTD (http://orientdb.com)
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * <p>For more information: http://www.orientdb.com
 */
package com.orientechnologies.spatial.functions;

import com.orientechnologies.orient.core.command.OCommandContext;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.sql.parser.OBinaryCompareOperator;
import com.orientechnologies.orient.core.sql.parser.OExpression;
import com.orientechnologies.orient.core.sql.parser.OFromClause;
import com.orientechnologies.spatial.shape.OShapeFactory;
import com.orientechnologies.spatial.strategy.SpatialQueryBuilderContains;
import org.locationtech.spatial4j.shape.Shape;

/** Created by Enrico Risa on 12/08/15. */
public class OSTContainsFunction extends OSpatialFunctionAbstractIndexable {

  public static final String NAME = "st_contains";

  private OShapeFactory factory = OShapeFactory.INSTANCE;

  public OSTContainsFunction() {
    super(NAME, 2, 2);
  }

  @Override
  public Object execute(
      Object iThis,
      OIdentifiable iCurrentRecord,
      Object iCurrentResult,
      Object[] iParams,
      OCommandContext iContext) {

    if (containsNull(iParams)) {
      return null;
    }

    Shape shape = toShape(iParams[0]);

    Shape shape1 = toShape(iParams[1]);

    return factory.operation().contains(shape, shape1);
  }

  @Override
  protected boolean isValidBinaryOperator(OBinaryCompareOperator operator) {
    return true;
  }

  @Override
  public String getSyntax() {
    return null;
  }

  @Override
  protected String operator() {
    return SpatialQueryBuilderContains.NAME;
  }

  @Override
  public Iterable<OIdentifiable> searchFromTarget(
      OFromClause target,
      OBinaryCompareOperator operator,
      Object rightValue,
      OCommandContext ctx,
      OExpression... args) {
    return results(target, args, ctx, rightValue);
  }
}
