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
package com.orientechnologies.orient.test.database.auto;

import java.io.IOException;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

@Test
public class AlterDatabaseTest extends DocumentDBBaseTest {
  @Parameters(value = "url")
  public AlterDatabaseTest(@Optional String url) {
    super(url);
  }

  public void alterDateFormatOk() throws IOException {
    database.command("alter database dateformat 'yyyy-MM-dd';").close();
    database.command("alter database dateformat 'yyyy-MM-dd'").close();
  }
}
