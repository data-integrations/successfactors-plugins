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
Feature: SuccessFactors Source - Design time scenarios

  @BATCH-TS-SCFA-DSGN-01
  Scenario Outline: Verify user should be able to get output schema for the plugin
    When Open Datafusion Project to configure pipeline
    And Select data pipeline type as: "Batch"
    And Select plugin: "SAP SuccessFactors" from the plugins list as: "Source"
    And Navigate to the properties page of plugin: "SAP SuccessFactors"
    And Enter input plugin property: "referenceName" with value: "Referencename"
    And Enter input plugin property: "baseURL" with value: "admin.baseurl"
    And Enter input plugin property: "entityName" with value: "<EntityName>"
    And Enter input plugin property: "username" with value: "admin.username"
    And Enter input plugin property: "password" with value: "admin.password"
    And Validate output schema with expectedSchema "<ExpectedSchema>"
    Examples: 
    | EntityName      | ExpectedSchema        |
    | BadgeTemplates  | schema.badgetemplates |
