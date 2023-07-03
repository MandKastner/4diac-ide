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
package org.eclipse.fordiac.ide.structuredtextfunctioneditor.ui.document;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.fordiac.ide.model.libraryElement.ICallable;
import org.eclipse.fordiac.ide.structuredtextcore.ui.document.STCoreDocumentPartitioner;
import org.eclipse.fordiac.ide.structuredtextfunctioneditor.stfunction.STFunctionSource;
import org.eclipse.fordiac.ide.structuredtextfunctioneditor.util.STFunctionPartitioner;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.model.XtextDocument;

public class STFunctionDocumentPartitioner extends STFunctionPartitioner implements STCoreDocumentPartitioner {

	@Override
	public EList<ICallable> partition(final XtextDocument document) {
		try {
			return document.readOnly(resource -> {
				if (resource.getModificationStamp() != document.getModificationStamp()) {
					resource.reparse(document.get());
				}
				return partition(resource);
			});
		} catch (final Exception e) {
			return emergencyPartition(document); // try to salvage what we can
		}
	}

	@SuppressWarnings("static-method") // overridable
	protected EList<ICallable> emergencyPartition(final XtextDocument document) {
		return ECollections.newBasicEList(newLostAndFound(document.get(), 0));
	}

	@Override
	protected EList<ICallable> emergencyPartition(final XtextResource resource) {
		throw new UnsupportedOperationException(); // always get it from the document instead
	}

	@Override
	protected EList<ICallable> emergencyPartition(final STFunctionSource source) {
		throw new UnsupportedOperationException(); // always get it from the document instead
	}
}