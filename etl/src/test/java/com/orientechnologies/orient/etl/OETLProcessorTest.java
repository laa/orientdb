/*
 *
 *  * Copyright 2010-2016 OrientDB LTD (info(-at-)orientdb.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.orientechnologies.orient.etl;

import static org.assertj.core.api.Assertions.assertThat;

import com.orientechnologies.common.io.OFileUtils;
import com.orientechnologies.orient.etl.transformer.OETLVertexTransformer;
import java.io.File;
import org.junit.Before;
import org.junit.Test;

/** Created by frank on 9/18/15. */
public class OETLProcessorTest extends OETLBaseTest {

  private static final String databaseDir = "./target/databases/etlProcessor";

  @Before
  public void setUp() throws Exception {
    OFileUtils.deleteRecursively(new File(databaseDir));
  }

  @Test
  public void testMain() throws Exception {

    try (OETLProcessor processor =
        new OETLProcessorConfigurator()
            .parseConfigAndParameters(
                new String[] {
                  "-dbURL=plocal:" + databaseDir + "orientDBoetl/testMain",
                  "./src/test/resources/comment.json"
                })) {

      assertThat(processor.getContext().getVariable("dbURL"))
          .isEqualTo("plocal:" + databaseDir + "orientDBoetl/testMain");
    }
  }

  @Test
  public void shouldParseSplitConfiguration() throws Exception {

    try (OETLProcessor processor =
        new OETLProcessorConfigurator()
            .parseConfigAndParameters(
                new String[] {
                  "-dbURL=plocal:" + databaseDir + "orientDBoetl/shouldParseSplitConfiguration",
                  "./src/test/resources/comment_split_1.json",
                  "./src/test/resources/comment_split_2.json"
                })) {

      assertThat(processor.getContext().getVariable("dbURL"))
          .isEqualTo("plocal:" + databaseDir + "orientDBoetl/shouldParseSplitConfiguration");
      assertThat(processor.getTransformers().get(0)).isInstanceOf(OETLVertexTransformer.class);
      assertThat(processor.getExtractor().getName()).isEqualTo("csv");
    }
  }

  @Test
  public void shouldExceuteBeginBlocktoExpandVariables() throws Exception {

    try (OETLProcessor processor =
        new OETLProcessorConfigurator()
            .parseConfigAndParameters(
                new String[] {
                  "-dbURL=plocal:"
                      + databaseDir
                      + "orientDBoetl/shouldExceuteBeginBlocktoExpandVariables",
                  "./src/test/resources/comment.json"
                })) {

      assertThat(processor.context.getVariable("filePath"))
          .isEqualTo("./src/test/resources/comments.csv");
    }
  }
}
