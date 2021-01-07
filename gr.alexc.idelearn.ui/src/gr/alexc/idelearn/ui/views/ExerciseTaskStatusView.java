package gr.alexc.idelearn.ui.views;

import java.net.URL;
import java.util.Collections;
import java.util.Optional;

import javax.inject.Inject;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import gr.alexc.idelearn.ui.IDELearnPlugin;
import gr.alexc.idelearn.ui.classanalysis.exercise.domain.Exercise;
import gr.alexc.idelearn.ui.classanalysis.exercise.domain.requirement.Requirement;
import gr.alexc.idelearn.ui.learn.LearnPlugin;
import gr.alexc.idelearn.ui.learn.listener.SingleChangeType;
import gr.alexc.idelearn.ui.learn.listener.SingleExerciseChangeEvent;
import gr.alexc.idelearn.ui.learn.listener.SingleExerciseChangedListener;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class ExerciseTaskStatusView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "idelearn.views.ExerciseTaskStatusView";

	@Inject
	IWorkbench workbench;
	
	private LearnPlugin learnPlugin;

	private TableViewer viewer;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;
	private Exercise selectedExercise;
	
	private final ImageDescriptor COMPLETED = AbstractUIPlugin.imageDescriptorFromPlugin(IDELearnPlugin.PLUGIN_ID, "icons/tick.gif"); // getImageDescriptor("tick.gif");
	private final ImageDescriptor NOT_COMPLETED = AbstractUIPlugin.imageDescriptorFromPlugin(IDELearnPlugin.PLUGIN_ID, "icons/error.gif"); // getImageDescriptor("error.gif");

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}

		@Override
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}

		@Override
		public Image getImage(Object obj) {
			return workbench.getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
	
	

	public ExerciseTaskStatusView() {
		this.learnPlugin = LearnPlugin.getInstance();
	}

	@Override
	public void createPartControl(Composite parent) {

		setupViewer(parent);
		// Create the help context id for the viewer's control
		workbench.getHelpSystem().setHelp(viewer.getControl(), "IDELearn.viewer");
		getSite().setSelectionProvider(viewer);
//		makeActions();
//		hookContextMenu();
//		hookDoubleClickAction();
//		contributeToActionBars();
	}

	private void setupViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);

		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setLabelProvider(new ViewLabelProvider());
		createColumns(viewer);

		GridData gridData = new GridData(GridData.FILL_BOTH);
		viewer.getControl().setLayoutData(gridData);

		Table table = viewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		// add the resource listeners
		learnPlugin.addSingleExerciseChangedListener(new SingleExerciseChangedListener() {

			@Override
			public void exerciseChanged(SingleExerciseChangeEvent event) {
				
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						SingleChangeType type = event.getChangeType();
						if (type.equals(SingleChangeType.SELECTED_EXERCISE)) {
							selectedExercise = event.getExercise();
							// initialize array
							viewer.setInput(selectedExercise.getAllRequirements());
						}
						if (type.equals(SingleChangeType.AUDITED_EXERCISE) && event.getExercise().equals(selectedExercise)) {
							// update array
							viewer.setInput(selectedExercise.getAllRequirements());
						}
						if (type.equals(SingleChangeType.REMOVED_EXERCISE) && event.getExercise().equals(selectedExercise)) {
							viewer.setInput(Collections.<Requirement>emptyList());
						}
						
						viewer.refresh();
					}
				});
			}
		});
		
		// initialize the data when the view is created
		initializeColumnData();
	}
	
	private void initializeColumnData() {
		Optional<Exercise> selectedExerciseOptional = learnPlugin.getSelectedExercise();
		if (selectedExerciseOptional.isPresent()) {
			selectedExercise = selectedExerciseOptional.get();
			// initialize array
			viewer.setInput(selectedExercise.getAllRequirements());
		}
	}

	private void createColumns(TableViewer viewer) {

		TableViewerColumn colRequirementCompleted = new TableViewerColumn(viewer, SWT.NONE);
		colRequirementCompleted.getColumn().setWidth(60);
		colRequirementCompleted.getColumn().setText("Status");
		colRequirementCompleted.setLabelProvider(new ColumnLabelProvider() {

			private ResourceManager resourceManager = new LocalResourceManager(JFaceResources.getResources());

			@Override
			public String getText(Object element) {
				return null; // no string representation, we only want to display the image
			}

			@Override
			public Image getImage(Object element) {
				if (selectedExercise.getExerciseCheckReport().getRequirementStatus((Requirement) element)) {
					return resourceManager.createImage(COMPLETED);
				}
				return resourceManager.createImage(NOT_COMPLETED);
			}

			@Override
			public void dispose() {
				super.dispose();
				resourceManager.dispose();
			}
		});

		TableViewerColumn colDescription = new TableViewerColumn(viewer, SWT.NONE);
		colDescription.getColumn().setWidth(1000);
		colDescription.getColumn().setResizable(true);
		colDescription.getColumn().setText("Description");
		colDescription.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Requirement p = (Requirement) element;
				return p.getDescription();
			}
		});

	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				ExerciseTaskStatusView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		manager.add(action2);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(
				PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		action2 = new Action() {
			public void run() {
				showMessage("Action 2 executed");
			}
		};
		action2.setText("Action 2");
		action2.setToolTipText("Action 2 tooltip");
		action2.setImageDescriptor(workbench.getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				IStructuredSelection selection = viewer.getStructuredSelection();
				Object obj = selection.getFirstElement();
				showMessage("Double-click detected on " + obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "Exercise Task Status View", message);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	private static ImageDescriptor getImageDescriptor(String file) {
	    Bundle bundle = FrameworkUtil.getBundle(ExerciseTaskStatusView.class);
	    URL url = FileLocator.find(bundle, new Path("icons/" + file), null);
	    return ImageDescriptor.createFromURL(url);
	}
}
