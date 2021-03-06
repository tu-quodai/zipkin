/*
 * Copyright 2015-2020 The OpenZipkin Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package zipkin2.storage.cassandra.v1;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.ResultSetFuture;
import com.datastax.driver.core.Session;
import java.util.List;
import zipkin2.Call;
import zipkin2.storage.cassandra.internal.call.DistinctSortedStrings;
import zipkin2.storage.cassandra.internal.call.ResultSetFutureCall;

final class SelectServiceNames extends ResultSetFutureCall<ResultSet> {
  static class Factory {
    final Session session;

    Factory(Session session) {
      this.session = session;
    }

    Call<List<String>> create() {
      return new SelectServiceNames(session).flatMap(new DistinctSortedStrings("service_name"));
    }
  }

  final Session session;

  SelectServiceNames(Session session) {
    this.session = session;
  }

  @Override protected ResultSetFuture newFuture() {
    return session.executeAsync("SELECT DISTINCT service_name FROM " + Tables.SERVICE_NAMES);
  }

  @Override public ResultSet map(ResultSet input) {
    return input;
  }

  @Override public String toString() {
    return "SelectServiceNames{}";
  }

  @Override public SelectServiceNames clone() {
    return new SelectServiceNames(session);
  }
}
