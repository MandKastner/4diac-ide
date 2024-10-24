/*******************************************************************************
 * Copyright (c) 2023 Martin Erich Jobst
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
package org.eclipse.fordiac.ide.structuredtextalgorithm.ui.editor.embedded;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fordiac.ide.model.libraryElement.INamedElement;
import org.eclipse.fordiac.ide.model.libraryElement.ITypedElement;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElement;
import org.eclipse.fordiac.ide.structuredtextalgorithm.resource.STAlgorithmResource;
import org.eclipse.fordiac.ide.structuredtextcore.stcore.STResource;
import org.eclipse.fordiac.ide.structuredtextcore.stcore.util.STCoreUtil;
import org.eclipse.xtext.ParserRule;

public class STAlgorithmInitialValueEditedResourceProvider extends STAlgorithmEditedResourceProvider {
	private final ITypedElement element;

	public STAlgorithmInitialValueEditedResourceProvider(final ITypedElement element) {
		super(EcoreUtil.getRootContainer(element) instanceof final LibraryElement libraryElement ? libraryElement
				: null);
		this.element = element;
	}

	@Override
	public STAlgorithmResource createResource() {
		final STAlgorithmResource resource = super.createResource();
		final INamedElement featureType = STCoreUtil.getFeatureType(element);
		resource.getDefaultLoadOptions().put(STResource.OPTION_EXPECTED_TYPE, featureType);
		return resource;
	}

	@Override
	protected ParserRule getEntryPoint() {
		return getParser().getGrammarAccess().getSTInitializerExpressionSourceRule();
	}
}
