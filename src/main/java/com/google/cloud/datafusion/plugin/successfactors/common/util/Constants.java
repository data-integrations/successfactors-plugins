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
package com.google.cloud.datafusion.plugin.successfactors.common.util;

/**
 * Constants Class
 */
public final class Constants {

  private Constants() {
  }

  /**
   * Reference Class
   */
  public static class Reference {
    public static final String REFERENCE_NAME = "referenceName";
    public static final String REFERENCE_NAME_DESCRIPTION = "This will be used to uniquely identify this source/sink " +
      "for lineage, annotating metadata, etc.";
  }
}
