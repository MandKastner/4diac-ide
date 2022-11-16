/*******************************************************************************
 * Copyright (c) 2022 Primetals Technologies Austria GmbH
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Alois Zoitl, Lukas Wais
 *                - initial API and implementation and/or initial documentation
 *   Lukas Wais   - enable image inserting
 *******************************************************************************/
package org.eclipse.fordiac.ide.fbtypeeditor.doc.editors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.fordiac.ide.fbtypeeditor.editors.IFBTEditorPart;
import org.eclipse.fordiac.ide.model.commands.change.ChangeDocumentationCommand;
import org.eclipse.fordiac.ide.model.libraryElement.FBType;
import org.eclipse.fordiac.ide.model.libraryElement.LibraryElementPackage;
import org.eclipse.fordiac.ide.typemanagement.FBTypeEditorInput;
import org.eclipse.fordiac.ide.ui.FordiacLogHelper;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.nebula.widgets.richtext.RichTextEditor;
import org.eclipse.nebula.widgets.richtext.RichTextEditorConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

public class DescriptionEditor extends EditorPart implements IFBTEditorPart {
	// @formatter:off
	private static final String TOOLBAR_GROUP_CONFIGURATION =
			"[" 	 																	//$NON-NLS-1$
			+ "{ name: 'clipboard', groups: [ 'undo', 'clipboard'] },"					//$NON-NLS-1$
			+ "{ name: 'colors' },"  													//$NON-NLS-1$
			+ "{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] }," 			//$NON-NLS-1$
			+ "{ name: 'styles' }," 													//$NON-NLS-1$
			+ "{ name: 'paragraph', groups: [ 'align', 'list', 'indent' ] }," 			//$NON-NLS-1$
			+ "{ name: 'find'}," 														//$NON-NLS-1$
			+ "{ name: 'insert' }," //$NON-NLS-1$
			+ "]"; 																		//$NON-NLS-1$
	// @formatter:on

	private CommandStack commandStack;
	private RichTextEditor editor;
	private boolean blockListeners = false;

	private final Adapter sysConfListener = new AdapterImpl() {
		@Override
		public void notifyChanged(final Notification notification) {
			if (!blockListeners && LibraryElementPackage.eINSTANCE.getIdentification_Description()
					.equals(notification.getFeature())) {
				final CommandStack comStackbuf = commandStack;
				commandStack = null;
				if (editor != null && !editor.isDisposed()) {
					editor.setText(getFbType().getIdentification().getDescription());
				}
				commandStack = comStackbuf;
			}
		}
	};

	private FBType getFbType() {
		return getEditorInput().getContent();
	}

	@Override
	public FBTypeEditorInput getEditorInput() {
		return (FBTypeEditorInput) super.getEditorInput();
	}

	@Override
	public void doSave(final IProgressMonitor monitor) {
		// nothing to be done
	}

	@Override
	public void doSaveAs() {
		// nothing to be done
	}

	@Override
	public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
		setInput(input);
		setSite(site);
		setPartName("Description"); //$NON-NLS-1$
		getFbType().getIdentification().eAdapters().add(sysConfListener);
	}

	@Override
	public void dispose() {
		commandStack = null;
		getFbType().getIdentification().eAdapters().remove(sysConfListener);
		super.dispose();
	}

	@Override
	public boolean isDirty() {
		return commandStack.isDirty();
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(final Composite parent) {
		GridLayoutFactory.fillDefaults().margins(0, 0).applyTo(parent);

		try {

			final Button button = new Button(parent, SWT.PUSH);
			button.setText("Add Image"); //$NON-NLS-1$ this button is temporary, therefore the NLS-tag
			button.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
			button.addListener(SWT.Selection, event -> {
				final FileDialog dialog = new FileDialog(parent.getShell());
				final String filename = dialog.open();
				if (filename != null) {
					final String base64 = encodeImageToBase64(new File(filename));
					editor.insertHTML("<img src= data:image/png;base64," + base64 + ">"); //$NON-NLS-1$ //$NON-NLS-2$
				}
			});

			final RichTextEditorConfiguration editorConfig = new RichTextEditorConfiguration();

			editorConfig.setOption("toolbarGroups", TOOLBAR_GROUP_CONFIGURATION); //$NON-NLS-1$
			editorConfig.removeDefaultToolbarButton("Flash", "Table", "HorizontalRule", "SpecialChar" + "", "Smiley",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$//$NON-NLS-5$ //$NON-NLS-6$
					"PageBreak", "Iframe"); //$NON-NLS-1$ //$NON-NLS-2$
			editor = new RichTextEditor(parent, editorConfig);
			GridDataFactory.fillDefaults().grab(true, true).applyTo(editor);
			editor.setText(getFbType().getDocumentation());
			editor.addModifyListener(e -> {
				if (editor != null && editor.getText() != null
						&& !editor.getText().equals(getFbType().getDocumentation())) {
					executeCommand(new ChangeDocumentationCommand(getFbType(), editor.getText()));
				}
			});
		} catch (final SWTError e) {
			final Label errorLabel = new Label(parent, SWT.NONE);
			errorLabel.setText(e.getMessage());
			GridDataFactory.swtDefaults().applyTo(errorLabel);
		}
	}

	private void executeCommand(final Command cmd) {
		if (commandStack != null && cmd != null) {
			blockListeners = true;
			commandStack.execute(cmd);
			blockListeners = false;
		}
	}

	@Override
	public void setFocus() {
		if (editor != null && !editor.isDisposed()) {
			editor.setFocus();
		}
	}

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {
		// nothing to be done
	}

	@Override
	public void gotoMarker(final IMarker marker) {
		// For now we don't handle markers in this editor
	}

	@Override
	public boolean isMarkerTarget(final IMarker marker) {
		// For now we don't handle markers in this editor
		return false;
	}

	@Override
	public boolean outlineSelectionChanged(final Object selectedElement) {
		return false;
	}

	@Override
	public void setCommonCommandStack(final CommandStack commandStack) {
		this.commandStack = commandStack;
	}

	@Override
	public void reloadType(final FBType type) {
		getEditorInput().setFbType(type);
		if (editor != null && !editor.isDisposed()) {
			editor.setText(getFbType().getDocumentation());
		}
	}

	@Override
	public Object getSelectableEditPart() {
		return null;
	}

	private static String encodeImageToBase64(final File image) {
		try (FileInputStream fileInputStreamReader = new FileInputStream(image)) {
			final byte[] bytes = new byte[(int) image.length()];
			fileInputStreamReader.read(bytes);

			return Base64.getEncoder().encodeToString(bytes);
		} catch (final FileNotFoundException e) {
			FordiacLogHelper.logError(e.getMessage());
		} catch (final IOException e) {
			FordiacLogHelper.logError(e.getMessage());
		}

		return ""; //$NON-NLS-1$
	}

}