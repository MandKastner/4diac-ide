/*******************************************************************************
 * Copyright (c) 2022, 2023 Martin Erich Jobst
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   Martin Jobst - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.structuredtextcore.scoping

import com.google.inject.Singleton
import java.lang.reflect.Method
import java.util.List
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.emf.ecore.resource.impl.ResourceImpl
import org.eclipse.fordiac.ide.model.data.DataType
import org.eclipse.fordiac.ide.model.eval.function.Comment
import org.eclipse.fordiac.ide.model.eval.function.Functions
import org.eclipse.fordiac.ide.model.eval.function.OnlySupportedBy
import org.eclipse.fordiac.ide.model.eval.function.ReturnValueComment
import org.eclipse.fordiac.ide.model.eval.function.StandardFunctions
import org.eclipse.fordiac.ide.model.eval.value.ValueOperations
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STCoreFactory
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STStandardFunction
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STVarDeclaration

import static extension org.eclipse.fordiac.ide.model.eval.function.Functions.*
import static extension org.eclipse.fordiac.ide.structuredtextcore.stcore.util.STCoreUtil.*

@Singleton
class STStandardFunctionProvider {
	public static final URI STANDARD_FUNCTIONS_URI = URI.createURI("__st_standard_functions.stfunc")

	static final List<Class<? extends Functions>> DEFAULT_FUNCTIONS = #[StandardFunctions]
	final List<Class<? extends Functions>> functions
	final Resource functionResource

	/**
	 * Create a new instance using the default set of standard functions
	 */
	new() {
		this(DEFAULT_FUNCTIONS)
	}

	/**
	 * Create a new instance using the given set of standard functions
	 */
	new(List<Class<? extends Functions>> functions) {
		this.functions = functions.immutableCopy
		functionResource = new ResourceImpl
		functionResource.URI = STANDARD_FUNCTIONS_URI
		functions.flatMap[Functions.getMethods(it)].forEach[toStandardFunction]
	}

	/**
	 * Get a list of all standard functions known to this provider
	 */
	def Iterable<STStandardFunction> get() {
		return functionResource.contents.filter(STStandardFunction)
	}

	/**
	 * Get a list of all standard functions matching the given argument types
	 */
	def Iterable<STStandardFunction> get(String name, List<DataType> argumentTypes) {
		return (functions.map [
			try {
				findMethodFromDataTypes(name, argumentTypes)
			} catch (NoSuchMethodException | IllegalStateException e) {
				null
			}
		] + functions.flatMap[findMethods(name)]).filterNull.toSet.map[toStandardFunction].toList
	}

	/**
	 * Convert a method to a standard function with concrete return and parameter types
	 */
	def protected create STCoreFactory.eINSTANCE.createSTStandardFunction toStandardFunction(Method method) {
		javaMethod = method
		name = method.name
		comment = method.getAnnotation(Comment)?.value ?: ""
		returnValueComment = method.getAnnotation(ReturnValueComment)?.value ?: ""
		returnType = method.returnType !== void ? ValueOperations.dataType(method.returnType) : null
		inputParameters.addAll(computeInputParameters(emptyList))
		outputParameters.addAll(computeOutputParameters(emptyList))
		varargs = method.varArgs
		onlySupportedBy.addAll(method.getAnnotationsByType(OnlySupportedBy).flatMap[value.toList])
		signature = '''«name»(«(inputParameters.filter(STVarDeclaration).map[type?.name ?: "NULL"] + outputParameters.filter(STVarDeclaration).map['''&«type?.name ?: "NULL"»''']).join(",")»)'''
		functionResource.contents.add(it)
	}
}