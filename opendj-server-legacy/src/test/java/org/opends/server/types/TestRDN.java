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
 * Copyright 2006-2010 Sun Microsystems, Inc.
 * Portions Copyright 2014-2016 ForgeRock AS.
 */
package org.opends.server.types;

import static org.opends.server.TestCaseUtils.*;
import static org.opends.server.core.DirectoryServer.*;
import static org.testng.Assert.*;

import java.util.Arrays;
import java.util.Iterator;

import org.forgerock.opendj.ldap.AVA;
import org.forgerock.opendj.ldap.ByteString;
import org.forgerock.opendj.ldap.RDN;
import org.forgerock.opendj.ldap.schema.AttributeType;
import org.opends.server.TestCaseUtils;
import org.opends.server.core.DirectoryServer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * This class defines a set of tests for the {@link RDN} class.
 */
public final class TestRDN extends TypesTestCase {

  /** Domain component attribute type. */
  private AttributeType AT_DC;
  /** Common name attribute type. */
  private AttributeType AT_CN;

  /** Test attribute value. */
  private ByteString AV_DC_ORG;
  /** Test attribute value. */
  private ByteString AV_CN;


  /**
   * Set up the environment for performing the tests in this suite.
   *
   * @throws Exception
   *           If the environment could not be set up.
   */
  @BeforeClass
  public void setUp() throws Exception {
    // This test suite depends on having the schema available, so
    // we'll start the server.
    TestCaseUtils.startServer();

    AT_DC = DirectoryServer.getAttributeType("dc");
    AT_CN = DirectoryServer.getAttributeType("cn");

    String attrName = "x-test-integer-type";
    AttributeType dummy = getAttributeType(attrName, getDefaultIntegerSyntax());
    DirectoryServer.getSchema().registerAttributeType(dummy, true);

    AV_DC_ORG = ByteString.valueOfUtf8("org");
    AV_CN = ByteString.valueOfUtf8("hello world");
  }



  /**
   * Test RDN construction with single AVA.
   *
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test
  public void testConstructor() throws Exception {
    RDN rdn = new RDN(AT_DC, AV_DC_ORG);

    assertEquals(rdn.size(), 1);
    AVA ava = rdn.getFirstAVA();
    assertEquals(ava.getAttributeType(), AT_DC);
    assertEquals(ava.getAttributeName(), AT_DC.getNameOrOID());
    assertEquals(ava.getAttributeValue(), AV_DC_ORG);
  }



  /**
   * Test RDN construction with single AVA and a user-defined name.
   *
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test
  public void testConstructorWithName() throws Exception {
    RDN rdn = new RDN(AT_DC, "domainComponent", AV_DC_ORG);

    assertEquals(rdn.size(), 1);
    AVA ava = rdn.getFirstAVA();
    assertEquals(ava.getAttributeType(), AT_DC);
    assertEquals(ava.getAttributeName(), "domainComponent");
    assertEquals(ava.getAttributeValue(), AV_DC_ORG);
  }



  /**
   * Test RDN construction with a multiple AVA elements.
   *
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test
  public void testConstructorMultiAVA() throws Exception {
    RDN rdn = new RDN(new AVA(AT_DC, AV_DC_ORG), new AVA(AT_CN, AV_CN));
    assertEquals(rdn.size(), 2);

    Iterator<AVA> it = rdn.iterator();
    AVA ava1 = it.next();
    AVA ava2 = it.next();

    assertEquals(ava1.getAttributeType(), AT_DC);
    assertEquals(ava1.getAttributeName(), AT_DC.getNameOrOID());
    assertEquals(ava1.getAttributeValue(), AV_DC_ORG);

    assertEquals(ava2.getAttributeType(), AT_CN);
    assertEquals(ava2.getAttributeName(), AT_CN.getNameOrOID());
    assertEquals(ava2.getAttributeValue(), AV_CN);
  }



  /**
   * Test RDN construction with a multiple AVA elements.
   *
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test
  public void testConstructorMultiAVAList() throws Exception {
    RDN rdn = new RDN(Arrays.asList(new AVA(AT_DC, AV_DC_ORG), new AVA(AT_CN, AV_CN)));

    assertEquals(rdn.size(), 2);

    Iterator<AVA> it = rdn.iterator();
    AVA ava1 = it.next();
    AVA ava2 = it.next();

    assertEquals(ava1.getAttributeType(), AT_DC);
    assertEquals(ava1.getAttributeName(), AT_DC.getNameOrOID());
    assertEquals(ava1.getAttributeValue(), AV_DC_ORG);

    assertEquals(ava2.getAttributeType(), AT_CN);
    assertEquals(ava2.getAttributeName(), AT_CN.getNameOrOID());
    assertEquals(ava2.getAttributeValue(), AV_CN);
  }



  /**
   * Test escaping of single space values.
   *
   * @throws Exception  If the test failed unexpectedly.
   */
  @Test
  public void testEscaping() {
    RDN rdn = new RDN(AT_DC, ByteString.valueOfUtf8(" "));
    assertEquals(rdn.toString(), "dc=\\ ");
  }



