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
package io.cdap.plugin.successfactors.source;

import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.annotation.Plugin;
import io.cdap.cdap.api.data.format.StructuredRecord;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.cdap.etl.api.FailureCollector;
import io.cdap.cdap.etl.api.PipelineConfigurer;
import io.cdap.cdap.etl.api.batch.BatchSource;
import io.cdap.cdap.etl.api.batch.BatchSourceContext;
import io.cdap.plugin.successfactors.common.exception.SuccessFactorsServiceException;
import io.cdap.plugin.successfactors.common.exception.TransportException;
import io.cdap.plugin.successfactors.common.util.ExceptionParser;
import io.cdap.plugin.successfactors.common.util.ResourceConstants;
import io.cdap.plugin.successfactors.source.config.SuccessFactorsPluginConfig;
import io.cdap.plugin.successfactors.source.service.SuccessFactorsService;
import io.cdap.plugin.successfactors.source.transport.SuccessFactorsTransporter;
import org.apache.hadoop.io.LongWritable;

import java.net.HttpURLConnection;
import javax.annotation.Nullable;

/**
 * Plugin returns records from SuccessFactors using entity name provided by user.
 * Reads data in batches, every batch is processed as a separate split by mapreduce.
 */
@Plugin(type = BatchSource.PLUGIN_TYPE)
@Name(SuccessFactorsSource.NAME)
@Description("Reads the SuccessFactors data which is exposed as OData services from SAP.")
public class SuccessFactorsSource extends BatchSource<LongWritable, StructuredRecord, StructuredRecord> {
  public static final String NAME = "SuccessFactors";
  private final SuccessFactorsPluginConfig config;

  public SuccessFactorsSource(SuccessFactorsPluginConfig config) {
    this.config = config;
  }

  @Override
  public void configurePipeline(PipelineConfigurer pipelineConfigurer) {
    FailureCollector failureCollector = pipelineConfigurer.getStageConfigurer().getFailureCollector();
    config.validatePluginParameters(failureCollector);

    if (config.isSchemaBuildRequired()) {
      pipelineConfigurer.getStageConfigurer().setOutputSchema(getOutputSchema(failureCollector));
    } else {
      pipelineConfigurer.getStageConfigurer().setOutputSchema(null);
    }
  }

  @Override
  public void prepareRun(BatchSourceContext context) throws Exception {

  }

  /**
   * Gets the appropriate Schema based on the provided plugin parameters and also
   * sets the appropriate error messages in case any error is identified while preparing the Schema.
   *
   * @param failureCollector {@code FailureCollector}
   * @return {@code Schema}
   */
  @Nullable
  private Schema getOutputSchema(FailureCollector failureCollector) {
    SuccessFactorsTransporter transporter = new SuccessFactorsTransporter(config.getUsername(), config.getPassword());
    SuccessFactorsService successFactorsServices = new SuccessFactorsService(config, transporter);
    try {
      //validate if the given parameters form a valid SuccessFactors URL.
      successFactorsServices.checkSuccessFactorsURL();
      return successFactorsServices.buildOutputSchema();
    } catch (TransportException te) {
      String errorMsg = ExceptionParser.buildTransportError(te);
      errorMsg = ResourceConstants.ERR_ODATA_SERVICE_CALL.getMsgForKeyWithCode(errorMsg);
      failureCollector.addFailure(errorMsg, null).withConfigProperty(SuccessFactorsPluginConfig.BASE_URL);
    } catch (SuccessFactorsServiceException ose) {
      attachFieldWithError(ose, failureCollector);
    }
    failureCollector.getOrThrowException();
    return null;
  }

  /**
   * Checks and attaches the UI fields with its relevant error message.
   *
   * @param ose              {@code SuccessFactorsServiceException}
   * @param failureCollector {@code FailureCollector}
   */
  private void attachFieldWithError(SuccessFactorsServiceException ose, FailureCollector failureCollector) {

    String errMsg = ExceptionParser.buildSuccessFactorsServiceError(ose);
    errMsg = ResourceConstants.ERR_ODATA_ENTITY_FAILURE.getMsgForKeyWithCode(errMsg);
    switch (ose.getErrorCode()) {
      case HttpURLConnection.HTTP_UNAUTHORIZED:
        failureCollector.addFailure(errMsg, null).withConfigProperty(SuccessFactorsPluginConfig.UNAME);
        failureCollector.addFailure(errMsg, null).withConfigProperty(SuccessFactorsPluginConfig.PASSWORD);
        break;
      case HttpURLConnection.HTTP_FORBIDDEN:
      case ExceptionParser.NO_VERSION_FOUND:
      case ExceptionParser.INVALID_VERSION_FOUND:
        failureCollector.addFailure(errMsg, null).withConfigProperty(SuccessFactorsPluginConfig.ENTITY_NAME);
        break;
      case HttpURLConnection.HTTP_NOT_FOUND:
        failureCollector.addFailure(errMsg, ResourceConstants.ERR_NOT_FOUND.getMsgForKey());
        break;
      case HttpURLConnection.HTTP_BAD_REQUEST:
        failureCollector.addFailure(errMsg, ResourceConstants.ERR_CHECK_ADVANCED_PARAM.getMsgForKey());
        break;

      default:
        failureCollector.addFailure(errMsg, null);
    }
  }
}
