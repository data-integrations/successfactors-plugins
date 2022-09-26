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

  @BATCH-TS-SCFA-DSGN-RNTM-MACRO-01 @BQ_SINK @FILE_PATH @BQ_SINK_CLEANUP
  Scenario: Verify user should be able to preview and deploy the pipeline when plugin is configured for Basic properties with macros
    When Open Datafusion Project to configure pipeline
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
    And Enter input plugin property: "referenceName" with value: "Reference"
    And Replace input plugin property: "project" with value: "projectId"
    And Enter input plugin property: "datasetProject" with value: "datasetprojectId"
    And Enter input plugin property: "dataset" with value: "dataset"
    And Enter input plugin property: "table" with value: "bqtarget.table"
    And Validate "BigQuery" plugin properties
    And Close the Plugin Properties page
    And Connect source as "SAP-SuccessFactors" and sink as "BigQueryTable" to establish connection
    And Preview and run the pipeline
    And Enter runtime argument value from environment variable "admin.baseurl" for key "baseURL"
    And Enter runtime argument value "valid.entityname" for key "entityName"
    And Enter runtime argument value from environment variable "admin.username" for key "username"
    And Enter runtime argument value from environment variable "admin.password" for key "password"
    And Run the preview of pipeline with runtime arguments
    And Open and capture pipeline preview logs
    And Verify the preview run status of pipeline in the logs is "succeeded"
    And Close the pipeline logs
    And Close the preview
    And Save and Deploy Pipeline
    And Run the Pipeline in Runtime
    And Enter runtime argument value from environment variable "admin.baseurl" for key "baseURL"
    And Enter runtime argument value "valid.entityname" for key "entityName"
    And Enter runtime argument value from environment variable "admin.username" for key "username"
    And Enter runtime argument value from environment variable "admin.password" for key "password"
    And Run the Pipeline in Runtime with runtime arguments
    And Wait till pipeline is in running state
    And Open and capture logs
    And Verify the pipeline status is "Succeeded"
    And Close the pipeline logs
    Then Validate record created in Sink application is equal to expected output file "TestOutputFile"

  @BATCH-TS-SCFA-DSGN-RNTM-MACRO-02 @BQ_SINK @FILE_PATH @BQ_SINK_CLEANUP @BQ_SINK_CLEANUP
  Scenario: Verify user should be able to preview and deploy the pipeline when plugin is configured for Advanced properties with macros
    When Open Datafusion Project to configure pipeline
    And Select plugin: "SAP SuccessFactors" from the plugins list as: "Source"
    And Navigate to the properties page of plugin: "SAP SuccessFactors"
    And Enter input plugin property: "referenceName" with value: "Referencename"
    And Enter input plugin property: "baseURL" with value: "admin.baseurl" for Credentials and Authorization related fields
    And Enter input plugin property: "entityName" with value: "EmpCostDistribution"
    And Enter input plugin property: "username" with value: "admin.username" for Credentials and Authorization related fields
    And Enter input plugin property: "password" with value: "admin.password" for Credentials and Authorization related fields
    And Click on the Macro button of Property: "paginationType" and set the value to: "paginationType"
    And Enter textarea plugin property: "filterOption" with value: "filteoption.rntm.equal"
    And Validate "SAP SuccessFactors" plugin properties
    And Close the Plugin Properties page
    And Select Sink plugin: "BigQueryTable" from the plugins list
    And Navigate to the properties page of plugin: "BigQuery"
    And Enter input plugin property: "referenceName" with value: "Reference"
    And Replace input plugin property: "project" with value: "projectId"
    And Enter input plugin property: "datasetProject" with value: "datasetprojectId"
    And Enter input plugin property: "dataset" with value: "dataset"
    And Enter input plugin property: "table" with value: "bqtarget.table"
    And Validate "BigQuery" plugin properties
    And Close the Plugin Properties page
    And Connect source as "SAP-SuccessFactors" and sink as "BigQueryTable" to establish connection
    And Preview and run the pipeline
    And Enter runtime argument value "paginationtype.client" for key "paginationType"
    And Run the preview of pipeline with runtime arguments
    And Open and capture pipeline preview logs
    And Verify the preview run status of pipeline in the logs is "succeeded"
    And Close the pipeline logs
    And Close the preview
    And Save and Deploy Pipeline
    And Run the Pipeline in Runtime
    And Enter runtime argument value "paginationtype.client" for key "paginationType"
    And Run the Pipeline in Runtime with runtime arguments
    And Wait till pipeline is in running state
    And Open and capture logs
    And Verify the pipeline status is "Succeeded"
    And Close the pipeline logs
    Then Validate record created in Sink application is equal to expected output file "TestOutputFileWithFilter"

  @BATCH-TS-SCFA-RNTM-MACRO-03 @BQ_SINK @BQ_SINK_CLEANUP
  Scenario: Verify pipeline failure message in logs when user provides invalid Entity Name with Macros
    When Open Datafusion Project to configure pipeline
    And Select plugin: "SAP SuccessFactors" from the plugins list as: "Source"
    And Navigate to the properties page of plugin: "SAP SuccessFactors"
    And Enter input plugin property: "referenceName" with value: "Referencename"
    And Enter input plugin property: "baseURL" with value: "admin.baseurl" for Credentials and Authorization related fields
    And Enter input plugin property: "username" with value: "admin.username" for Credentials and Authorization related fields
    And Enter input plugin property: "password" with value: "admin.password" for Credentials and Authorization related fields
    And Click on the Macro button of Property: "entityName" and set the value to: "entityName"
    And Validate "SAP SuccessFactors" plugin properties
    And Close the Plugin Properties page
    And Select Sink plugin: "BigQueryTable" from the plugins list
    And Navigate to the properties page of plugin: "BigQuery"
    And Enter input plugin property: "referenceName" with value: "Reference"
    And Replace input plugin property: "project" with value: "projectId"
    And Enter input plugin property: "datasetProject" with value: "datasetprojectId"
    And Enter input plugin property: "dataset" with value: "dataset"
    And Enter input plugin property: "table" with value: "bqtarget.table"
    Then Validate "BigQuery" plugin properties
    And Close the Plugin Properties page
    And Connect source as "SAP-SuccessFactors" and sink as "BigQueryTable" to establish connection
    And Save and Deploy Pipeline
    And Run the Pipeline in Runtime
    And Enter runtime argument value "invalid.entityname" for key "entityName"
    And Run the Pipeline in Runtime with runtime arguments
    And Wait till pipeline is in running state
    And Verify the pipeline status is "Failed"
    Then Open Pipeline logs and verify Log entries having below listed Level and Message:
      | Level | Message                                   |
      | ERROR | invalid.entityname.logsmessage            |

  @BATCH-TS-SCFA-RNTM-MACRO-04 @BQ_SINK @BQ_SINK_CLEANUP
  Scenario:Verify pipeline failure message in logs when user provides invalid Credentials with Macros
    When Open Datafusion Project to configure pipeline
    And Select plugin: "SAP SuccessFactors" from the plugins list as: "Source"
    And Navigate to the properties page of plugin: "SAP SuccessFactors"
    And Enter input plugin property: "referenceName" with value: "Referencename"
    And Enter input plugin property: "baseURL" with value: "admin.baseurl" for Credentials and Authorization related fields
    And Enter input plugin property: "entityName" with value: "AdvancesAccumulation"
    And Click on the Macro button of Property: "username" and set the value to: "username"
    And Click on the Macro button of Property: "password" and set the value to: "password"
    And Validate "SAP SuccessFactors" plugin properties
    And Close the Plugin Properties page
    And Select Sink plugin: "BigQueryTable" from the plugins list
    And Navigate to the properties page of plugin: "BigQuery"
    And Enter input plugin property: "referenceName" with value: "Reference"
    And Replace input plugin property: "project" with value: "projectId"
    And Enter input plugin property: "datasetProject" with value: "datasetprojectId"
    And Enter input plugin property: "dataset" with value: "dataset"
    And Enter input plugin property: "table" with value: "bqtarget.table"
    Then Validate "BigQuery" plugin properties
    And Close the Plugin Properties page
    And Connect source as "SAP-SuccessFactors" and sink as "BigQueryTable" to establish connection
    And Save and Deploy Pipeline
    And Run the Pipeline in Runtime
    And Enter runtime argument value "invalid.username" for key "username"
    And Enter runtime argument value "invalid.password" for key "password"
    And Run the Pipeline in Runtime with runtime arguments
    And Wait till pipeline is in running state
    And Verify the pipeline status is "Failed"
    Then Open Pipeline logs and verify Log entries having below listed Level and Message:
      | Level | Message                                   |
      | ERROR | invalid.credentials.logsmessage           |

  @BATCH-TS-SCFA-RNTM-MACRO-05 @BQ_SINK @BQ_SINK_CLEANUP
  Scenario: Verify pipeline failure message in logs when user provides invalid Expand Field properties with Macros
    When Open Datafusion Project to configure pipeline
    And Select plugin: "SAP SuccessFactors" from the plugins list as: "Source"
    And Navigate to the properties page of plugin: "SAP SuccessFactors"
    And Enter input plugin property: "referenceName" with value: "Referencename"
    And Enter input plugin property: "baseURL" with value: "admin.baseurl" for Credentials and Authorization related fields
    And Enter input plugin property: "username" with value: "admin.username" for Credentials and Authorization related fields
    And Enter input plugin property: "password" with value: "admin.password" for Credentials and Authorization related fields
    And Click on the Macro button of Property: "entityName" and set the value to: "entityName"
    And Click on the Macro button of Property: "expandOption" and set the value to: "expandOption"
    And Click on the Macro button of Property: "associatedEntityName" and set the value to: "associatedEntityName"
    And Validate "SAP SuccessFactors" plugin properties
    And Close the Plugin Properties page
    And Select Sink plugin: "BigQueryTable" from the plugins list
    And Navigate to the properties page of plugin: "BigQuery"
    And Enter input plugin property: "referenceName" with value: "Reference"
    And Replace input plugin property: "project" with value: "projectId"
    And Enter input plugin property: "datasetProject" with value: "datasetprojectId"
    And Enter input plugin property: "dataset" with value: "dataset"
    And Enter input plugin property: "table" with value: "bqtarget.table"
    Then Validate "BigQuery" plugin properties
    And Close the Plugin Properties page
    And Connect source as "SAP-SuccessFactors" and sink as "BigQueryTable" to establish connection
    And Save and Deploy Pipeline
    And Run the Pipeline in Runtime
    And Enter runtime argument value "hierarchical.valid.entityname" for key "entityName"
    And Enter runtime argument value "hierarchical.invalid.expandfield" for key "expandOption"
    And Enter runtime argument value "hierarchical.invalid.associatedentityname" for key "associatedEntityName"
    And Run the Pipeline in Runtime with runtime arguments
    And Wait till pipeline is in running state
    And Verify the pipeline status is "Failed"
    Then Open Pipeline logs and verify Log entries having below listed Level and Message:
      | Level | Message                                   |
      | ERROR | invalid.expandfield.logsmessage           |