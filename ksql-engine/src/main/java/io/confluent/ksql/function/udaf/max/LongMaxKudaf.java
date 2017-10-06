/**
 * Copyright 2017 Confluent Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package io.confluent.ksql.function.udaf.max;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.streams.kstream.Merger;

import java.util.Arrays;

import io.confluent.ksql.function.KsqlAggregateFunction;

public class LongMaxKudaf extends KsqlAggregateFunction<Long, Long> {

  public LongMaxKudaf(Integer argIndexInValue) {
    super(argIndexInValue, Long.MIN_VALUE, Schema.INT64_SCHEMA,
          Arrays.asList(Schema.INT64_SCHEMA),
          "MAX", LongMaxKudaf.class);
  }

  @Override
  public Long aggregate(Long currentVal, Long currentAggVal) {
    if (currentVal > currentAggVal) {
      return currentVal;
    }
    return currentAggVal;
  }

  @Override
  public Merger getMerger() {
    return new Merger<String, Long>() {
      @Override
      public Long apply(final String aggKey, final Long aggOne, final Long aggTwo) {
        if (aggOne > aggTwo) {
          return aggOne;
        }
        return aggTwo;
      }
    };
  }
}