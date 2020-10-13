/*******************************************************************************
 * Copyright (c) 2020 Johannes Kepler University
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Daniel Lindhuber
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.ui.widget;

import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

public interface I4diacTableUtil extends ISelectionProvider {

	TableViewer getViewer();

	void addEntry(Object entry, int index, CompoundCommand cmd);

	Object removeEntry(int index, CompoundCommand cmd);

	void executeCompoundCommand(CompoundCommand cmd);

	@Override
	public default void addSelectionChangedListener(ISelectionChangedListener listener) {

	}

	@Override
	public default ISelection getSelection() {
		return new StructuredSelection(new Object[] {});
	}

	@Override
	public default void removeSelectionChangedListener(ISelectionChangedListener listener) {

	}

	@Override
	public default void setSelection(ISelection selection) {

	}

}
