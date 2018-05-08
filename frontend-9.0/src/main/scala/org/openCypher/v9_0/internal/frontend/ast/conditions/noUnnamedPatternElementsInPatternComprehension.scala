/*
 * Copyright (c) 2002-2018 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
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
package org.openCypher.v9_0.internal.frontend.ast.conditions

import org.openCypher.v9_0.internal.expressions.{PatternComprehension, PatternElement, RelationshipsPattern}
import org.openCypher.v9_0.internal.frontend.helpers.rewriting.Condition
import org.openCypher.v9_0.internal.util.Foldable._

case object noUnnamedPatternElementsInPatternComprehension extends Condition {

  override def name: String = productPrefix

  override def apply(that: Any): Seq[String] = that.treeFold(Seq.empty[String]) {
    case expr: PatternComprehension if containsUnNamedPatternElement(expr.pattern) =>
      acc => (acc :+ s"Expression $expr contains pattern elements which are not named", None)
  }

  private def containsUnNamedPatternElement(expr: RelationshipsPattern) = expr.treeExists {
    case p: PatternElement => p.variable.isEmpty
  }
}
