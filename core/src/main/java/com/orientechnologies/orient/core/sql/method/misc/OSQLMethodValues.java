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

import com.orientechnologies.orient.core.command.OCommandContext;
import com.orientechnologies.orient.core.db.record.OIdentifiable;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.executor.OResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Johann Sorel (Geomatys)
 * @author Luca Garulli (l.garulli--(at)--orientdb.com)
 */
public class OSQLMethodValues extends OAbstractSQLMethod {

  public static final String NAME = "values";

  public OSQLMethodValues() {
    super(NAME);
  }

  @Override
  public Object execute(
      Object iThis,
      OIdentifiable iCurrentRecord,
      OCommandContext iContext,
      Object ioResult,
      Object[] iParams) {
    if (ioResult instanceof Map) {
      return ((Map<?, ?>) ioResult).values();
    }
    if (ioResult instanceof ODocument doc) {
      var propertyNames = doc.getPropertyNames();
      var result = new ArrayList<>(propertyNames.size());

      for (String propertyName : propertyNames) {
        result.add(doc.getProperty(propertyName));
      }

      return result;
    }
    if (ioResult instanceof OResult) {
      OResult res = (OResult) ioResult;
      return res.getPropertyNames().stream()
          .map(field -> res.getProperty(field))
          .collect(Collectors.toList());
    }
    if (ioResult instanceof Collection) {
      List result = new ArrayList();
      for (Object o : (Collection) ioResult) {
        result.addAll((Collection) execute(iThis, iCurrentRecord, iContext, o, iParams));
      }
      return result;
    }
    return null;
  }
}
