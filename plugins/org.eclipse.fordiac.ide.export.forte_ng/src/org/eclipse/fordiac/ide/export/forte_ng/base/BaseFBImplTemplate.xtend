/*******************************************************************************
 * Copyright (c) 2022 - 2023 Martin Erich Jobst
 * 				 2022 Primetals Technologies Austria GmbH
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   Martin Jobst - initial API and implementation and/or initial documentation
 *   Martin Melik Merkumians - Adjustments for changed FORTE implementation
 *******************************************************************************/
package org.eclipse.fordiac.ide.export.forte_ng.base

import java.nio.file.Path
import java.util.Map
import java.util.Set
import org.eclipse.fordiac.ide.export.forte_ng.ForteFBTemplate
import org.eclipse.fordiac.ide.export.language.ILanguageSupport
import org.eclipse.fordiac.ide.export.language.ILanguageSupportFactory
import org.eclipse.fordiac.ide.model.libraryElement.AdapterFB
import org.eclipse.fordiac.ide.model.libraryElement.Algorithm
import org.eclipse.fordiac.ide.model.libraryElement.BaseFBType
import org.eclipse.fordiac.ide.model.libraryElement.Event
import org.eclipse.fordiac.ide.model.libraryElement.INamedElement
import org.eclipse.fordiac.ide.model.libraryElement.Method

abstract class BaseFBImplTemplate<T extends BaseFBType> extends ForteFBTemplate<T> {
	final Map<Algorithm, ILanguageSupport> algorithmLanguageSupport
	final Map<Method, ILanguageSupport> methodLanguageSupport

	new(T type, String name, Path prefix, String baseClass) {
		super(type, name, prefix, baseClass)
		algorithmLanguageSupport = type.algorithm.toInvertedMap [
			ILanguageSupportFactory.createLanguageSupport("forte_ng", it)
		]
		methodLanguageSupport = type.methods.toInvertedMap [
			ILanguageSupportFactory.createLanguageSupport("forte_ng", it)
		]
	}

	override generate() '''
		«generateHeader»
		
		«generateImplIncludes»
		
		«generateFBDefinition»
		«generateFBInterfaceDefinition»
		«generateFBInterfaceSpecDefinition»
		
		«IF !type.internalVars.isEmpty»
			«type.generateInternalVarDefinition»
			
		«ENDIF»
		«IF !type.internalConstVars.isEmpty»
			«type.internalConstVars.generateVariableDefinitions(true)»
			
		«ENDIF»
		«IF !type.internalFbs.isEmpty»
			«type.generateInternalFbDeclarations»
			
		«ENDIF»
		«FBClassName»::«FBClassName»(CStringDictionary::TStringId pa_nInstanceNameId, CResource *pa_poSrcRes) :
		    «baseClass»(pa_poSrcRes, &scm_stFBInterfaceSpec, pa_nInstanceNameId, «IF !type.internalVars.empty»&scm_stInternalVars«ELSE»nullptr«ENDIF»)«// no newline
			»«(type.internalVars + type.interfaceList.inputVars + type.interfaceList.outputVars).generateVariableInitializer»«generateConnectionInitializer» {
		}
		
		«(type.internalVars + type.interfaceList.inputVars + type.interfaceList.outputVars).generateSetInitialValuesDefinition»
		«IF !type.internalFbs.isEmpty»
			«generateChangeFBExecutionState»
			
		«ENDIF»	
		«generateExecuteEvent»
		
		«generateAlgorithms»
		
		«generateMethods»
		
		«generateInterfaceDefinitions»
		«type.internalVars.generateAccessorDefinition("getVarInternal", false)»
	'''

	def generateChangeFBExecutionState() //
	'''
		EMGMResponse «FBClassName»::changeFBExecutionState(EMGMCommandType paCommand) {
		  return changeFBExecutionStateHelper(paCommand, csmAmountOfInternalFBs, mInternalFBs);
		}
	'''

	def protected generateSendEvent(Event event) {
		if (event.FBNetworkElement instanceof AdapterFB) {
			return '''sendAdapterEvent(scm_n«event.FBNetworkElement.name»AdpNum, FORTE_«event.adapterDeclaration.typeName»::scm_nEvent«event.name»ID, paECET);'''
		}
		'''sendOutputEvent(scm_nEvent«event.name»ID, paECET);'''
	}

	def private getAdapterDeclaration(Event event) {
		(event.FBNetworkElement as AdapterFB).adapterDecl;
	}

	def protected generateAlgorithms() '''
		«FOR algorithm : type.algorithm»
			«algorithm.generateAlgorithm»
		«ENDFOR»
	'''

	def protected generateAlgorithm(Algorithm alg) '''
		void «FBClassName»::«alg.generateAlgorithmName»(void) {
		  «algorithmLanguageSupport.get(alg)?.generate(emptyMap)»
		}
	'''

	def protected generateMethods() '''
		«FOR method : type.methods»
			«methodLanguageSupport.get(method)?.generate(emptyMap)»
		«ENDFOR»
	'''

	def protected generateAlgorithmName(Algorithm alg) '''alg_«alg.name»'''

	def protected abstract CharSequence generateExecuteEvent()

	override protected generateImplIncludes() '''
		«super.generateImplIncludes»
		«getDependencies(emptyMap).generateDependencyIncludes»
	'''

	override getErrors() {
		(super.getErrors + (algorithmLanguageSupport.values + methodLanguageSupport.values).filterNull.flatMap [
			getErrors
		].toSet).toList
	}

	override getWarnings() {
		(super.getWarnings + (algorithmLanguageSupport.values + methodLanguageSupport.values).filterNull.flatMap [
			getWarnings
		].toSet).toList
	}

	override getInfos() {
		(super.getInfos + (algorithmLanguageSupport.values + methodLanguageSupport.values).filterNull.flatMap [
			getInfos
		].toSet).toList
	}

	override Set<INamedElement> getDependencies(Map<?, ?> options) {
		(super.getDependencies(options) +
			(algorithmLanguageSupport.values + methodLanguageSupport.values).filterNull.flatMap [
				getDependencies(options)
			]
		).toSet
	}
}
