/*
 * Copyright 2010-2016 OrientDB LTD (http://orientdb.com)
 * Copyright 2013 Geomatys.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.orientechnologies.orient.core.sql.method.misc;

import com.orientechnologies.common.io.OIOUtils;
import com.orientechnologies.orient.core.command.OCommandContext;
import com.orientechnologies.orient.core.db.record.OIdentifiable;

/**
 * @author Luca Garulli (l.garulli--(at)--orientdb.com)
 */
public class OSQLMethodLastIndexOf extends OAbstractSQLMethod {

  public static final String NAME = "lastindexof";

  public OSQLMethodLastIndexOf() {
    super(NAME, 1, 2);
  }

  @Override
  public Object execute(
      Object iThis,
      OIdentifiable iCurrentRecord,
      OCommandContext iContext,
      Object ioResult,
      Object[] iParams) {
    final String toFind = OIOUtils.getStringContent(iParams[0].toString());
    if (iParams.length > 1) {
      return iThis.toString().lastIndexOf(toFind, Integer.parseInt(iParams[1].toString()));
    } else {
      return iThis.toString().lastIndexOf(toFind);
    }
  }
}
