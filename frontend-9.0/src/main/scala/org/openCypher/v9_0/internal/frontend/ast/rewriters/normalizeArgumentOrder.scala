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
package org.openCypher.v9_0.internal.frontend.ast.rewriters

import org.openCypher.v9_0.internal.expressions.{Equals, FunctionInvocation, InequalityExpression, Property}
import org.openCypher.v9_0.internal.util.{Rewriter, topDown}
import org.openCypher.v9_0.internal.expressions.functions

case object normalizeArgumentOrder extends Rewriter {

  override def apply(that: AnyRef): AnyRef = instance(that)

  private val instance: Rewriter = topDown(Rewriter.lift {

    // move id(n) on equals to the left
    case predicate @ Equals(func@FunctionInvocation(_, _, _, _), _) if func.function == functions.Id =>
      predicate

    case predicate @ Equals(lhs, rhs @ FunctionInvocation(_, _, _, _)) if rhs.function == functions.Id =>
      predicate.copy(lhs = rhs, rhs = lhs)(predicate.position)

    // move n.prop on equals to the left
    case predicate @ Equals(Property(_, _), _) =>
      predicate

    case predicate @ Equals(lhs, rhs @ Property(_, _)) =>
      predicate.copy(lhs = rhs, rhs = lhs)(predicate.position)

    case inequality: InequalityExpression =>
      val lhsIsProperty = inequality.lhs.isInstanceOf[Property]
      val rhsIsProperty = inequality.rhs.isInstanceOf[Property]
      if (!lhsIsProperty && rhsIsProperty) {
        inequality.swapped
      } else {
        inequality
      }
  })
}
