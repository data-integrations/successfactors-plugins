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
package io.cdap.plugin.successfactors.actions;

import com.google.cloud.bigquery.TableResult;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.cdap.e2e.utils.BigQueryClient;
import io.cdap.e2e.utils.PluginPropertyUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Successfactors batch source - Properties page - Actions.
 */
public class SuccessfactorsPropertiesPageActions {
  private static final Logger logger = LoggerFactory.getLogger(SuccessfactorsPropertiesPageActions.class);
  private static Gson gson = new Gson();
  private static List<String> bigQueryrows = new ArrayList<>();

  public static void verifyIfRecordCreatedInSinkIsCorrect(String expectedOutputFile)
    throws IOException, InterruptedException {
    List<String> expectedOutput = new ArrayList<>();
    try (BufferedReader bf1 = Files.newBufferedReader(Paths.get(PluginPropertyUtils.pluginProp(expectedOutputFile)))) {
      String line;
      while ((line = bf1.readLine()) != null) {
        expectedOutput.add(line);
      }
    }
    for (int expectedRow = 0; expectedRow < expectedOutput.size(); expectedRow++) {
      JsonObject expectedOutputAsJson = gson.fromJson(expectedOutput.get(expectedRow), JsonObject.class);
      String uniqueId = expectedOutputAsJson.get("usersSysId").getAsString();
      getBigQueryTableData(PluginPropertyUtils.pluginProp("dataset"),
                           PluginPropertyUtils.pluginProp("bqtarget.table"), uniqueId);

    }
    for (int row = 0; row < bigQueryrows.size() && row < expectedOutput.size(); row++) {
      Assert.assertTrue(SuccessfactorsPropertiesPageActions.compareValueOfBothResponses(expectedOutput.get(row),
                                                                                        bigQueryrows.get(row)));
    }
    bigQueryrows.clear();
  }

  static boolean compareValueOfBothResponses(String succesFactorsResponse, String bigQueryResponse) {
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    Map<String, Object> succesFactorsResponseInMap = gson.fromJson(succesFactorsResponse, type);
    Map<String, Object> bigQueryResponseInMap = gson.fromJson(bigQueryResponse, type);
    MapDifference<String, Object> mapDifference = Maps.difference(succesFactorsResponseInMap, bigQueryResponseInMap);
    logger.info("Record of source and sink application is :" + mapDifference);

    return mapDifference.areEqual();
  }

  public static void getBigQueryTableData(String dataset, String table, String uniqueId)
    throws IOException, InterruptedException {
    String projectId = PluginPropertyUtils.pluginProp("projectId");
    String selectQuery = "SELECT TO_JSON(t) FROM `" + projectId + "." + dataset + "." + table + "` AS t WHERE " +
      "usersSysId='" + uniqueId + "' ";
    TableResult result = BigQueryClient.getQueryResult(selectQuery);
    result.iterateAll().forEach(value -> bigQueryrows.add(value.get(0).getValue().toString()));
  }
}
