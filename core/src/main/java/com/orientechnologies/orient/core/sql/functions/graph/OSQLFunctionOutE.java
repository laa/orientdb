package com.orientechnologies.orient.core.sql.functions.graph;

import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.record.ODirection;

/** Created by luigidellaquila on 03/01/17. */
public class OSQLFunctionOutE extends OSQLFunctionMove {
  public static final String NAME = "outE";

  public OSQLFunctionOutE() {
    super(NAME, 0, -1);
  }

  @Override
  protected Object move(
      final ODatabaseSession graph, final OIdentifiable iRecord, final String[] iLabels) {
    return v2e(graph, iRecord, ODirection.OUT, iLabels);
  }
}
