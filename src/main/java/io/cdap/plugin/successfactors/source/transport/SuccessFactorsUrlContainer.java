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

package io.cdap.plugin.successfactors.source.transport;

import io.cdap.plugin.successfactors.common.util.ResourceConstants;
import io.cdap.plugin.successfactors.common.util.SuccessFactorsUtil;
import io.cdap.plugin.successfactors.source.config.SuccessFactorsPluginConfig;
import okhttp3.HttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * This {@code SuccessFactorsUrlContainer} contains the implementation of different SuccessFactors url:
 * * Test url
 * * Metadata url
 * * Available record count url
 * * Data url
 */
public class SuccessFactorsUrlContainer {

  private static final Logger LOG = LoggerFactory.getLogger(SuccessFactorsUrlContainer.class);
  private static final String TOP_OPTION = "$top";
  private static final String METADATA = "$metadata";
  private final SuccessFactorsPluginConfig pluginConfig;

  public SuccessFactorsUrlContainer(SuccessFactorsPluginConfig pluginConfig) {
    this.pluginConfig = pluginConfig;
  }

  /**
   * Construct tester URL.
   *
   * @return tester URL.
   */
  public URL getTesterURL() {
    HttpUrl.Builder builder = HttpUrl.parse(pluginConfig.getBaseURL())
      .newBuilder()
      .addPathSegment(pluginConfig.getEntityName());

    URL testerURL = buildQueryOptions(builder)
      .addQueryParameter(TOP_OPTION, "1")
      .build()
      .url();

    LOG.debug(ResourceConstants.DEBUG_TEST_ENDPOINT.getMsgForKey(testerURL));

    return testerURL;
  }

  /**
   * Constructs metadata URL.
   *
   * @return metadata URL.
   */
  public URL getMetadataURL() {
    URL metadataURL = HttpUrl.parse(pluginConfig.getBaseURL())
      .newBuilder()
      .addPathSegments(pluginConfig.getEntityName())
      .addPathSegment(METADATA)
      .build()
      .url();

    LOG.debug(ResourceConstants.DEBUG_METADATA_ENDPOINT.getMsgForKey(metadataURL));

    return metadataURL;
  }

  /**
   * Adds Query option parameters in {@code HttpUrl.Builder} as per the given sequence.
   * Sequence:
   * 1. $filter
   * 2. $select
   * 3. $expand
   *
   * @param urlBuilder
   * @return initialize the passed {@code HttpUrl.Builder} with the provided query options
   * in {@code SuccessFactorsPluginConfig} and return it.
   */
  private HttpUrl.Builder buildQueryOptions(HttpUrl.Builder urlBuilder) {
    if (SuccessFactorsUtil.isNotNullOrEmpty(pluginConfig.getFilterOption())) {
      urlBuilder.addQueryParameter("$filter", pluginConfig.getFilterOption());
    }
    if (SuccessFactorsUtil.isNotNullOrEmpty(pluginConfig.getSelectOption())) {
      urlBuilder.addQueryParameter("$select", pluginConfig.getSelectOption());
    }
    if (SuccessFactorsUtil.isNotNullOrEmpty(pluginConfig.getExpandOption())) {
      urlBuilder.addQueryParameter("$expand", pluginConfig.getExpandOption());
    }

    return urlBuilder;
  }
}
