package com.orientechnologies.orient.test.server.network.http;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests HTTP "cluster" command.
 *
 * @author Luca Garulli (l.garulli--(at)--orientdb.com) (l.garulli--at-orientdb.com)
 */
public class HttpClusterTest extends BaseHttpDatabaseTest {
  @Test
  public void testExistentClass() throws Exception {
    Assert.assertEquals(
        get("cluster/" + getDatabaseName() + "/OUser").getResponse().getCode(), 200);
  }

  @Test
  public void testNonExistentClass() throws Exception {
    Assert.assertEquals(
        get("cluster/" + getDatabaseName() + "/NonExistentCLass").getResponse().getCode(), 404);
  }

  @Override
  public String getDatabaseName() {
    return "httpcluster";
  }
}
