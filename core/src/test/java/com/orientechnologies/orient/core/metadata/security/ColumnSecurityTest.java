package com.orientechnologies.orient.core.metadata.security;

import com.orientechnologies.orient.core.db.*;
import com.orientechnologies.orient.core.index.OIndexException;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import org.junit.*;

public class ColumnSecurityTest {

  static String DB_NAME = "test";
  static OrientDB orient;
  private ODatabaseSession db;

  @BeforeClass
  public static void beforeClass() {
    orient = new OrientDB("plocal:.", OrientDBConfig.defaultConfig());
  }

  @AfterClass
  public static void afterClass() {
    orient.close();
  }

  @Before
  public void before() {
    orient.create("test", ODatabaseType.MEMORY);
    this.db = orient.open(DB_NAME, "admin", "admin");
  }

  @After
  public void after() {
    this.db.close();
    orient.drop("test");
    this.db = null;
  }

  @Test
  public void testIndexWithPolicy1() {
    OSecurityInternal security = ((ODatabaseInternal) db).getSharedContext().getSecurity();

    OClass person = db.createClass("Person");
    person.createProperty("name", OType.STRING);

    OSecurityPolicy policy = security.createSecurityPolicy(db, "testPolicy");
    policy.setActive(true);
    policy.setReadRule("name = 'foo'");
    security.saveSecurityPolicy(db, policy);
    security.setSecurityPolicy(db, security.getRole(db, "reader"), "database.class.Person.name", policy);

    try {
      db.command("create index Person.name on Person (name) NOTUNIQUE");
      Assert.fail();
    } catch (OIndexException e) {
    }
  }

  @Test
  public void testIndexWithPolicy2() {
    OSecurityInternal security = ((ODatabaseInternal) db).getSharedContext().getSecurity();

    OClass person = db.createClass("Person");
    person.createProperty("name", OType.STRING);

    OSecurityPolicy policy = security.createSecurityPolicy(db, "testPolicy");
    policy.setActive(true);
    policy.setCreateRule("name = 'foo'");
    policy.setBeforeUpdateRule("name = 'foo'");
    policy.setAfterUpdateRule("name = 'foo'");
    policy.setDeleteRule("name = 'foo'");

    security.saveSecurityPolicy(db, policy);
    security.setSecurityPolicy(db, security.getRole(db, "reader"), "database.class.Person.name", policy);

    db.command("create index Person.name on Person (name) NOTUNIQUE");
  }

  @Test
  public void testIndexWithPolicy3() {
    OSecurityInternal security = ((ODatabaseInternal) db).getSharedContext().getSecurity();

    OClass person = db.createClass("Person");
    person.createProperty("name", OType.STRING);

    OSecurityPolicy policy = security.createSecurityPolicy(db, "testPolicy");
    policy.setActive(true);
    policy.setReadRule("name = 'foo'");
    security.saveSecurityPolicy(db, policy);
    security.setSecurityPolicy(db, security.getRole(db, "reader"), "database.class.Person.surname", policy);

    db.command("create index Person.name on Person (name) NOTUNIQUE");
  }
}