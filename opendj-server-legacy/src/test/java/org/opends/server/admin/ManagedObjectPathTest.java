/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions Copyright [year] [name of copyright owner]".
 *
 * Copyright 2008 Sun Microsystems, Inc.
 * Portions Copyright 2014-2016 ForgeRock AS.
 */
package org.opends.server.admin;

import static org.opends.server.TestCaseUtils.*;
import static org.testng.Assert.*;

import org.opends.server.DirectoryServerTestCase;
import org.opends.server.TestCaseUtils;
import org.opends.server.admin.std.client.ConnectionHandlerCfgClient;
import org.opends.server.admin.std.client.GlobalCfgClient;
import org.opends.server.admin.std.client.LDAPConnectionHandlerCfgClient;
import org.opends.server.admin.std.meta.ConnectionHandlerCfgDefn;
import org.opends.server.admin.std.meta.GlobalCfgDefn;
import org.opends.server.admin.std.meta.LDAPConnectionHandlerCfgDefn;
import org.opends.server.admin.std.meta.ReplicationDomainCfgDefn;
import org.opends.server.admin.std.meta.ReplicationSynchronizationProviderCfgDefn;
import org.opends.server.admin.std.meta.RootCfgDefn;
import org.opends.server.admin.std.server.ConnectionHandlerCfg;
import org.opends.server.admin.std.server.GlobalCfg;
import org.opends.server.admin.std.server.LDAPConnectionHandlerCfg;
import org.forgerock.opendj.ldap.DN;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * ManagedObjectPath test cases.
 */
public class ManagedObjectPathTest extends DirectoryServerTestCase {

  /**
   * Sets up tests
   *
   * @throws Exception
   *           If the server could not be initialized.
   */
  @BeforeClass
  public void setUp() throws Exception {
    // This test suite depends on having the schema available, so
    // we'll start the server.
    TestCaseUtils.startServer();
  }



  /**
   * Tests that the empty path is empty.
   */
  @Test
  public void testEmptyPathIsEmpty() {
    ManagedObjectPath<?, ?> path = ManagedObjectPath.emptyPath();
    assertTrue(path.isEmpty());
  }



  /**
   * Tests that the empty path has a size of zero.
   */
  @Test
  public void testEmptyPathHasZeroElements() {
    ManagedObjectPath<?, ?> path = ManagedObjectPath.emptyPath();
    assertEquals(path.size(), 0);
  }



  /**
   * Tests that the empty path has no parent.
   */
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testEmptyPathHasNoParent() {
    ManagedObjectPath<?, ?> path = ManagedObjectPath.emptyPath();
    path.parent();
  }



  /**
   * Tests that the empty path represents the root configuration.
   */
  @Test
  public void testEmptyPathIsRootConfiguration() {
    ManagedObjectPath<?, ?> path = ManagedObjectPath.emptyPath();
    assertEquals(path.getManagedObjectDefinition(), RootCfgDefn.getInstance());
  }



  /**
   * Tests that the empty path represents has no relation.
   */
  @Test
  public void testEmptyPathHasNoRelation() {
    ManagedObjectPath<?, ?> path = ManagedObjectPath.emptyPath();
    assertNull(path.getRelationDefinition());
  }



  /**
   * Tests that the empty path has no name.
   */
  @Test
  public void testEmptyPathHasNoName() {
    ManagedObjectPath<?, ?> path = ManagedObjectPath.emptyPath();
    assertNull(path.getName());
  }



  /**
   * Tests that the empty path has a string representation of "/".
   */
  @Test
  public void testEmptyPathString() {
    ManagedObjectPath<?, ?> path = ManagedObjectPath.emptyPath();
    assertEquals(path.toString(), "/");
  }



  /**
   * Tests that the empty path can be decoded.
   */
  @Test
  public void testEmptyPathDecode() {
    ManagedObjectPath<?, ?> path = ManagedObjectPath.valueOf("/");
    assertEquals(path, ManagedObjectPath.emptyPath());
  }



