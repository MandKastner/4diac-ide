/*******************************************************************************
 * Copyright (c) 2008, 2024 Profactor GmbH, TU Wien ACIN, fortiss GmbH,
 * 							Primetals Technologies Austria GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Gerhard Ebenhofer, Alois Zoitl, Ingo Hegny, Monika Wenger, Martin Jobst
 *      - initial API and implementation and/or initial documentation
 *    Alois Zoitl  - turned the Palette model into POJOs
 ******************************************************************************/
package org.eclipse.fordiac.ide.model.typelibrary.impl;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.fordiac.ide.model.dataexport.AbstractTypeExporter;
import org.eclipse.fordiac.ide.model.dataimport.CommonElementImporter;
import org.eclipse.fordiac.ide.model.dataimport.SEGImporter;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElement;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage;
import org.eclipse.fordiac.ide.model.libraryElement.SegmentType;
import org.eclipse.fordiac.ide.model.typelibrary.SegmentTypeEntry;

public class SegmentTypeEntryImpl extends AbstractCheckedTypeEntryImpl<SegmentType> implements SegmentTypeEntry {

	public SegmentTypeEntryImpl() {
		super(SegmentType.class);
	}

	@Override
	public void save(final LibraryElement toSave, final IProgressMonitor monitor) throws CoreException {
		// currently we can not save segments, but we also have no editor for it
	}

	@Override
	protected CommonElementImporter getImporter() {
		return new SEGImporter(getFile());
	}

	@Override
	protected AbstractTypeExporter getTypeExporter(final SegmentType type) {
		// currently we can not save segments, but we also have no editor for it
		return null;
	}

	@Override
	public EClass getTypeEClass() {
		return LibraryElementPackage.Literals.SEGMENT_TYPE;
	}
}
