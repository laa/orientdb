package com.orientechnologies.orient.test.server.network.http;

import java.io.IOException;
import java.net.URLEncoder;
import org.junit.Assert;

/**
 * @author Luca Garulli (l.garulli--(at)--orientdb.com) (l.garulli--at-orientdb.com)
 */
public class HttpAuthenticationTest extends BaseHttpDatabaseTest {
  public void testChangeOfUserOnSameConnectionIsAllowed() throws IOException {
    Assert.assertEquals(
        get("query/"
                + getDatabaseName()
                + "/sql/"
                + URLEncoder.encode("select from OUSer", "UTF8")
                + "/10")
            .setUserName("root")
            .setUserPassword("root")
            .getResponse()
            .getCode(),
        200);

    Assert.assertEquals(
        get("query/"
                + getDatabaseName()
                + "/sql/"
                + URLEncoder.encode("select from OUSer", "UTF8")
                + "/10")
            .setUserName("admin")
            .setUserPassword("admin")
            .getResponse()
            .getCode(),
        200);
  }

  @Override
  public String getDatabaseName() {
    return "httpauth";
  }
}
