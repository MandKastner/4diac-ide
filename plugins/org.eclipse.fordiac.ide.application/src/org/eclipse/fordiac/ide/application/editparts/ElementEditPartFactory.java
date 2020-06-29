/*******************************************************************************
 * Copyright (c) 2008 - 2017 Profactor GmbH, fortiss GmbH
 * 				 2019 - 2020 Johannes Kepler University
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Gerhard Ebenhofer, Alois Zoitl, Gerd Kainz, Monika Wenger
 *     - initial API and implementation and/or initial documentation
 *   Alois Zoitl - separated FBNetworkElement from instance name for better
 *                 direct editing of instance names
 *   Bianca Wiesmayr - added struct
 *******************************************************************************/
package org.eclipse.fordiac.ide.application.editparts;

import org.eclipse.fordiac.ide.gef.editparts.Abstract4diacEditPartFactory;
import org.eclipse.fordiac.ide.gef.editparts.ValueEditPart;
import org.eclipse.fordiac.ide.model.data.StructuredType;
import org.eclipse.fordiac.ide.model.libraryElement.Connection;
import org.eclipse.fordiac.ide.model.libraryElement.FB;
import org.eclipse.fordiac.ide.model.libraryElement.FBNetwork;
import org.eclipse.fordiac.ide.model.libraryElement.IInterfaceElement;
import org.eclipse.fordiac.ide.model.libraryElement.StructManipulator;
import org.eclipse.fordiac.ide.model.libraryElement.SubApp;
import org.eclipse.fordiac.ide.model.libraryElement.Value;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.parts.GraphicalEditor;

/**
 * A factory for creating new EditParts.
 */
public class ElementEditPartFactory extends Abstract4diacEditPartFactory {

	public ElementEditPartFactory(GraphicalEditor editor) {
		super(editor);
	}

	/**
	 * Maps an object to an EditPart.
	 *
	 * @throws RuntimeException if no match was found (programming error)
	 */
	@Override
	protected EditPart getPartForElement(final EditPart context, final Object modelElement) {
		EditPart part = null;

		if (modelElement instanceof FBNetwork) {
			if (((FBNetwork) modelElement).eContainer() instanceof SubApp) {
				part = new UISubAppNetworkEditPart();
			} else {
				part = new FBNetworkEditPart();
			}
		} else if (modelElement instanceof FB) {
			if ((null != ((FB) modelElement).getType())
					&& ((((FB) modelElement).getType().getName().contentEquals("STRUCT_MUX"))
							|| (((FB) modelElement).getType().getName().contentEquals("STRUCT_DEMUX")))) {
				part = new StructManipulatorEditPart();
			} else {
				part = new FBEditPart();
			}
		} else if (modelElement instanceof InstanceName) {
			part = new InstanceNameEditPart();
		} else if (modelElement instanceof Connection) {
			part = new ConnectionEditPart();
		} else if (modelElement instanceof SubApp) {
			part = new SubAppForFBNetworkEditPart();
		} else if (modelElement instanceof IInterfaceElement) {
			part = createInterfaceEditPart(modelElement);
		} else if (modelElement instanceof Value) {
			part = new ValueEditPart();
		} else {
			throw createEditpartCreationException(modelElement);
		}
		return part;
	}

	private EditPart createInterfaceEditPart(final Object modelElement) {
		EditPart part;
		IInterfaceElement element = (IInterfaceElement) modelElement;
		if (element.getFBNetworkElement() instanceof StructManipulator &&
				element.getType() instanceof StructuredType) {
			return new StructInterfaceEditPart();
		}
		if ((element.getFBNetworkElement() instanceof SubApp)
				&& (null == element.getFBNetworkElement().getType())) {
			part = new UntypedSubAppInterfaceElementEditPart();
		} else {
			part = new InterfaceEditPartForFBNetwork();
		}
		return part;
	}

}
