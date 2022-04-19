# Copyright © 2022 Cask Data, Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License"); you may not
# use this file except in compliance with the License. You may obtain a copy of
# the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
# License for the specific language governing permissions and limitations under
# the License.


@SuccessFactorsSource
@Smoke
@Regression
Feature: SuccessFactors Source - Run time scenarios (macros)

  @BATCH-TS-SCFA-DSGN-RNTM-MACRO-01 @BQ_SINK_TEST
  Scenario: Verify user should be able to preview the pipeline when plugin is configured for Basic properties with macros
    When Open Datafusion Project to configure pipeline
    And Select data pipeline type as: "Batch"
    And Select plugin: "SAP SuccessFactors" from the plugins list as: "Source"
    And Navigate to the properties page of plugin: "SAP SuccessFactors"
    And Enter input plugin property: "referenceName" with value: "Referencename"
    And Click on the Macro button of Property: "baseURL" and set the value to: "baseURL"
    And Click on the Macro button of Property: "entityName" and set the value to: "entityName"
    And Click on the Macro button of Property: "username" and set the value to: "username"
    And Click on the Macro button of Property: "password" and set the value to: "password"
    And Click on the Validate button
    And Close the Plugin Properties page
    And Select Sink plugin: "BigQueryTable" from the plugins list
    And Navigate to the properties page of plugin: "BigQuery"
    And Configure BigQuery sink plugin for Dataset and Table
    And Validate "BigQuery" plugin properties
    And Close the Plugin Properties page
    And Connect source as "SAP-SuccessFactors" and sink as "BigQueryTable" to establish connection
    And Save the pipeline
    And Preview and run the pipeline
    And Enter runtime argument value "admin.baseurl" for key "baseURL"
    And Enter runtime argument value "valid.entityname" for key "entityName"
    And Enter runtime argument value "admin.username" for key "username"
    And Enter runtime argument value "admin.password" for key "password"
    And Run the preview of pipeline with runtime arguments
    And Verify the preview of pipeline is "successfully"

  @BATCH-TS-SCFA-RNTM-MACRO-02 @BQ_SINK_TEST
  Scenario: Verify user should be able to run and deploy the pipeline when plugin is configured for Basic properties with macros
    When Open Datafusion Project to configure pipeline
    And Select data pipeline type as: "Batch"
    And Select plugin: "SAP SuccessFactors" from the plugins list as: "Source"
    And Navigate to the properties page of plugin: "SAP SuccessFactors"
    And Enter input plugin property: "referenceName" with value: "Referencename"
    And Click on the Macro button of Property: "baseURL" and set the value to: "baseURL"
    And Click on the Macro button of Property: "entityName" and set the value to: "entityName"
    And Click on the Macro button of Property: "username" and set the value to: "username"
    And Click on the Macro button of Property: "password" and set the value to: "password"
    And Validate "SAP SuccessFactors" plugin properties
    And Close the Plugin Properties page
    And Select Sink plugin: "BigQueryTable" from the plugins list
    And Navigate to the properties page of plugin: "BigQuery"
    And Configure BigQuery sink plugin for Dataset and Table
    Then Validate "BigQuery" plugin properties
    And Close the Plugin Properties page
    And Connect source as "SAP-SuccessFactors" and sink as "BigQueryTable" to establish connection
    And Save and Deploy Pipeline
    And Run the Pipeline in Runtime
    And Enter runtime argument value "admin.baseurl" for key "baseURL"
    And Enter runtime argument value "valid.entityname" for key "entityName"
    And Enter runtime argument value "admin.username" for key "username"
    And Enter runtime argument value "admin.password" for key "password"
    And Run the Pipeline in Runtime with runtime arguments
    And Wait till pipeline is in running state
    And Verify the pipeline status is "Succeeded"
    Then Verify count of no of records transferred to the target BigQuery Table

  @BATCH-TS-SCFA-DSGN-RNTM-MACRO-03 @BQ_SINK_TEST
  Scenario: Verify user should be able to preview the pipeline when plugin is configured for Advanced properties with macros
    When Open Datafusion Project to configure pipeline
    And Select data pipeline type as: "Batch"
    And Select plugin: "SAP SuccessFactors" from the plugins list as: "Source"
    And Navigate to the properties page of plugin: "SAP SuccessFactors"
    And Enter input plugin property: "referenceName" with value: "Referencename"
    And Enter input plugin property: "baseURL" with value: "admin.baseurl"
    And Enter input plugin property: "entityName" with value: "Advance"
    And Enter input plugin property: "username" with value: "admin.username"
    And Enter input plugin property: "password" with value: "admin.password"
    And Click on the Macro button of Property: "filterOption" and set the value in textarea: "filterOption"
    And Click on the Macro button of Property: "selectOption" and set the value in textarea: "selectOption"
    And Click on the Macro button of Property: "splitCount" and set the value to: "splitCount"
    And Click on the Macro button of Property: "batchSize" and set the value to: "batchSize"
    And Validate "SAP SuccessFactors" plugin properties
    And Close the Plugin Properties page
    And Select Sink plugin: "BigQueryTable" from the plugins list
    And Navigate to the properties page of plugin: "BigQuery"
    And Configure BigQuery sink plugin for Dataset and Table
    And Validate "BigQuery" plugin properties
    And Close the Plugin Properties page
    And Connect source as "SAP-SuccessFactors" and sink as "BigQueryTable" to establish connection
    And Save the pipeline
    And Preview and run the pipeline
    And Enter runtime argument value "filteoption.greaterthan" for key "filterOption"
    And Enter runtime argument value "filter.rntm.selectedfields" for key "selectOption"
    And Enter runtime argument value "2" for key "splitCount"
    And Enter runtime argument value "1000" for key "batchSize"
    And Run the preview of pipeline with runtime arguments
    And Verify the preview of pipeline is "successfully"

  @BATCH-TS-SCFA-RNTM-MACRO-04 @BQ_SINK_TEST
  Scenario: Verify user should be able to run and deploy the pipeline when plugin is configured for Advanced properties with macros
    When Open Datafusion Project to configure pipeline
    And Select data pipeline type as: "Batch"
    And Select plugin: "SAP SuccessFactors" from the plugins list as: "Source"
    And Navigate to the properties page of plugin: "SAP SuccessFactors"
    And Enter input plugin property: "referenceName" with value: "Referencename"
    And Enter input plugin property: "baseURL" with value: "admin.baseurl"
    And Enter input plugin property: "entityName" with value: "Advance"
    And Enter input plugin property: "username" with value: "admin.username"
    And Enter input plugin property: "password" with value: "admin.password"
    And Click on the Macro button of Property: "filterOption" and set the value in textarea: "filterOption"
    And Click on the Macro button of Property: "selectOption" and set the value in textarea: "selectOption"
    And Click on the Macro button of Property: "splitCount" and set the value to: "splitCount"
    And Click on the Macro button of Property: "batchSize" and set the value to: "batchSize"
    And Validate "SAP SuccessFactors" plugin properties
    And Close the Plugin Properties page
    And Select Sink plugin: "BigQueryTable" from the plugins list
    And Navigate to the properties page of plugin: "BigQuery"
    And Configure BigQuery sink plugin for Dataset and Table
    Then Validate "BigQuery" plugin properties
    And Close the Plugin Properties page
    And Connect source as "SAP-SuccessFactors" and sink as "BigQueryTable" to establish connection
    And Save and Deploy Pipeline
    And Run the Pipeline in Runtime
    And Enter runtime argument value "filteoption.greaterthan" for key "filterOption"
    And Enter runtime argument value "filter.rntm.selectedfields" for key "selectOption"
    And Enter runtime argument value "2" for key "splitCount"
    And Enter runtime argument value "1000" for key "batchSize"
    And Run the Pipeline in Runtime with runtime arguments
    And Wait till pipeline is in running state
    And Verify the pipeline status is "Succeeded"
    Then Verify count of no of records transferred to the target BigQuery Table


