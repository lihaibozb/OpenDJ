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
 *      Copyright 2013-2014 ForgeRock AS
 */
package org.opends.server.tools.upgrade;

import com.forgerock.opendj.cli.ClientException;

/**
 * Abstract upgrade task implementation.
 */
abstract class AbstractUpgradeTask implements UpgradeTask
{
  /**
   * Creates a new abstract upgrade task.
   */
  AbstractUpgradeTask()
  {
    // No implementation required.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void interact(UpgradeContext context)
      throws ClientException
  {
    // Nothing to do.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void perform(UpgradeContext context) throws ClientException
  {
    // Must be implemented.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void verify(UpgradeContext context)
      throws ClientException
  {
    // Nothing to do.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void postUpgrade(UpgradeContext context)
      throws ClientException
  {
    // Nothing to do.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void postponePostUpgrade(UpgradeContext context)
      throws ClientException
  {
    // Nothing to do.
  }
}