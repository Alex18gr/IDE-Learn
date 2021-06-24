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

public class ExerciseTaskStatusView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "idelearn.views.ExerciseTaskStatusView";

	@Inject
	IWorkbench workbench;
	
	private LearnPlugin learnPlugin;

	private TableViewer viewer;
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

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
}
