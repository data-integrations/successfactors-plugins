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
package io.cdap.plugin.successfactors.stepsdesign;

import io.cdap.plugin.successfactors.actions.SuccessfactorsPropertiesPageActions;
import io.cucumber.java.en.Then;

import java.io.IOException;

/**
 * Successfactors batch source - Design Time - Steps.
 */
public class DesignTimeSteps {
  @Then("Validate record created in Sink application is equal to expected output file {string}")
  public void validateRecordCreatedInSinkApplicationIsEqualToExpectedOutputFile(String expectedOutputFile)
    throws IOException, InterruptedException {
    SuccessfactorsPropertiesPageActions.verifyIfRecordCreatedInSinkIsCorrect(expectedOutputFile);
  }
}
