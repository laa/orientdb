package com.orientechnologies.orient.core.sql.executor;

import com.orientechnologies.BaseMemoryDatabase;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Luigi Dell'Aquila (l.dellaquila-(at)-orientdb.com)
 */
public class OUpdateEdgeStatementExecutionTest extends BaseMemoryDatabase {

  @Test
  public void testUpdateEdge() {

    db.command("create class V1 extends V");

    db.command("create class E1 extends E");

    db.getMetadata().getSchema().reload();

    // VERTEXES
    ODocument v1 = db.command(new OCommandSQL("create vertex")).execute();
    Assert.assertEquals(v1.getClassName(), "V");

    ODocument v2 = db.command(new OCommandSQL("create vertex V1")).execute();
    Assert.assertEquals(v2.getClassName(), "V1");

    ODocument v3 =
        db.command(new OCommandSQL("create vertex set vid = 'v3', brand = 'fiat'")).execute();
    Assert.assertEquals(v3.getClassName(), "V");
    Assert.assertEquals(v3.field("brand"), "fiat");

    ODocument v4 =
        db.command(new OCommandSQL("create vertex V1 set vid = 'v4',  brand = 'fiat',name = 'wow'"))
            .execute();
    Assert.assertEquals(v4.getClassName(), "V1");
    Assert.assertEquals(v4.field("brand"), "fiat");
    Assert.assertEquals(v4.field("name"), "wow");

    OResultSet edges =
        db.command("create edge E1 from " + v1.getIdentity() + " to " + v2.getIdentity());
    Assert.assertTrue(edges.hasNext());
    OResult edge = edges.next();
    Assert.assertFalse(edges.hasNext());
    Assert.assertEquals(((ODocument) edge.getElement().get().getRecord()).getClassName(), "E1");
    edges.close();

    db.command(
        "update edge E1 set out = "
            + v3.getIdentity()
            + ", in = "
            + v4.getIdentity()
            + " where @rid = "
            + edge.getElement().get().getIdentity());

    OResultSet result = db.query("select expand(out('E1')) from " + v3.getIdentity());
    Assert.assertTrue(result.hasNext());
    OResult vertex4 = result.next();
    Assert.assertEquals(vertex4.getProperty("vid"), "v4");
    Assert.assertFalse(result.hasNext());
    result.close();

    result = db.query("select expand(in('E1')) from " + v4.getIdentity());
    Assert.assertTrue(result.hasNext());
    OResult vertex3 = result.next();
    Assert.assertEquals(vertex3.getProperty("vid"), "v3");
    Assert.assertFalse(result.hasNext());
    result.close();

    result = db.query("select expand(out('E1')) from " + v1.getIdentity());
    Assert.assertFalse(result.hasNext());
    result.close();

    result = db.query("select expand(in('E1')) from " + v2.getIdentity());
    Assert.assertFalse(result.hasNext());
    result.close();
  }

  @Test
  public void testUpdateEdgeOfTypeE() {
    // issue #6378
    ODocument v1 = db.command(new OCommandSQL("create vertex")).execute();
    ODocument v2 = db.command(new OCommandSQL("create vertex")).execute();
    ODocument v3 = db.command(new OCommandSQL("create vertex")).execute();

    OResultSet edges =
        db.command("create edge E from " + v1.getIdentity() + " to " + v2.getIdentity());
    OResult edge = edges.next();

    db.command(
        "UPDATE EDGE " + edge.getElement().get().getIdentity() + " SET in = " + v3.getIdentity());

    Iterable<ODocument> result =
        db.command(new OSQLSynchQuery<ODocument>("select expand(out()) from " + v1.getIdentity()))
            .execute();
    Assert.assertEquals(result.iterator().next().getIdentity(), v3.getIdentity());

    result =
        db.command(new OSQLSynchQuery<ODocument>("select expand(in()) from " + v3.getIdentity()))
            .execute();
    Assert.assertEquals(result.iterator().next().getIdentity(), v1.getIdentity());

    result =
        db.command(new OSQLSynchQuery<ODocument>("select expand(in()) from " + v2.getIdentity()))
            .execute();
    Assert.assertFalse(result.iterator().hasNext());
  }
}