  /**
   * RDN test data provider.
   *
   * @return The array of test RDN strings.
   */
  @DataProvider(name = "testRDNs")
  public Object[][] createData() {
    return new Object[][] {
        { "dc=hello world", "dc=hello%20world", "dc=hello world" },
        { "dc =hello world", "dc=hello%20world", "dc=hello world" },
        { "dc  =hello world", "dc=hello%20world", "dc=hello world" },
        { "dc= hello world", "dc=hello%20world", "dc=hello world" },
        { "dc=  hello world", "dc=hello%20world", "dc=hello world" },
        { "undefined=hello", "undefined=hello", "undefined=hello" },
        { "DC=HELLO WORLD", "dc=hello%20world", "DC=HELLO WORLD" },
        { "dc = hello    world", "dc=hello%20world", "dc=hello    world" },
        { "   dc = hello world   ", "dc=hello%20world",  "dc=hello world" },
        { "givenName=John+cn=Doe", "cn=doe+givenname=john", "givenName=John+cn=Doe" },
        // FIXME parsing next RDN should fail in the first place
        { "givenName=John\\+cn=Doe", "givenname=john%2Bcn%3Ddoe", "givenName=John\\+cn\\=Doe" },
        { "cn=Doe\\, John", "cn=doe%2C%20john", "cn=Doe\\, John" },
        { "OU=Sales+CN=J. Smith", "cn=j.%20smith+ou=sales","OU=Sales+CN=J. Smith" },
        { "CN=James \\\"Jim\\\" Smith\\, III", "cn=james%20%22jim%22%20smith%2C%20iii",
            "CN=James \\\"Jim\\\" Smith\\, III" },
            //\0d is a hex representation of Carriage return. It is mapped
             //to a SPACE as defined in the MAP ( RFC 4518)
        { "CN=Before\\0dAfter", "cn=before%20after", "CN=Before\\0dAfter" },
        { "1.3.6.1.4.1.1466.0=#04024869",
            //Unicode codepoints from 0000-0008 are mapped to nothing.
            "1.3.6.1.4.1.1466.0=hi", "1.3.6.1.4.1.1466.0=\\04\\02Hi" },
        { "CN=Lu\\C4\\8Di\\C4\\87", "cn=luc%CC%8Cic%CC%81", "CN=Lu\u010di\u0107" },
        { "ou=\\e5\\96\\b6\\e6\\a5\\ad\\e9\\83\\a8", "ou=%E5%96%B6%E6%A5%AD%E9%83%A8", "ou=\u55b6\u696d\u90e8" },
        { "photo=\\ john \\ ", "photo=%20john%20%20", "photo=\\ john \\ " },
     //   { "AB-global=", "ab-global=", "AB-global=" },
        { "cn=John+a=", "a=+cn=john", "cn=John+a=" },
        { "OID.1.3.6.1.4.1.1466.0=#04024869",
            //Unicode codepoints from 0000-0008 are mapped to nothing.
            "1.3.6.1.4.1.1466.0=hi",
            "1.3.6.1.4.1.1466.0=\\04\\02Hi" },
        { "O=\"Sue, Grabbit and Runn\"", "o=sue%2C%20grabbit%20and%20runn", "O=Sue\\, Grabbit and Runn" }, };
  }



  /**
   * Test RDN string decoder.
   *
   * @param rawRDN
   *          Raw RDN string representation.
   * @param normRDN
   *          Normalized RDN string representation.
   * @param stringRDN
   *          String representation.
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test(dataProvider = "testRDNs")
  public void testNormalizationToSafeUrlString(String rawRDN, String normRDN, String stringRDN) throws Exception {
    RDN rdn = RDN.valueOf(rawRDN);
    assertEquals(rdn.toNormalizedUrlSafeString(), normRDN);
  }



  /**
   * Illegal RDN test data provider.
   *
   * @return The array of illegal test RDN strings.
   */
  @DataProvider(name = "illegalRDNs")
  public Object[][] createIllegalData() {
    return new Object[][] { { null }, { "" }, { " " }, { "=" }, { "manager" },
        { "manager " }, { "cn+"}, { "cn+Jim" }, { "cn=Jim+" }, { "cn=Jim +" },
        { "cn=Jim+ " }, /* FIXME activate { "cn=Jim+cn=John" }, */ { "cn=Jim+sn" }, { "cn=Jim+sn " },
        { "cn=Jim+sn equals" }, { "cn=Jim," }, { "cn=Jim;" }, { "cn=Jim,  " },
        { "cn=Jim+sn=a," }, { "cn=Jim, sn=Jam " }, { "cn+uid=Jim" },
        { "-cn=Jim" }, { "/tmp=a" }, { "\\tmp=a" }, { "cn;lang-en=Jim" },
        { "@cn=Jim" }, { "_name_=Jim" }, { "\u03c0=pi" }, { "v1.0=buggy" },
        { "cn=Jim+sn=Bob++" }, { "cn=Jim+sn=Bob+," },
        { "1.3.6.1.4.1.1466..0=#04024869" }, };
  }



