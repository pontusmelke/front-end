/*
 * Copyright © 2002-2018 Neo4j Sweden AB (http://neo4j.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opencypher.v9_0.rewriting

import org.opencypher.v9_0.rewriting.rewriters.simplifyPredicates
import org.opencypher.v9_0.util.test_helpers.CypherFunSuite

class SimplifyPredicatesTest extends CypherFunSuite with PredicateTestSupport {

  val rewriter = simplifyPredicates

  test("double negation is removed") {
    not(not(P)) <=> P
  }

  test("double negation is removed by keeping an extra not") {
    not(not(not(P))) <=> not(P)
  }

  test("repeated double negation is removed") {
    not(not(not(not(P)))) <=> P
  }
}
