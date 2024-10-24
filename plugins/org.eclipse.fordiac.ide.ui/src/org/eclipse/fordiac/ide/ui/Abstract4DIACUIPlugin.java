/*******************************************************************************
 * Copyright (c) 2014 - 2017 fortiss GmbH, TU Wien ACIN
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Alois Zoitl, Martin Melik Merkumians, Monika Wenger
 *     - initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.ui;

import org.eclipse.ui.plugin.AbstractUIPlugin;

public abstract class Abstract4DIACUIPlugin extends AbstractUIPlugin {

	public String getID() {
		return getBundle().getSymbolicName();
	}

}
