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
package org.apache.ambari.infra.job.cleanup;

import java.time.Duration;
import java.time.OffsetDateTime;

import org.apache.ambari.infra.job.InfraJobExecutionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class TaskHistoryWiper implements Tasklet {

  private static final Logger logger = LoggerFactory.getLogger(TaskHistoryWiper.class);
  public static final Duration DEFAULT_TTL = Duration.ofHours(1);

  private final InfraJobExecutionDao infraJobExecutionDao;
  private final Duration ttl;

  public TaskHistoryWiper(InfraJobExecutionDao infraJobExecutionDao, Duration ttl) {
    this.infraJobExecutionDao = infraJobExecutionDao;
    if (ttl == null || ttl.compareTo(DEFAULT_TTL) < 0) {
      logger.info("The ttl value ({}) less than the minimum required. Using the default ({}) instead", ttl, DEFAULT_TTL);
      this.ttl = DEFAULT_TTL;
    }
    else {
      this.ttl = ttl;
    }
  }

  @Override
  public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
    infraJobExecutionDao.deleteJobExecutions(OffsetDateTime.now().minus(ttl));
    return RepeatStatus.FINISHED;
  }
}