  /**
   * Tests singleton child creation.
   */
  @Test
  public void testSingletonChild() {
    ManagedObjectPath<?, ?> path = ManagedObjectPath.emptyPath();
    ManagedObjectPath<GlobalCfgClient, GlobalCfg> child = path
        .child(RootCfgDefn.getInstance()
            .getGlobalConfigurationRelationDefinition());

    assertFalse(child.isEmpty());
    assertEquals(child.size(), 1);
    assertEquals(child.parent(), path);
    assertNull(child.getName());
    assertEquals(child.getManagedObjectDefinition(), GlobalCfgDefn
        .getInstance());
    assertEquals(child.getRelationDefinition(), RootCfgDefn.getInstance()
        .getGlobalConfigurationRelationDefinition());
    assertEquals(child.toString(), "/relation=global-configuration");
    assertEquals(child, ManagedObjectPath
        .valueOf("/relation=global-configuration"));
  }



  /**
   * Tests instantiable child creation.
   */
  @Test
  public void testInstantiableChild() {
    ManagedObjectPath<?, ?> path = ManagedObjectPath.emptyPath();
    ManagedObjectPath<ConnectionHandlerCfgClient, ConnectionHandlerCfg> child = path
        .child(RootCfgDefn.getInstance()
            .getConnectionHandlersRelationDefinition(),
            "LDAP connection handler");

    assertFalse(child.isEmpty());
    assertEquals(child.size(), 1);
    assertEquals(child.parent(), path);
    assertEquals(child.getName(), "LDAP connection handler");
    assertEquals(child.getManagedObjectDefinition(), ConnectionHandlerCfgDefn
        .getInstance());
    assertEquals(child.getRelationDefinition(), RootCfgDefn.getInstance()
        .getConnectionHandlersRelationDefinition());
    assertEquals(child.toString(),
        "/relation=connection-handler+name=LDAP connection handler");
    assertEquals(child, ManagedObjectPath
        .valueOf("/relation=connection-handler+name=LDAP connection handler"));
  }



  /**
   * Tests instantiable child creation with specific sub-type.
   */
  @Test
  public void testInstantiableChildWithSubtype() {
    ManagedObjectPath<?, ?> path = ManagedObjectPath.emptyPath();
    ManagedObjectPath<LDAPConnectionHandlerCfgClient, LDAPConnectionHandlerCfg> child = path
        .child(RootCfgDefn.getInstance()
            .getConnectionHandlersRelationDefinition(),
            LDAPConnectionHandlerCfgDefn.getInstance(),
            "LDAP connection handler");

    assertFalse(child.isEmpty());
    assertEquals(child.size(), 1);
    assertEquals(child.parent(), path);
    assertEquals(child.getManagedObjectDefinition(),
        LDAPConnectionHandlerCfgDefn.getInstance());
    assertEquals(child.getRelationDefinition(), RootCfgDefn.getInstance()
        .getConnectionHandlersRelationDefinition());
    assertEquals(
        child.toString(),
        "/relation=connection-handler+type=ldap-connection-handler+name=LDAP connection handler");
    assertEquals(
        child,
        ManagedObjectPath
            .valueOf("/relation=connection-handler+type=ldap-connection-handler+name=LDAP connection handler"));
  }



  /**
   * Tests instantiable child creation with multiple levels.
   */
  @Test
  public void testInstantiableMultipleLevels() {
    ManagedObjectPath<?, ?> root = ManagedObjectPath.emptyPath();
    ManagedObjectPath<?, ?> mmr = root.child(RootCfgDefn.getInstance()
        .getSynchronizationProvidersRelationDefinition(),
        ReplicationSynchronizationProviderCfgDefn.getInstance(), "MMR");
    ManagedObjectPath<?, ?> domain = mmr.child(
        ReplicationSynchronizationProviderCfgDefn.getInstance()
            .getReplicationDomainsRelationDefinition(), "Domain");
    assertFalse(domain.isEmpty());
    assertEquals(domain.size(), 2);
    assertEquals(domain.parent(), mmr);
    assertEquals(domain.parent(2), root);
    assertEquals(domain.getManagedObjectDefinition(), ReplicationDomainCfgDefn
        .getInstance());
    assertEquals(domain.getRelationDefinition(),
        ReplicationSynchronizationProviderCfgDefn.getInstance()
            .getReplicationDomainsRelationDefinition());
    assertEquals(
        domain.toString(),
        "/relation=synchronization-provider+type=replication-synchronization-provider+name=MMR/relation=replication-domain+name=Domain");
    assertEquals(
        domain,
        ManagedObjectPath
            .valueOf("/relation=synchronization-provider+type=replication-synchronization-provider+name=MMR/relation=replication-domain+name=Domain"));
  }