  /**
   * Test RDN string decoder against illegal strings.
   *
   * @param rawRDN
   *          Illegal RDN string representation.
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test(dataProvider = "illegalRDNs", expectedExceptions = DirectoryException.class)
  public void testDecodeString(String rawRDN) throws Exception {
    RDN.valueOf(rawRDN);

    fail("Expected exception for value \"" + rawRDN + "\"");
  }

  /**
   * Test getAttributeName.
   *
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test
  public void testGetAttributeName() throws Exception {
    RDN rdn = new RDN(new AVA(AT_DC, AV_DC_ORG), new AVA(AT_CN, AV_CN));
    Iterator<AVA> it = rdn.iterator();
    AVA ava1 = it.next();
    AVA ava2 = it.next();

    assertEquals(ava1.getAttributeName(), AT_DC.getNameOrOID());
    assertEquals(ava2.getAttributeName(), AT_CN.getNameOrOID());
 }

  @SuppressWarnings("javadoc")
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void ensureRDNIsCreatedWithNonEmptyArguments() throws Exception {
      new RDN(new AVA[0]);
  }

  /**
   * Test getAttributeType.
   *
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test
  public void testGetAttributeType() throws Exception {
    RDN rdn = new RDN(new AVA(AT_DC, AV_DC_ORG), new AVA(AT_CN, AV_CN));
    Iterator<AVA> it = rdn.iterator();
    assertEquals(it.next().getAttributeType(), AT_DC);
    assertEquals(it.next().getAttributeType(), AT_CN);
  }

  /**
   * Test getAttributeValue.
   *
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test
  public void testGetAttributeValue() throws Exception {
    RDN rdn = new RDN(new AVA(AT_DC, AV_DC_ORG), new AVA(AT_CN, AV_CN));
    Iterator<AVA> it = rdn.iterator();
    assertEquals(it.next().getAttributeValue(), AV_DC_ORG);
    assertEquals(it.next().getAttributeValue(), AV_CN);
  }

  /**
   * Test getAttributeValue.
   *
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test
  public void testGetAttributeValueByType() throws Exception {
    RDN rdn = new RDN(AT_DC, AV_DC_ORG);

    assertEquals(rdn.getAttributeValue(AT_DC), AV_DC_ORG);
    assertNull(rdn.getAttributeValue(AT_CN));
  }



  /**
   * Test {@link RDN#size()}.
   *
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test
  public void testSize() throws Exception {
    RDN rdn = new RDN(AT_DC, AV_DC_ORG);
    assertEquals(rdn.size(), 1);

    rdn.addValue(AT_CN, AT_CN.getNameOrOID(), AV_CN);
    assertEquals(rdn.size(), 2);
  }

  /**
   * Test isMultiValued.
   *
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test
  public void testIsMultiValued() throws Exception {
    RDN rdn = new RDN(AT_DC, AV_DC_ORG);
    assertEquals(rdn.size(), 1);
    assertFalse(rdn.isMultiValued());

    rdn.addValue(AT_CN, AT_CN.getNameOrOID(), AV_CN);
    assertTrue(rdn.isMultiValued());
  }

  /**
   * Tests addValue with a duplicate value.
   *
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test
  public void testAddDuplicateValue() throws Exception {
    RDN rdn = new RDN(AT_DC, AV_DC_ORG);
    assertFalse(rdn.addValue(AT_DC, AT_DC.getNameOrOID(), AV_DC_ORG));
  }



  /**
   * Test RDN string decoder.
   *
   * @param rawRDN
   *          Raw RDN string representation.
   * @param normRDN
   *          Normalized RDN string representation.
   * @param stringRDN
   *          String representation.
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test(dataProvider = "testRDNs")
  public void testToString(String rawRDN, String normRDN,
      String stringRDN) throws Exception {
    RDN rdn = RDN.valueOf(rawRDN);
    assertEquals(rdn.toString(), stringRDN);
  }

  /**
   * RDN equality test data provider.
   *
   * @return The array of test RDN strings.
   */
  @DataProvider(name = "createRDNEqualityData")
  public Object[][] createRDNEqualityData() {
    return new Object[][] {
        { "cn=hello world", "cn=hello world", 0 },
        { "cn=hello world", "CN=hello world", 0 },
        { "cn=hello   world", "cn=hello world", 0 },
        { "  cn =  hello world  ", "cn=hello world", 0 },
        { "cn=hello world\\ ", "cn=hello world", 0 },
        { "cn=HELLO WORLD", "cn=hello world", 0 },
        { "cn=HELLO+sn=WORLD", "sn=world+cn=hello", 0 },
        { "cn=HELLO+sn=WORLD", "cn=hello+sn=nurse", 1 },
        { "cn=HELLO+sn=WORLD", "cn=howdy+sn=yall", -1 },
        { "cn=hello", "cn=hello+sn=world", -1 },
        { "cn=hello+sn=world", "cn=hello", 1 },
        { "cn=hello+sn=world", "cn=hello+description=world", 1 },
        { "cn=hello", "sn=world", -1 },
        { "sn=hello", "cn=world", 1 },
        { "x-test-integer-type=10", "x-test-integer-type=9", 1 },
        { "x-test-integer-type=999", "x-test-integer-type=1000", -1 },
        { "x-test-integer-type=-1", "x-test-integer-type=0", -1 },
        { "x-test-integer-type=0", "x-test-integer-type=-1", 1 },
        { "cn=aaa", "cn=aaaa", -1 }, { "cn=AAA", "cn=aaaa", -1 },
        { "cn=aaa", "cn=AAAA", -1 }, { "cn=aaaa", "cn=aaa", 1 },
        { "cn=AAAA", "cn=aaa", 1 }, { "cn=aaaa", "cn=AAA", 1 },
        { "cn=aaab", "cn=aaaa", 1 }, { "cn=aaaa", "cn=aaab", -1 }
    };
  }



