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
 * Portions Copyright 2015 ForgeRock AS.
 */

package org.opends.guitools.controlpanel.event;

/**
 * Interface that must be implemented by the objects that want to receive
 * notifications when the authentication to be used to handle referrals in the
 * LDAP entries browser.
 * For the time being it is not used but it can be used in the future when some
 * UI to specify authentication for referrals is implemented.
 *
 */
public interface ReferralAuthenticationListener {
  /**
   * Notification that some authentication changed.
   *
   */
  void notifyAuthDataChanged();
}
