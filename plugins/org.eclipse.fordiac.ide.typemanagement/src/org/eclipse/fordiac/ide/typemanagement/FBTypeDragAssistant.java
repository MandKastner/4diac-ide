/*******************************************************************************
 * Copyright (c) 2012 - 2017 Profactor GmbH, TU Wien ACIN, fortiss GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Gerhard Ebenhofer, Alois Zoitl
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.typemanagement;

import org.eclipse.core.resources.IFile;
import org.eclipse.fordiac.ide.model.typelibrary.TypeEntry;
import org.eclipse.fordiac.ide.model.typelibrary.TypeLibraryManager;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.navigator.CommonDragAdapterAssistant;

public class FBTypeDragAssistant extends CommonDragAdapterAssistant {

	@Override
	public Transfer[] getSupportedTransferTypes() {
		return new Transfer[] { TemplateTransfer.getInstance() };
	}

	@Override
	public void dragStart(final DragSourceEvent anEvent, final IStructuredSelection aSelection) {
		if (aSelection.getFirstElement() instanceof IFile) {
			final TypeEntry entry = TypeLibraryManager.INSTANCE
					.getTypeEntryForFile((IFile) aSelection.getFirstElement());
			if (entry != null) {
				TemplateTransfer.getInstance().setTemplate(entry);
			} else {
				anEvent.doit = false;
			}
		}
		super.dragStart(anEvent, aSelection);
	}

	@Override
	public boolean setDragData(final DragSourceEvent anEvent, final IStructuredSelection aSelection) {
		if (aSelection.getFirstElement() instanceof IFile) {
			final TypeEntry entry = TypeLibraryManager.INSTANCE
					.getTypeEntryForFile((IFile) aSelection.getFirstElement());
			if (null != entry) {
				anEvent.data = entry;
				return true;
			}
		}

		return false;
	}

}
