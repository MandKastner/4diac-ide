/*******************************************************************************
 * Copyright (c) 2012 - 2016 Profactor GbmH, fortiss GmbH
 * 				 2019 Johannes Kepler University Linz
 * 				 2022 Primetals Technologies Austria GmbH
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
 *   Alois Zoitl - added removing the watch listener on dispose
 *   Fabio Gandolfi - added selection handling
 *******************************************************************************/
package org.eclipse.fordiac.ide.monitoring.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.fordiac.ide.application.editparts.FBEditPart;
import org.eclipse.fordiac.ide.application.editparts.FBNetworkEditPart;
import org.eclipse.fordiac.ide.application.editparts.GroupContentEditPart;
import org.eclipse.fordiac.ide.application.editparts.GroupEditPart;
import org.eclipse.fordiac.ide.application.editparts.UISubAppNetworkEditPart;
import org.eclipse.fordiac.ide.deployment.monitoringbase.IMonitoringListener;
import org.eclipse.fordiac.ide.deployment.monitoringbase.MonitoringBaseElement;
import org.eclipse.fordiac.ide.deployment.monitoringbase.PortElement;
import org.eclipse.fordiac.ide.model.monitoring.MonitoringElement;
import org.eclipse.fordiac.ide.monitoring.Messages;
import org.eclipse.fordiac.ide.monitoring.MonitoringManager;
import org.eclipse.fordiac.ide.monitoring.provider.WatchesContentProvider;
import org.eclipse.fordiac.ide.monitoring.provider.WatchesLabelProvider;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.part.ViewPart;

public class WatchesView extends ViewPart implements ISelectionListener {

	private Composite root;
	private FilteredTree filteredTree;
	private final WatchesContentProvider provider = new WatchesContentProvider();
	private boolean visible = false;
	private boolean selectionActive = false;
	private Action toggleSelection;

	private final IMonitoringListener listener = new IMonitoringListener() {

		@Override
		public void notifyTriggerEvent(final PortElement port) {
			// currently nothing to do
		}

		@Override
		public void notifyRemovePort(final PortElement port) {
			filteredTree.getViewer().refresh();
		}

		@Override
		public void notifyAddPort(final PortElement port) {
			if (!filteredTree.isDisposed()) {
				filteredTree.getViewer().refresh();
			}
		}

		@Override
		public void notifyWatchesChanged() {
			provider.update();
			if (!filteredTree.isDisposed()) {
				filteredTree.getViewer().refresh();
			}
		}
	};

	@Override
	public void createPartControl(final Composite parent) {

		root = new Composite(parent, SWT.NONE);
		root.setLayout(new GridLayout());
		final PatternFilter patternFilter = new PatternFilter();

		filteredTree = new FilteredTree(root, SWT.H_SCROLL | SWT.V_SCROLL, patternFilter, true, true);

		final GridData treeGridData = new GridData();
		treeGridData.grabExcessHorizontalSpace = true;
		treeGridData.grabExcessVerticalSpace = true;
		treeGridData.horizontalAlignment = SWT.FILL;
		treeGridData.verticalAlignment = SWT.FILL;

		filteredTree.setLayoutData(treeGridData);

		final TreeViewerColumn column1 = new TreeViewerColumn(filteredTree.getViewer(), SWT.None);
		column1.getColumn().setText("Watched Element");
		column1.getColumn().setWidth(340);
		final TreeViewerColumn column2 = new TreeViewerColumn(filteredTree.getViewer(), SWT.None);
		column2.getColumn().setText("Value");
		column2.getColumn().setWidth(100);
		column2.setEditingSupport(new EditingSupport(column2.getViewer()) {

			@Override
			protected void setValue(final Object element, final Object value) {
				if (element instanceof WatchValueTreeNode) {
					final MonitoringBaseElement monitoringBaseElement = ((WatchValueTreeNode) element)
							.getMonitoringBaseElement();
					MonitoringManager.getInstance().writeValue((MonitoringElement) monitoringBaseElement,
							(String) value);
				}

			}

			@Override
			protected Object getValue(final Object element) {
				if (element instanceof WatchValueTreeNode) {
					return ((WatchValueTreeNode) element).getValue();
				}
				return ""; //$NON-NLS-1$
			}

			@Override
			protected CellEditor getCellEditor(final Object element) {
				return new TextCellEditor(filteredTree.getViewer().getTree());
			}

			@Override
			protected boolean canEdit(final Object element) {
				// TODO REMOVE that check after forcing is possible
				if (element instanceof WatchValueTreeNode && ((WatchValueTreeNode) element).isStructNode()) {
					return ((WatchValueTreeNode) element).isStructRootNode();
				}
				return true;
			}
		});

		filteredTree.getViewer().getTree().setHeaderVisible(true);
		filteredTree.getViewer().getTree().setLinesVisible(true);

		filteredTree.getViewer().setContentProvider(provider);

		filteredTree.getViewer().setLabelProvider(new WatchesLabelProvider());
		filteredTree.getViewer().setInput(new Object());

		contributeToActionBars();

		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
		getSite().getPage().addPartListener(new IPartListener2() {
			@Override
			public void partHidden(final IWorkbenchPartReference ref) {
				final IWorkbenchPart part = ref.getPart(false);
				if (part != null && part.getClass().getName().equals(this.getClass().getName().split("\\$")[0])) {
					visible = false;
				}
			}

			@Override
			public void partVisible(final IWorkbenchPartReference ref) {
				final IWorkbenchPart part = ref.getPart(false);
				if (part != null && part.getClass().getName().equals(this.getClass().getName().split("\\$")[0])) {
					visible = true;
				}
			}
		});

		addWatchesAdapters();

	}

