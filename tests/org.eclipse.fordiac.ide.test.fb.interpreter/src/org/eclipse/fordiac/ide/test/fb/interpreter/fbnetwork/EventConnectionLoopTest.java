/*******************************************************************************
 * Copyright (c) 2022 Paul Pavlicek and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Paul Pavlicek, Bianca Wiesmayr
 *       - initial implementation and/or documentation
 *******************************************************************************/
package org.eclipse.fordiac.ide.test.fb.interpreter.fbnetwork;


import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.fordiac.ide.fb.interpreter.api.FBTransactionBuilder;
import org.eclipse.fordiac.ide.fb.interpreter.mm.utils.FBNetworkTestRunner.IllegalTraceException;
import org.eclipse.fordiac.ide.model.libraryElement.FBNetwork;
import org.eclipse.fordiac.ide.model.libraryElement.SubApp;
import org.eclipse.fordiac.ide.test.fb.interpreter.infra.AbstractInterpreterTest;

/** fb network including an E_SPLIT, E_REND, and another E_SPLIT tests parallel paths (two output events sent in
 * parallel by E_SPLIT) */
public class EventConnectionLoopTest extends AbstractInterpreterTest {
	private static final String INITIAL_FB = "E_PERMIT"; //$NON-NLS-1$
	private static final String INITIAL_PIN = "EI"; //$NON-NLS-1$

	@Override
	public void test() throws IllegalTraceException {

		final FBNetwork network = loadFbNetwork("ReferenceExamples", "ReferenceExamples", "EventConnections"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertNotNull(network);
		final SubApp subApp = network.getSubAppNamed("Ex6a"); //$NON-NLS-1$

		// TODO EventConnectionLoopTest does not work correctly(does not terminate)
		// TODO fix interpreter to solve the problem(test works)
		// final EList<Transaction> returnedTransactions = runFBNetworkTest(subApp.getSubAppNetwork(), INITIAL_FB,
		// INITIAL_PIN);

		final List<FBTransactionBuilder> expectedTs = new ArrayList<>();

		expectedTs.add(new FBTransactionBuilder(INITIAL_FB + "." + INITIAL_PIN).addOutputEvent("E_PERMIT.EO")); //$NON-NLS-1$ //$NON-NLS-2$
		expectedTs.add(new FBTransactionBuilder("E_CTU.CU").addOutputEvent("E_CTU.CUO", "Q:=FALSE;CV:=1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		expectedTs.add(new FBTransactionBuilder("SimpleNOT.REQ").addOutputEvent("SimpleNOT.CNF", "DO1:=TRUE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		expectedTs.add(new FBTransactionBuilder("E_PERMIT.EI").addOutputEvent("E_PERMIT.EO")); //$NON-NLS-1$ //$NON-NLS-2$
		expectedTs.add(new FBTransactionBuilder("E_CTU.CU").addOutputEvent("E_CTU.CUO", "Q:=TRUE;CV:=2")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		expectedTs.add(new FBTransactionBuilder("SimpleNOT.REQ").addOutputEvent("SimpleNOT.CNF", "DO1:=FALSE")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		expectedTs.add(new FBTransactionBuilder("E_PERMIT.EI")); //$NON-NLS-1$

		// checkNetworkResults(returnedTransactions, expectedTs);
	}
}