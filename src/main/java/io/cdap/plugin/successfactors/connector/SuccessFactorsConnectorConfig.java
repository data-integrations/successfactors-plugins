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
package io.cdap.plugin.successfactors.connector;

import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Macro;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.plugin.PluginConfig;
import io.cdap.cdap.etl.api.FailureCollector;
import io.cdap.plugin.successfactors.common.exception.TransportException;
import io.cdap.plugin.successfactors.common.util.ResourceConstants;
import io.cdap.plugin.successfactors.common.util.SuccessFactorsUtil;
import io.cdap.plugin.successfactors.source.transport.SuccessFactorsResponseContainer;
import io.cdap.plugin.successfactors.source.transport.SuccessFactorsTransporter;
import okhttp3.HttpUrl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.annotation.Nullable;
import javax.ws.rs.core.MediaType;

/**
 * SuccessFactorsConnectorConfig Class
 */
public class SuccessFactorsConnectorConfig extends PluginConfig {
  public static final String PROPERTY_AUTH_TYPE = "authType";
  public static final String ASSERTION_TOKEN_TYPE = "assertionTokenType";
  public static final String BASIC_AUTH = "basicAuth";
  public static final String OAUTH2 = "oAuth2";
  public static final String ENTER_TOKEN = "enterToken";
  public static final String CREATE_TOKEN = "createToken";
  public static final String BASE_URL = "baseURL";
  public static final String UNAME = "username";
  public static final String PASSWORD = "password";
  public static final String TOKEN_URL = "tokenURL";
  public static final String CLIENT_ID = "clientId";
  public static final String PRIVATE_KEY = "privateKey";
  public static final String USER_ID = "userId";
  public static final String ASSERTION_TOKEN = "assertionToken";
  public static final String COMPANY_ID = "companyId";
  public static final String TEST = "TEST";
  private static final String COMMON_ACTION = ResourceConstants.ERR_MISSING_PARAM_OR_MACRO_ACTION.getMsgForKey();
  private static final String SAP_SUCCESSFACTORS_USERNAME = "SAP SuccessFactors Username";
  private static final String SAP_SUCCESSFACTORS_PASSWORD = "SAP SuccessFactors Password";
  private static final String SAP_SUCCESSFACTORS_BASE_URL = "SAP SuccessFactors Base URL";
  private static final Logger LOG = LoggerFactory.getLogger(SuccessFactorsConnectorConfig.class);

  @Nullable
  @Name(PROPERTY_AUTH_TYPE)
  @Description("Type of authentication used to submit request. OAuth 2.0, Basic Authentication types are available.")
  protected String authType;

  @Nullable
  @Name(ASSERTION_TOKEN_TYPE)
  @Description("Assertion token can be entered or can be created using the required parameters.")
  protected String assertionTokenType;

  @Nullable
  @Name(UNAME)
  @Macro
  @Description("SAP SuccessFactors Username for user authentication.")
  private final String username;

  @Nullable
  @Name(PASSWORD)
  @Macro
  @Description("SAP SuccessFactors password for user authentication.")
  private final String password;

  @Nullable
  @Name(TOKEN_URL)
  @Macro
  @Description("Token URL to generate the assertion token.")
  private final String tokenURL;

  @Nullable
  @Name(CLIENT_ID)
  @Macro
  @Description("Client Id to generate the token.")
  private final String clientId;

  @Nullable
  @Name(PRIVATE_KEY)
  @Macro
  @Description("Private key to generate the token.")
  private final String privateKey;

  @Nullable
  @Name(USER_ID)
  @Macro
  @Description("User Id to generate the token.")
  private final String userId;

  @Nullable
  @Name(ASSERTION_TOKEN)
  @Macro
  @Description("Assertion token used to generate the access token.")
  private final String assertionToken;

  @Nullable
  @Name(COMPANY_ID)
  @Macro
  @Description("Company Id to generate the token.")
  private final String companyId;

  @Macro
  @Name(BASE_URL)
  @Description("SuccessFactors Base URL.")
  private final String baseURL;