	private void addWatchesAdapters() {
		MonitoringManager.getInstance().addMonitoringListener(listener);
	}

	/** Contribute to action bars. */
	private void contributeToActionBars() {
		final IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	/** Fill local tool bar.
	 *
	 * @param manager the manager */
	private void fillLocalToolBar(final IToolBarManager manager) {
		toggleSelection = new Action(Messages.MonitoringManagerUtils_SelectionFilteringActive, Action.AS_RADIO_BUTTON) {
			@Override
			public void run() {
				selectionActive = !selectionActive;
				toggleSelection.setChecked(selectionActive);
				if (!selectionActive) {
					update();
				}
			}
		};
		toggleSelection.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_ELCL_SYNCED));
		toggleSelection.setToolTipText(Messages.MonitoringManagerUtils_SelectionFilteringActive);
		manager.add(toggleSelection);
	}

	@Override
	public void dispose() {
		MonitoringManager.getInstance().removeMonitoringListener(listener);
		super.dispose();
	}

	@Override
	public void setFocus() {
		// currently nothing to do
	}

	@Override
	public void selectionChanged(final IWorkbenchPart part, final ISelection selection) {

		if (visible && (null != root) && (!root.isDisposed()) && selectionActive
				&& selection instanceof StructuredSelection) {

			List<FBEditPart> fbs;

			if (((IStructuredSelection) selection).size() == 1
					&& (((IStructuredSelection) selection).getFirstElement() instanceof FBNetworkEditPart
							|| ((IStructuredSelection) selection)
							.getFirstElement() instanceof UISubAppNetworkEditPart
							|| ((IStructuredSelection) selection).getFirstElement() instanceof GroupEditPart)) {
				final GraphicalViewer viewer = part.getAdapter(GraphicalViewer.class);
				final List<?> children = viewer.getContents().getChildren();

				if (((IStructuredSelection) selection).getFirstElement() instanceof GroupEditPart) {
					fbs = extractEditPartsFromGroupEditParts(children);
				} else if (((IStructuredSelection) selection).getFirstElement() instanceof GroupContentEditPart) {
					fbs = extractEditPartsFromGroupEditParts(children);
				} else {
					fbs = extractEditPartsFromGroupEditParts(children);
					fbs.addAll(getEditParts(children));
				}
			} else {
				fbs = getEditParts(((IStructuredSelection) selection).toList());
			}

			if (!fbs.isEmpty()) {

				final Collection<MonitoringBaseElement> elements = MonitoringManager.getInstance()
						.getAllElementsToMonitor();
				final  Collection<MonitoringBaseElement> activeElements = new ArrayList<>();
				for (final MonitoringBaseElement monitoringBaseElement : elements) {

					if ((fbs.stream()
							.anyMatch(sel -> sel.getModel().equals(monitoringBaseElement.getPort().getFb())))) {

						activeElements.add(monitoringBaseElement);
					}

					updateWithList(activeElements);
				}
			}
		}
	}

	public void update() {
		provider.update();
		if (!filteredTree.isDisposed()) {
			filteredTree.getViewer().refresh();
		}
	}

	public void updateWithList(final Collection<MonitoringBaseElement> activeElements) {
		provider.updateWithList(activeElements);
		if (!filteredTree.isDisposed()) {
			filteredTree.getViewer().refresh();
		}
	}

	public static List<FBEditPart> extractEditPartsFromGroupEditParts(final List<?> input) {
		final List<FBEditPart> fbs = new ArrayList<>();
		for (final Object obj : input) {
			if (obj instanceof GroupEditPart) {
				fbs.addAll(extractEditPartsFromGroupContentEditParts(((GroupEditPart) obj).getChildren()));
			}
		}
		return fbs;
	}

	public static List<FBEditPart> extractEditPartsFromGroupContentEditParts(final List<?> input) {
		final List<FBEditPart> fbs = new ArrayList<>();
		for (final Object obj : input) {
			if (obj instanceof GroupContentEditPart) {
				fbs.addAll(getEditParts(((GroupContentEditPart) obj).getChildren()));
			}
		}
		return fbs;
	}

	@SuppressWarnings("unchecked")
	public static List<FBEditPart> getEditParts(final List<?> input) {
		return (List<FBEditPart>) input.stream().filter(sel -> sel instanceof FBEditPart).collect(Collectors.toList());
	}

}