  /**
   * Tests matches and equals methods behave differently.
   */
  @Test
  public void testMatches() {
    ManagedObjectPath<?, ?> path = ManagedObjectPath.emptyPath();

    ManagedObjectPath<ConnectionHandlerCfgClient, ConnectionHandlerCfg> child1 = path
        .child(RootCfgDefn.getInstance()
            .getConnectionHandlersRelationDefinition(),
            "LDAP connection handler");

    ManagedObjectPath<LDAPConnectionHandlerCfgClient, LDAPConnectionHandlerCfg> child2 = path
        .child(RootCfgDefn.getInstance()
            .getConnectionHandlersRelationDefinition(),
            LDAPConnectionHandlerCfgDefn.getInstance(),
            "LDAP connection handler");

    ManagedObjectPath<LDAPConnectionHandlerCfgClient, LDAPConnectionHandlerCfg> child3 = path
        .child(RootCfgDefn.getInstance()
            .getConnectionHandlersRelationDefinition(),
            LDAPConnectionHandlerCfgDefn.getInstance(),
            "Another LDAP connection handler");

    assertTrue(child1.matches(child1));
    assertTrue(child2.matches(child2));
    assertTrue(child1.matches(child2));
    assertTrue(child2.matches(child1));

    assertEquals(child1, child1);
    assertEquals(child2, child2);
    assertNotEquals(child1, child2);
    assertNotEquals(child2, child1);

    assertFalse(child1.matches(child3));
    assertFalse(child2.matches(child3));
    assertFalse(child3.matches(child1));
    assertFalse(child3.matches(child2));

    assertNotEquals(child1, child3);
    assertNotEquals(child2, child3);
    assertNotEquals(child3, child1);
    assertNotEquals(child3, child2);
  }

  /**
   * Tests toDN method.
   *
   * @throws Exception
   *           If an unexpected error occurred.
   */
  @Test
  public void testToDN() throws Exception {
    ManagedObjectPath<?, ?> path = ManagedObjectPath.emptyPath();

    ManagedObjectPath<ConnectionHandlerCfgClient, ConnectionHandlerCfg> child1 = path
        .child(RootCfgDefn.getInstance()
            .getConnectionHandlersRelationDefinition(),
            "LDAP connection handler");

    ManagedObjectPath<LDAPConnectionHandlerCfgClient, LDAPConnectionHandlerCfg> child2 = path
        .child(RootCfgDefn.getInstance()
            .getConnectionHandlersRelationDefinition(),
            LDAPConnectionHandlerCfgDefn.getInstance(),
            "LDAP connection handler");

    ManagedObjectPath<LDAPConnectionHandlerCfgClient, LDAPConnectionHandlerCfg> child3 = path
        .child(RootCfgDefn.getInstance()
            .getConnectionHandlersRelationDefinition(),
            LDAPConnectionHandlerCfgDefn.getInstance(),
            "Another LDAP connection handler");

    DN expectedEmpty = DN.rootDN();
    DN expectedChild1 = DN.valueOf("cn=LDAP connection handler,cn=connection handlers,cn=config");
    DN expectedChild2 = DN.valueOf("cn=LDAP connection handler,cn=connection handlers,cn=config");
    DN expectedChild3 = DN.valueOf("cn=Another LDAP connection handler,cn=connection handlers,cn=config");

    assertEquals(path.toDN(), expectedEmpty);
    assertEquals(child1.toDN(), expectedChild1);
    assertEquals(child2.toDN(), expectedChild2);
    assertEquals(child3.toDN(), expectedChild3);
  }
}
