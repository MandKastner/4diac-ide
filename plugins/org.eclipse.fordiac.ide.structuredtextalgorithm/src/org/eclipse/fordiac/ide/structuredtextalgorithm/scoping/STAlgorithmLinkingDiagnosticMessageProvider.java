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
package org.eclipse.fordiac.ide.structuredtextalgorithm.scoping;

import org.eclipse.fordiac.ide.structuredtextalgorithm.stalgorithm.STAlgorithmPackage;
import org.eclipse.fordiac.ide.structuredtextcore.scoping.STCoreLinkingDiagnosticMessageProvider;
import org.eclipse.xtext.diagnostics.DiagnosticMessage;

public class STAlgorithmLinkingDiagnosticMessageProvider extends STCoreLinkingDiagnosticMessageProvider {

	@Override
	public DiagnosticMessage getUnresolvedProxyMessage(final ILinkingDiagnosticContext context) {
		if (context.getReference() == STAlgorithmPackage.Literals.ST_METHOD__RETURN_TYPE) {
			return createDataTypeDiagnosticMessage(context);
		}
		return super.getUnresolvedProxyMessage(context);
	}
}