  /**
   * Test RDN equality
   *
   * @param first
   *          First RDN to compare.
   * @param second
   *          Second RDN to compare.
   * @param result
   *          Expected comparison result.
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test(dataProvider = "createRDNEqualityData")
  public void testEquality(String first, String second, int result)
      throws Exception {
    RDN rdn1 = RDN.valueOf(first);
    RDN rdn2 = RDN.valueOf(second);

    if (result == 0) {
      assertEquals(rdn1, rdn2,
          "RDN equality for <" + first + "> and <" + second + ">");
    } else {
      assertNotEquals(rdn1, rdn2,
          "RDN equality for <" + first + "> and <" + second + ">");
    }
  }



  /**
   * Test RDN hashCode
   *
   * @param first
   *          First RDN to compare.
   * @param second
   *          Second RDN to compare.
   * @param result
   *          Expected comparison result.
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test(dataProvider = "createRDNEqualityData")
  public void testHashCode(String first, String second, int result)
      throws Exception {
    RDN rdn1 = RDN.valueOf(first);
    RDN rdn2 = RDN.valueOf(second);

    int h1 = rdn1.hashCode();
    int h2 = rdn2.hashCode();

    if (result == 0) {
      assertEquals(h1, h2, "Hash codes for <" + first + "> and <" + second
          + "> should be the same.");
    } else {
      assertNotEquals(h1, h2, "Hash codes for <" + first + "> and <" + second
          + "> should be the same.");
    }
  }



  /**
   * Test RDN compareTo
   *
   * @param first
   *          First RDN to compare.
   * @param second
   *          Second RDN to compare.
   * @param result
   *          Expected comparison result.
   * @throws Exception
   *           If the test failed unexpectedly.
   */
  @Test(dataProvider = "createRDNEqualityData")
  public void testCompareTo(String first, String second, int result)
      throws Exception {
    RDN rdn1 = RDN.valueOf(first);
    RDN rdn2 = RDN.valueOf(second);

    int rc = rdn1.compareTo(rdn2);

    // Normalize the result.
    if (rc < 0) {
      rc = -1;
    } else if (rc > 0) {
      rc = 1;
    }

    assertEquals(rc, result, "Comparison for <" + first + "> and <" + second + ">.");
  }



  /**
   * Tests the equals method with a null argument.
   */
  @Test
  public void testEqualityNull() {
    RDN rdn = new RDN(AT_DC, AV_DC_ORG);

    assertNotEquals(rdn, null);
  }



  /**
   * Tests the equals method with a non-RDN argument.
   */
  @Test
  public void testEqualityNonRDN() {
    RDN rdn = new RDN(AT_DC, AV_DC_ORG);

    assertNotEquals(rdn, "this isn't an RDN");
  }
}