  public SuccessFactorsConnectorConfig(@Nullable String username, @Nullable String password, @Nullable String tokenURL,
                                       @Nullable String clientId, @Nullable String privateKey, @Nullable String userId,
                                       @Nullable String companyId, String baseURL, String authType,
                                       String assertionTokenType,
                                       @Nullable String samlUsername, @Nullable String assertionToken) {
    this.username = username;
    this.password = password;
    this.tokenURL = tokenURL;
    this.clientId = clientId;
    this.privateKey = privateKey;
    this.userId = userId;
    this.companyId = companyId;
    this.baseURL = baseURL;
    this.authType = authType;
    this.assertionTokenType = assertionTokenType;
    this.assertionToken = assertionToken;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getAuthType() {
    return authType;
  }

  @Nullable
  public String getTokenURL() {
    return tokenURL;
  }

  @Nullable
  public String getCompanyId() {
    return companyId;
  }

  @Nullable
  public String getAssertionTokenType() {
    return assertionTokenType;
  }

  @Nullable
  public String getClientId() {
    return clientId;
  }

  @Nullable
  public String getAssertionToken() {
    return assertionToken;
  }

  @Nullable
  public String getPrivateKey() {
    return privateKey;
  }

  @Nullable
  public String getUserId() {
    return userId;
  }

  public String getBaseURL() {
    return baseURL;
  }

  public void validateBasicCredentials(FailureCollector failureCollector) {

    if (SuccessFactorsUtil.isNullOrEmpty(getAuthType())) {
      return;
    }

    if (authType.equals(BASIC_AUTH)) {
      if (SuccessFactorsUtil.isNullOrEmpty(getUsername()) && !containsMacro(UNAME)) {
        String errMsg = ResourceConstants.ERR_MISSING_PARAM_PREFIX.getMsgForKey(SAP_SUCCESSFACTORS_USERNAME);
        failureCollector.addFailure(errMsg, COMMON_ACTION).withConfigProperty(UNAME);
      }
      if (SuccessFactorsUtil.isNullOrEmpty(getPassword()) && !containsMacro(PASSWORD)) {
        String errMsg = ResourceConstants.ERR_MISSING_PARAM_PREFIX.getMsgForKey(SAP_SUCCESSFACTORS_PASSWORD);
        failureCollector.addFailure(errMsg, COMMON_ACTION).withConfigProperty(PASSWORD);
      }

    }
    if (authType.equals(OAUTH2)) {
      if (SuccessFactorsUtil.isNullOrEmpty(getClientId()) && !containsMacro(CLIENT_ID)) {
        String errMsg = ResourceConstants.ERR_MISSING_PARAM_PREFIX.getMsgForKey(CLIENT_ID);
        failureCollector.addFailure(errMsg, COMMON_ACTION).withConfigProperty(CLIENT_ID);
      }
      if (SuccessFactorsUtil.isNullOrEmpty(getCompanyId()) && !containsMacro(COMPANY_ID)) {
        String errMsg = ResourceConstants.ERR_MISSING_PARAM_PREFIX.getMsgForKey(COMPANY_ID);
        failureCollector.addFailure(errMsg, COMMON_ACTION).withConfigProperty(COMPANY_ID);
      }

      if (assertionTokenType.equals(ENTER_TOKEN)) {
        if (SuccessFactorsUtil.isNullOrEmpty(getAssertionToken()) && !containsMacro(ASSERTION_TOKEN)) {
          String errMsg = ResourceConstants.ERR_MISSING_PARAM_PREFIX.getMsgForKey(ASSERTION_TOKEN);
          failureCollector.addFailure(errMsg, COMMON_ACTION).withConfigProperty(ASSERTION_TOKEN);
        }
      }

      if (assertionTokenType.equals(CREATE_TOKEN)) {
        if (SuccessFactorsUtil.isNullOrEmpty(getTokenURL()) && !containsMacro(TOKEN_URL)) {
          String errMsg = ResourceConstants.ERR_MISSING_PARAM_PREFIX.getMsgForKey(TOKEN_URL);
          failureCollector.addFailure(errMsg, COMMON_ACTION).withConfigProperty(TOKEN_URL);
        }
        if (SuccessFactorsUtil.isNullOrEmpty(getPrivateKey()) && !containsMacro(PRIVATE_KEY)) {
          String errMsg = ResourceConstants.ERR_MISSING_PARAM_PREFIX.getMsgForKey(PRIVATE_KEY);
          failureCollector.addFailure(errMsg, COMMON_ACTION).withConfigProperty(PRIVATE_KEY);
        }
        if ((SuccessFactorsUtil.isNullOrEmpty(getUserId()) && !containsMacro(USER_ID))) {
          String errMsg = ResourceConstants.ERR_MISSING_PARAM_PREFIX.getMsgForKey(USER_ID);
          failureCollector.addFailure(errMsg, COMMON_ACTION).withConfigProperty(USER_ID);
        }
      }

    }

    if (SuccessFactorsUtil.isNullOrEmpty(getBaseURL()) && !containsMacro(BASE_URL)) {
      String errMsg = ResourceConstants.ERR_MISSING_PARAM_PREFIX.getMsgForKey(SAP_SUCCESSFACTORS_BASE_URL);
      failureCollector.addFailure(errMsg, COMMON_ACTION).withConfigProperty(BASE_URL);
    }
    if (SuccessFactorsUtil.isNotNullOrEmpty(getBaseURL()) && !containsMacro(BASE_URL)) {
      if (HttpUrl.parse(getBaseURL()) == null) {
        String errMsg = ResourceConstants.ERR_INVALID_BASE_URL.getMsgForKey(SAP_SUCCESSFACTORS_BASE_URL);
        failureCollector.addFailure(errMsg, COMMON_ACTION).withConfigProperty(BASE_URL);
      }
    }
  }

  /**
   * Method to validate the credential fields.
   */
  public void validateConnection(FailureCollector collector) {
    SuccessFactorsTransporter successFactorsHttpClient = new SuccessFactorsTransporter(this);
    URL testerURL = HttpUrl.parse(getBaseURL()).newBuilder().build().url();
    SuccessFactorsResponseContainer responseContainer = null;
    try {
      responseContainer =
        successFactorsHttpClient.callSuccessFactorsEntity(testerURL, MediaType.APPLICATION_JSON, TEST);
    } catch (TransportException e) {
      LOG.error("Unable to fetch the response", e);
      collector.addFailure("Unable to call SuccessFatorsEntity", "Please check the values");
      return;
    }
    if (responseContainer.getHttpStatusCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
      String errMsg = ResourceConstants.ERR_INVALID_CREDENTIAL.getMsgForKey();
      collector.addFailure(errMsg, COMMON_ACTION);
    } else if (responseContainer.getHttpStatusCode() == HttpURLConnection.HTTP_NOT_FOUND) {
      String errMsg = ResourceConstants.ERR_INVALID_BASE_URL.getMsgForKey();
      collector.addFailure(errMsg, COMMON_ACTION);
    }
  }
}
