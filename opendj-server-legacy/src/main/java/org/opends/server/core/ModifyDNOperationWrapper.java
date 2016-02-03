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
 * Portions Copyright 2013-2016 ForgeRock AS.
 */
package org.opends.server.core;

import java.util.List;

import org.opends.server.types.*;
import org.forgerock.opendj.ldap.ByteString;
import org.forgerock.opendj.ldap.DN;
import org.forgerock.opendj.ldap.RDN;

/**
 * This abstract class wraps/decorates a given moddn operation.
 * This class will be extended by sub-classes to enhance the
 * functionality of the ModifyDNOperationBasis.
 */
public abstract class ModifyDNOperationWrapper extends
    OperationWrapper<ModifyDNOperation> implements ModifyDNOperation
{

  /**
   * Creates a new moddn operation based on the provided moddn operation.
   *
   * @param modifyDN The moddn operation to wrap
   */
  public ModifyDNOperationWrapper(ModifyDNOperation modifyDN)
  {
    super(modifyDN);
  }

  /** {@inheritDoc} */
  @Override
  public void addModification(Modification modification) {
    getOperation().addModification(modification);
  }

  /** {@inheritDoc} */
  @Override
  public boolean deleteOldRDN() {
    return getOperation().deleteOldRDN();
  }

  /** {@inheritDoc} */
  @Override
  public DN getEntryDN() {
    return getOperation().getEntryDN();
  }

  /** {@inheritDoc} */
  @Override
  public List<Modification> getModifications() {
    return getOperation().getModifications();
  }

  /** {@inheritDoc} */
  @Override
  public RDN getNewRDN() {
    return getOperation().getNewRDN();
  }

  /** {@inheritDoc} */
  @Override
  public DN getNewSuperior() {
    return getOperation().getNewSuperior();
  }

  /** {@inheritDoc} */
  @Override
  public Entry getOriginalEntry() {
    return getOperation().getOriginalEntry();
  }

  /** {@inheritDoc} */
  @Override
  public ByteString getRawEntryDN() {
    return getOperation().getRawEntryDN();
  }

  /** {@inheritDoc} */
  @Override
  public ByteString getRawNewRDN() {
    return getOperation().getRawNewRDN();
  }

  /** {@inheritDoc} */
  @Override
  public ByteString getRawNewSuperior() {
    return getOperation().getRawNewSuperior();
  }

  /** {@inheritDoc} */
  @Override
  public Entry getUpdatedEntry() {
    return getOperation().getUpdatedEntry();
  }

  /** {@inheritDoc} */
  @Override
  public void setDeleteOldRDN(boolean deleteOldRDN) {
    getOperation().setDeleteOldRDN(deleteOldRDN);
  }

  /** {@inheritDoc} */
  @Override
  public void setRawEntryDN(ByteString rawEntryDN) {
    getOperation().setRawEntryDN(rawEntryDN);
  }

  /** {@inheritDoc} */
  @Override
  public void setRawNewRDN(ByteString rawNewRDN) {
    getOperation().setRawNewRDN(rawNewRDN);
  }

  /** {@inheritDoc} */
  @Override
  public void setRawNewSuperior(ByteString rawNewSuperior) {
    getOperation().setRawNewSuperior(rawNewSuperior);
  }

  /** {@inheritDoc} */
  @Override
  public DN getNewDN()
  {
    return getOperation().getNewDN();
  }

}
