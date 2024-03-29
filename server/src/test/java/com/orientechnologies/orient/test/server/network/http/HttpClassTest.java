package com.orientechnologies.orient.test.server.network.http;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests HTTP "class" command.
 *
 * @author Luca Garulli (l.garulli--(at)--orientdb.com) (l.garulli--at-orientdb.com)
 */
public class HttpClassTest extends BaseHttpDatabaseTest {
  @Test
  public void testExistentClass() throws Exception {
    Assert.assertEquals(get("class/" + getDatabaseName() + "/OUser").getResponse().getCode(), 200);
  }

  @Test
  public void testNonExistentClass() throws Exception {
    Assert.assertEquals(
        get("class/" + getDatabaseName() + "/NonExistentCLass").getResponse().getCode(), 404);
  }

  @Test
  public void testCreateClass() throws Exception {
    Assert.assertEquals(
        post("class/" + getDatabaseName() + "/NewClass").getResponse().getCode(), 201);
  }

  @Test
  public void testDropClass() throws Exception {
    Assert.assertEquals(
        post("class/" + getDatabaseName() + "/NewClassToDrop").getResponse().getCode(), 201);
    Assert.assertEquals(
        delete("class/" + getDatabaseName() + "/NewClassToDrop").getResponse().getCode(), 204);
  }

  @Override
  public String getDatabaseName() {
    return "httpclass";
  }
}
