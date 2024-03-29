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
  Scenario Outline: Verify user should be able to get output schema for a valid Entity name
    When Open Datafusion Project to configure pipeline
    And Select plugin: "SAP SuccessFactors" from the plugins list as: "Source"
    And Navigate to the properties page of plugin: "SAP SuccessFactors"
    And Enter input plugin property: "referenceName" with value: "Referencename"
    And Enter input plugin property: "baseURL" with value: "admin.baseurl" for Credentials and Authorization related fields
    And Enter input plugin property: "entityName" with value: "<EntityName>"
    And Enter input plugin property: "username" with value: "admin.username" for Credentials and Authorization related fields
    And Enter input plugin property: "password" with value: "admin.password" for Credentials and Authorization related fields
    And Validate "SAP SuccessFactors" plugin properties
    Then Verify the Output Schema matches the Expected Schema: "<ExpectedSchema>"
    Examples: 
    | EntityName             | ExpectedSchema                |
    | AdvancesAccumulation   | schema.advancesaccumulation   |
    | Advance                | schema.advance                |
    | AdvancesEligibility    | schema.advanceseligibility    |
    | EmpCompensation        | schema.empcompensation        |
    | Background_Certificates| schema.backgroundcertificates |

  @BATCH-TS-SCFA-DSGN-02
  Scenario Outline: Verify user should be able to validate the plugin with Filter options property
    When Open Datafusion Project to configure pipeline
    And Select plugin: "SAP SuccessFactors" from the plugins list as: "Source"
    And Navigate to the properties page of plugin: "SAP SuccessFactors"
    And Enter input plugin property: "referenceName" with value: "Referencename"
    And Enter input plugin property: "baseURL" with value: "admin.baseurl" for Credentials and Authorization related fields
    And Enter input plugin property: "entityName" with value: "Advance"
    And Enter input plugin property: "username" with value: "admin.username" for Credentials and Authorization related fields
    And Enter input plugin property: "password" with value: "admin.password" for Credentials and Authorization related fields
    And Enter textarea plugin property: "filterOption" with value: "<FilterOption>"
    Then Validate "SAP SuccessFactors" plugin properties
    Examples:
    | FilterOption                     |
    | filteoption.equal                |
    | filteoption.notequal             |
    | filteoption.greaterthan          |
    | filteoption.greaterthanorequal   |
    | filteoption.lessthan             |
    | filteoption.lessthanorequal      |
    | filteoption.and                  |
    | filteoption.or                   |
    | filter.add                       |
    | filter.sub                       |
    | filter.mul                       |
    | filter.div                       |
    | filter.mod                       |
    | filter.grouping                  |

  @BATCH-TS-SCFA-DSGN-03
  Scenario Outline: Verify user should be able to get output schema with Select Fields property
    When Open Datafusion Project to configure pipeline
    And Select plugin: "SAP SuccessFactors" from the plugins list as: "Source"
    And Navigate to the properties page of plugin: "SAP SuccessFactors"
    And Enter input plugin property: "referenceName" with value: "Referencename"
    And Enter input plugin property: "baseURL" with value: "admin.baseurl" for Credentials and Authorization related fields
    And Enter input plugin property: "entityName" with value: "<EntityName>"
    And Enter input plugin property: "username" with value: "admin.username" for Credentials and Authorization related fields
    And Enter input plugin property: "password" with value: "admin.password" for Credentials and Authorization related fields
    And Enter textarea plugin property: "selectOption" with value: "<SelectedFields>"
    And Click on the Validate button
    Then Validate output schema with expectedSchema "<ExpectedSchema>"
    Examples:
      | EntityName      | SelectedFields                | ExpectedSchema        |
      | Advance         | filter.advance.selectedfields | filter.advance.schema |
      | EmpCompensation | filter.empcomp.selectedfields | filter.empcomp.schema |

  @BATCH-TS-SCFA-DSGN-04
  Scenario: Verify user should be able to validate the plugin with Advanced properties
    When Open Datafusion Project to configure pipeline
    And Select plugin: "SAP SuccessFactors" from the plugins list as: "Source"
    And Navigate to the properties page of plugin: "SAP SuccessFactors"
    And Enter input plugin property: "referenceName" with value: "Referencename"
    And Enter input plugin property: "baseURL" with value: "admin.baseurl" for Credentials and Authorization related fields
    And Enter input plugin property: "entityName" with value: "Advance"
    And Enter input plugin property: "username" with value: "admin.username" for Credentials and Authorization related fields
    And Enter input plugin property: "password" with value: "admin.password" for Credentials and Authorization related fields
    And Enter textarea plugin property: "selectOption" with value: "eligibileAmount"
    And Select radio button plugin property: "paginationType" with value: "paginationtype.client"
    Then Validate "SAP SuccessFactors" plugin properties

  @BATCH-TS-SCFA-DSGN-05
  Scenario: Verify user should be able to validate the plugin with Expand Fields Property
    When Open Datafusion Project to configure pipeline
    And Select plugin: "SAP SuccessFactors" from the plugins list as: "Source"
    And Navigate to the properties page of plugin: "SAP SuccessFactors"
    And Enter input plugin property: "referenceName" with value: "referenceName"
    And Enter input plugin property: "baseURL" with value: "admin.baseurl" for Credentials and Authorization related fields
    And Enter input plugin property: "entityName" with value: "User"
    And Enter input plugin property: "username" with value: "admin.username" for Credentials and Authorization related fields
    And Enter input plugin property: "password" with value: "admin.password" for Credentials and Authorization related fields
    And Enter textarea plugin property: "selectOption" with value: "customManager,customReports"
    And Enter input plugin property: "expandOption" with value: "customManager,customReports"
    And Select radio button plugin property: "paginationType" with value: "paginationtype.client"
    And Validate "SAP SuccessFactors" plugin properties
    Then Verify the Output Schema matches the Expected Schema for listed Hierarchical fields:
    | customManager | filter.expand.schema |

  @BATCH-TS-SCFA-DSGN-06
  Scenario: Verify user should be able to validate the plugin with Pagination Type Property
    When Open Datafusion Project to configure pipeline
    And Select plugin: "SAP SuccessFactors" from the plugins list as: "Source"
    And Navigate to the properties page of plugin: "SAP SuccessFactors"
    And Enter input plugin property: "referenceName" with value: "referenceName"
    And Enter input plugin property: "baseURL" with value: "admin.baseurl" for Credentials and Authorization related fields
    And Enter input plugin property: "entityName" with value: "Advance"
    And Enter input plugin property: "username" with value: "admin.username" for Credentials and Authorization related fields
    And Enter input plugin property: "password" with value: "admin.password" for Credentials and Authorization related fields
    And Enter textarea plugin property: "selectOption" with value: "eligibileAmount"
    And Select radio button plugin property: "paginationType" with value: "paginationtype.server"
    Then Validate "SAP SuccessFactors" plugin properties