/*
 * Copyright © 2022 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.plugin.successfactors.source.service;

import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.plugin.successfactors.common.exception.SuccessFactorsServiceException;
import io.cdap.plugin.successfactors.common.exception.TransportException;
import io.cdap.plugin.successfactors.common.util.ExceptionParser;
import io.cdap.plugin.successfactors.common.util.ResourceConstants;
import io.cdap.plugin.successfactors.common.util.SuccessFactorsUtil;
import io.cdap.plugin.successfactors.source.config.SuccessFactorsPluginConfig;
import io.cdap.plugin.successfactors.source.metadata.SuccessFactorsEntityProvider;
import io.cdap.plugin.successfactors.source.metadata.SuccessFactorsSchemaGenerator;
import io.cdap.plugin.successfactors.source.transport.SuccessFactorsResponseContainer;
import io.cdap.plugin.successfactors.source.transport.SuccessFactorsTransporter;
import io.cdap.plugin.successfactors.source.transport.SuccessFactorsUrlContainer;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderException;

import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.core.MediaType;

/**
 * This {@code SuccessFactorsService} contains all the SAP SuccessFactors relevant service call implementations
 * - check the correctness of the formed SuccessFactors URL
 * - builds the Output Schema
 * - fetch total number of available record count
 * - builds the base64 encoded SAP SuccessFactors entity metadata string
 */
public class SuccessFactorsService {

  public static final String TEST = "TEST";
  public static final String METADATA = "METADATA";
  public static final String DATA = "DATA";
  private final SuccessFactorsPluginConfig pluginConfig;
  private final SuccessFactorsTransporter successFactorsHttpClient;
  private final SuccessFactorsUrlContainer urlContainer;

  public SuccessFactorsService(SuccessFactorsPluginConfig pluginConfig,
                               SuccessFactorsTransporter successFactorsHttpClient) {
    this.pluginConfig = pluginConfig;
    this.successFactorsHttpClient = successFactorsHttpClient;
    urlContainer = new SuccessFactorsUrlContainer(pluginConfig);
  }

  /**
   * Calls to check the Successfactors URL correctness.
   *
   * @throws TransportException             any http client exceptions are wrapped under it.
   * @throws SuccessFactorsServiceException any SuccessFactors service based exception is wrapped under it.
   */
  public void checkSuccessFactorsURL() throws TransportException, SuccessFactorsServiceException {

    SuccessFactorsResponseContainer responseContainer =
      successFactorsHttpClient.callSuccessFactorsEntity(urlContainer.getTesterURL(), MediaType.APPLICATION_JSON, TEST);

    ExceptionParser.checkAndThrowException(ResourceConstants.ERR_FAILED_ENTITY_VALIDATION.getMsgForKey(),
                                           responseContainer);
  }

  /**
   * Prepares output schema based on the provided plugin config parameters.
   * e.g.
   * - builds schema with given selective properties
   * - builds schema with default and given expanded navigation properties
   * - builds schema with non-navigation default properties
   * <p>
   * For more detail please refer {@code SuccessFactorsSchemaGenerator}
   *
   * @return {@code Schema}
   * @throws TransportException             any http client exceptions are wrapped under it.
   * @throws SuccessFactorsServiceException any SuccessFactors service based exception is wrapped under it.
   */
  public Schema buildOutputSchema() throws SuccessFactorsServiceException, TransportException {

    SuccessFactorsEntityProvider edmData = fetchServiceMetadata(callEntityMetadata());
    SuccessFactorsSchemaGenerator successFactorsSchemaGenerator = new SuccessFactorsSchemaGenerator(edmData);

    if (SuccessFactorsUtil.isNotNullOrEmpty(pluginConfig.getSelectOption())) {
      return successFactorsSchemaGenerator.buildSelectOutputSchema(pluginConfig.getEntityName(), pluginConfig.
        getSelectOption());
    } else if (SuccessFactorsUtil.isNotNullOrEmpty(pluginConfig.getExpandOption())) {
      return successFactorsSchemaGenerator.buildExpandOutputSchema(pluginConfig.getEntityName(), pluginConfig.
        getExpandOption());
    } else {
      return successFactorsSchemaGenerator.buildDefaultOutputSchema(pluginConfig.getEntityName());
    }
  }

  /**
   * Calls the SAP SuccessFactors Service and returns the {@code Edm} instance.
   *
   * @return {@code SuccessFactorsEntityProvider}
   * @throws SuccessFactorsServiceException any SuccessFactors service based exception is wrapped under it.
   */
  private SuccessFactorsEntityProvider fetchServiceMetadata(InputStream metadataStream)
    throws SuccessFactorsServiceException {
    try (InputStream stream = metadataStream) {
      Edm metadata = EntityProvider.readMetadata(stream, false);
      return new SuccessFactorsEntityProvider(metadata);
    } catch (EntityProviderException | IOException e) {
      String errMsg = ResourceConstants.ERR_READING_METADATA.getMsgForKey(pluginConfig.getEntityName());
      throw new SuccessFactorsServiceException(errMsg, e);
    }
  }

  /**
   * Calls the SAP SuccessFactors catalog entity to fetch Entity metadata
   *
   * @return {@code InputStream}
   * @throws TransportException any http client exceptions are wrapped under it.
   */
  private InputStream callEntityMetadata() throws TransportException {
    SuccessFactorsResponseContainer responseContainer = successFactorsHttpClient
      .callSuccessFactorsEntity(urlContainer.getMetadataURL(), MediaType.APPLICATION_XML, METADATA);
    return responseContainer.getResponseStream();
  }
}
