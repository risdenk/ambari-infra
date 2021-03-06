/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.ambari.infra.job.deleting;

import static org.apache.ambari.infra.json.StringToDurationConverter.toDuration;

import java.time.Duration;

import org.apache.ambari.infra.job.JobProperties;
import org.apache.ambari.infra.json.DurationToStringConverter;
import org.springframework.batch.core.JobParameters;

public class DocumentDeletingProperties extends JobProperties<DeletingParameters> {
  private String zooKeeperConnectionString;
  private String collection;
  private String filterField;
  private Duration ttl;

  public String getZooKeeperConnectionString() {
    return zooKeeperConnectionString;
  }

  public void setZooKeeperConnectionString(String zooKeeperConnectionString) {
    this.zooKeeperConnectionString = zooKeeperConnectionString;
  }

  public String getCollection() {
    return collection;
  }

  public void setCollection(String collection) {
    this.collection = collection;
  }

  public String getFilterField() {
    return filterField;
  }

  public void setFilterField(String filterField) {
    this.filterField = filterField;
  }

  public Duration getTtl() {
    return ttl;
  }

  public void setTtl(Duration ttl) {
    this.ttl = ttl;
  }

  @Override
  public DeletingParameters merge(JobParameters jobParameters) {
    DeletingParameters deletingParameters = new DeletingParameters();
    deletingParameters.setZooKeeperConnectionString(jobParameters.getString("zooKeeperConnectionString", zooKeeperConnectionString));
    deletingParameters.setCollection(jobParameters.getString("collection", collection));
    deletingParameters.setFilterField(jobParameters.getString("filterField", filterField));
    deletingParameters.setStart(jobParameters.getString("start", "*"));
    deletingParameters.setEnd(jobParameters.getString("end", "*"));
    deletingParameters.setTtl(toDuration(jobParameters.getString("ttl", DurationToStringConverter.toString(ttl))));
    return deletingParameters;
  }
}
