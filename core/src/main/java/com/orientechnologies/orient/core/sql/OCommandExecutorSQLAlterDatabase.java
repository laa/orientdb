/*
 *
 *  *  Copyright 2010-2016 OrientDB LTD (http://orientdb.com)
 *  *
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *
 *  *       http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *  *
 *  * For more information: http://orientdb.com
 *
 */
package com.orientechnologies.orient.core.sql;

import com.orientechnologies.common.exception.OException;
import com.orientechnologies.orient.core.command.OCommandDistributedReplicateRequest;
import com.orientechnologies.orient.core.command.OCommandRequest;
import com.orientechnologies.orient.core.command.OCommandRequestText;
import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.ODatabaseDocumentInternal;
import com.orientechnologies.orient.core.db.ODatabaseSession;
import com.orientechnologies.orient.core.exception.OCommandExecutionException;
import com.orientechnologies.orient.core.metadata.security.ORole;
import com.orientechnologies.orient.core.metadata.security.ORule;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

/**
 * SQL ALTER DATABASE command: Changes an attribute of the current database.
 *
 * @author Luca Garulli (l.garulli--(at)--orientdb.com)
 */
@SuppressWarnings("unchecked")
public class OCommandExecutorSQLAlterDatabase extends OCommandExecutorSQLAbstract
    implements OCommandDistributedReplicateRequest {
  public static final String KEYWORD_ALTER = "ALTER";
  public static final String KEYWORD_DATABASE = "DATABASE";

  private ODatabaseSession.ATTRIBUTES attribute;
  private String value;

  public OCommandExecutorSQLAlterDatabase parse(final OCommandRequest iRequest) {
    final OCommandRequestText textRequest = (OCommandRequestText) iRequest;

    String queryText = textRequest.getText();
    String originalQuery = queryText;
    try {
      queryText = preParse(queryText, iRequest);
      textRequest.setText(queryText);

      init((OCommandRequestText) iRequest);

      StringBuilder word = new StringBuilder();

      int oldPos = 0;
      int pos = nextWord(parserText, parserTextUpperCase, oldPos, word, true);
      if (pos == -1 || !word.toString().equals(KEYWORD_ALTER))
        throw new OCommandSQLParsingException(
            "Keyword " + KEYWORD_ALTER + " not found. Use " + getSyntax(), parserText, oldPos);

      oldPos = pos;
      pos = nextWord(parserText, parserTextUpperCase, oldPos, word, true);
      if (pos == -1 || !word.toString().equals(KEYWORD_DATABASE))
        throw new OCommandSQLParsingException(
            "Keyword " + KEYWORD_DATABASE + " not found. Use " + getSyntax(), parserText, oldPos);

      oldPos = pos;
      pos = nextWord(parserText, parserTextUpperCase, oldPos, word, true);
      if (pos == -1)
        throw new OCommandSQLParsingException(
            "Missed the database's attribute to change. Use " + getSyntax(), parserText, oldPos);

      final String attributeAsString = word.toString();

      try {
        attribute =
            ODatabaseSession.ATTRIBUTES.valueOf(attributeAsString.toUpperCase(Locale.ENGLISH));
      } catch (IllegalArgumentException e) {
        throw OException.wrapException(
            new OCommandSQLParsingException(
                "Unknown database's attribute '"
                    + attributeAsString
                    + "'. Supported attributes are: "
                    + Arrays.toString(ODatabaseSession.ATTRIBUTES.values()),
                parserText,
                oldPos),
            e);
      }

      value = parserText.substring(pos + 1).trim();

      if (value.length() == 0)
        throw new OCommandSQLParsingException(
            "Missed the database's value to change for attribute '"
                + attribute
                + "'. Use "
                + getSyntax(),
            parserText,
            oldPos);

      if (value.equalsIgnoreCase("null")) value = null;
    } finally {
      textRequest.setText(originalQuery);
    }

    return this;
  }

  @Override
  public long getDistributedTimeout() {
    return getDatabase()
        .getConfiguration()
        .getValueAsLong(OGlobalConfiguration.DISTRIBUTED_COMMAND_QUICK_TASK_SYNCH_TIMEOUT);
  }

  /** Execute the ALTER DATABASE. */
  public Object execute(final Map<Object, Object> iArgs) {
    if (attribute == null)
      throw new OCommandExecutionException(
          "Cannot execute the command because it has not been parsed yet");

    final ODatabaseDocumentInternal database = getDatabase();
    database.checkSecurity(ORule.ResourceGeneric.DATABASE, ORole.PERMISSION_UPDATE);

    database.setInternal(attribute, value);
    return null;
  }

  @Override
  public QUORUM_TYPE getQuorumType() {
    return QUORUM_TYPE.ALL;
  }

  public String getSyntax() {
    return "ALTER DATABASE <attribute-name> <attribute-value>";
  }
}
