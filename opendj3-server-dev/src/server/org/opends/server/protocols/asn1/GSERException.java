/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License, Version 1.0 only
 * (the "License").  You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the license at legal-notices/CDDLv1_0.txt
 * or http://forgerock.org/license/CDDLv1.0.html.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at legal-notices/CDDLv1_0.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information:
 *      Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 *
 *      Copyright 2013-2014 Manuel Gaupp
 *      Portions Copyright 2014 ForgeRock AS
 */
package org.opends.server.protocols.asn1;



import org.forgerock.i18n.LocalizableMessage;
import org.opends.server.types.IdentifiedException;



/**
 * This class defines an exception that may be thrown if a problem occurs while
 * interacting with a GSER String.
 */
public final class GSERException
       extends IdentifiedException
{
  /**
   * The serial version identifier required to satisfy the compiler because this
   * class extends <CODE>java.lang.Exception</CODE>, which implements the
   * <CODE>java.io.Serializable</CODE> interface.  This value was generated
   * using the <CODE>serialver</CODE> command-line utility included with the
   * Java SDK.
   */
  private static final long serialVersionUID = 3655637382448481369L;



  /**
   * Creates a new GSER exception with the provided message.
   *
   * @param  message    The message that explains the problem that occurred.
   */
  public GSERException(LocalizableMessage message)
  {
    super(message);
  }



  /**
   * Creates a new GSER exception with the provided message and root
   * cause.
   *
   * @param  message    The message that explains the problem that occurred.
   * @param  cause      The exception that was caught to trigger this exception.
   */
  public GSERException(LocalizableMessage message, Throwable cause)
  {
    super(message, cause);
  }
}

