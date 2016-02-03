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
 * Copyright 2006-2008 Sun Microsystems, Inc.
 * Portions Copyright 2014-2016 ForgeRock AS.
 */
package org.opends.server.api;

import org.opends.server.admin.std.server.BackendCfg;
import org.opends.server.config.ConfigEntry;
import org.forgerock.opendj.config.server.ConfigException;
import org.forgerock.opendj.ldap.DN;
import org.opends.server.types.DirectoryException;
import org.opends.server.types.InitializationException;

/**
 * This class defines the set of methods and structures that must be
 * implemented by a Directory Server configuration handler.
 *
 * @param <C> the BackendCfg class in use with this ConfigHandler
 */
@org.opends.server.types.PublicAPI(
     stability=org.opends.server.types.StabilityLevel.VOLATILE,
     mayInstantiate=false,
     mayExtend=true,
     mayInvoke=true)
public abstract class ConfigHandler<C extends BackendCfg> extends Backend<C>
{
  /**
   * Bootstraps this configuration handler using the information in
   * the provided configuration file.  Depending on this configuration
   * handler implementation, the provided file may contain either the
   * entire server configuration or information that is needed to
   * access the configuration in some other location or repository.
   *
   * @param  configFile   The path to the file to use to initialize
   *                      this configuration handler.
   * @param  checkSchema  Indicates whether to perform schema checking
   *                      on the configuration data.
   *
   * @throws  InitializationException  If a problem occurs while
   *                                   attempting to initialize this
   *                                   configuration handler.
   */
  public abstract void initializeConfigHandler(String configFile,
                                               boolean checkSchema)
         throws InitializationException;



  /**
   * Finalizes this configuration handler so that it will release any
   * resources associated with it so that it will no longer be used.
   * This will be called when the Directory Server is shutting down,
   * as well as in the startup process once the schema has been read
   * so that the configuration can be re-read using the updated
   * schema.
   */
  public abstract void finalizeConfigHandler();



  /**
   * Retrieves the entry that is at the root of the Directory Server
   * configuration.
   *
   * @return  The entry that is at the root of the Directory Server
   *          configuration.
   *
   * @throws  ConfigException  If a problem occurs while interacting
   *                           with the configuration.
   */
  public abstract ConfigEntry getConfigRootEntry()
         throws ConfigException;



  /**
   * Retrieves the requested entry from the configuration.
   *
   * @param  entryDN  The distinguished name of the configuration
   *                  entry to retrieve.
   *
   * @return  The requested configuration entry.
   *
   * @throws  ConfigException  If a problem occurs while interacting
   *                           with the configuration.
   */
  public abstract ConfigEntry getConfigEntry(DN entryDN)
         throws ConfigException;



  /**
   * Retrieves the absolute path of the Directory Server install
   * root.
   *
   * @return  The absolute path of the Directory Server install root.
   */
  public abstract String getServerRoot();


  /**
   * Retrieves the absolute path of the Directory Server instance
   * root.
   *
   * @return  The absolute path of the Directory Server instance root.
   */
  public abstract String getInstanceRoot();


  /**
   * Writes an updated version of the Directory Server configuration
   * to the repository.  This should ensure that the stored
   * configuration matches the pending configuration.
   *
   * @throws  DirectoryException  If a problem is encountered while
   *                              writing the updated configuration.
   */
  public abstract void writeUpdatedConfig()
         throws DirectoryException;



  /**
   * Indicates that the Directory Server has started successfully and
   * that the configuration handler should save a copy of the current
   * configuration for use as a "last known good" reference.  Note
   * that this may not be possible with some kinds of configuration
   * repositories, so it should be a best effort attempt.
   * <BR><BR>
   * This method should only be called by the Directory Server itself
   * when the server has started successfully.  It should not be
   * invoked by any other component at any other time.
   */
  @org.opends.server.types.PublicAPI(
       stability=org.opends.server.types.StabilityLevel.VOLATILE,
       mayInstantiate=false,
       mayExtend=true,
       mayInvoke=false)
  public abstract void writeSuccessfulStartupConfig();
}

