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
package org.openCypher.v9_0.ast

import org.openCypher.v9_0.ast.semantics._
import org.openCypher.v9_0.expressions.{Expression, LabelName, LogicalProperty, Variable}
import org.openCypher.v9_0.util.symbols._
import org.openCypher.v9_0.util.{ASTNode, InputPosition}

sealed trait SetItem extends ASTNode with SemanticCheckable

case class SetLabelItem(variable: Variable, labels: Seq[LabelName])(val position: InputPosition) extends SetItem {
  def semanticCheck =
    SemanticExpressionCheck.simple(variable) chain
    SemanticExpressionCheck.expectType(CTNode.covariant, variable)
}

sealed trait SetProperty extends SetItem with SemanticAnalysisTooling

case class SetPropertyItem(property: LogicalProperty, expression: Expression)(val position: InputPosition) extends SetProperty {
  def semanticCheck =
    SemanticExpressionCheck.simple(property) chain
      SemanticExpressionCheck.simple(expression) chain
      expectType(CTNode.covariant | CTRelationship.covariant, property.map)
}

case class SetExactPropertiesFromMapItem(variable: Variable, expression: Expression)
                                        (val position: InputPosition) extends SetProperty {
  def semanticCheck =
    SemanticExpressionCheck.simple(variable) chain
      expectType(CTNode.covariant | CTRelationship.covariant, variable) chain
      SemanticExpressionCheck.simple(expression) chain
      expectType(CTMap.covariant, expression)
}

case class SetIncludingPropertiesFromMapItem(variable: Variable, expression: Expression)
                                        (val position: InputPosition) extends SetProperty {
  def semanticCheck =
    SemanticExpressionCheck.simple(variable) chain
      expectType(CTNode.covariant | CTRelationship.covariant, variable) chain
      SemanticExpressionCheck.simple(expression) chain
      expectType(CTMap.covariant, expression)
}