package gr.alexc.idelearn.views;

import javax.inject.Inject;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import gr.alexc.idelearn.classanalysis.exercise.domain.Exercise;
import gr.alexc.idelearn.learn.LearnPlugin;
import gr.alexc.idelearn.learn.listener.SingleChangeType;
import gr.alexc.idelearn.learn.listener.SingleExerciseChangeEvent;
import gr.alexc.idelearn.learn.listener.SingleExerciseChangedListener;

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

public class ExerciseOverviewView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "gr.alexc.idelearn.views.ExerciseOverviewView";

	@Inject
	IWorkbench workbench;

	private TableViewer viewer;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;

//	private Combo exercisesListCombo;
	private ComboViewer comboViewer;
	private Text descriptionText;
	private Label statusLabel;
	private ProgressBar statusProgressBar;

	private Exercise selectedExercise;

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

	@Override
	public void createPartControl(Composite parent) {

		GridLayout mainLayout = new GridLayout();
		parent.setLayout(mainLayout);

//		Composite mainComposite = new Composite(parent, SWT.BORDER);
//		RowLayout rowLayout = new RowLayout();
//		mainComposite.setLayout(rowLayout);
//		rowLayout.fill = true;
//		rowLayout.type = SWT.VERTICAL;

//		GridLayout mainLayout = new GridLayout();
//		parent.setLayout(mainLayout);

		comboViewer = new ComboViewer(parent, SWT.READ_ONLY);

		comboViewer.setContentProvider(ArrayContentProvider.getInstance());

		/* if the current person is selected, show text */
		comboViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof Exercise) {
					Exercise exercise = (Exercise) element;
					return exercise.getName();
				}
				return super.getText(element);
			}
		});
		comboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				descriptionText.setText(((Exercise) event.getStructuredSelection().getFirstElement()).getRequirementsDescription());
				
			}
		});

//		exercisesListCombo = new Combo(parent, SWT.READ_ONLY);
//		exercisesListCombo.addSelectionListener(new SelectionListener() {
//
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//
//				LearnPlugin learnPlugin = LearnPlugin.getInstance();
//				descriptionText.setText(((Exercise) e.data).getRequirementsDescription());
//
//			}
//
//			@Override
//			public void widgetDefaultSelected(SelectionEvent e) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//		exercisesListCombo.setItems("Exercise 1", "Exercise 2", "Exercise 3", "Exercise 4", "Exercise 5");
		GridData comboBoxData = new GridData(SWT.FILL, SWT.FILL, true, false);
		comboBoxData.widthHint = 150;
//		comboBoxData.heightHint = 150;
		comboBoxData.minimumWidth = 1;
		comboBoxData.minimumHeight = 1;
//		exercisesListCombo.setLayoutData(comboBoxData);
		comboViewer.getCombo().setLayoutData(comboBoxData);

		descriptionText = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);
		descriptionText.setText("This is a very large exercise description text...");
		GridData descriptionTextGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
//		descriptionTextGridData.heightHint = 250;
		descriptionTextGridData.minimumHeight = 1;
		descriptionText.setLayoutData(descriptionTextGridData);

		// Status Composite

		Composite statusComposite = createStatsComposite(parent);
		GridData statusCompositeGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		statusComposite.setLayoutData(statusCompositeGridData);

		// add the resource listeners
		LearnPlugin.getInstance().addSingleExerciseChangedListener(new SingleExerciseChangedListener() {

			@Override
			public void exerciseChanged(SingleExerciseChangeEvent event) {
				if (event.getChangeType().equals(SingleChangeType.ADDED_EXERCISE)) {
//					exercisesListCombo.add(event.getExercise().getName());
//					exercisesListCombo.setData(event.getExercise().getName(), event.getExercise());
					comboViewer.add(event.getExercise());
				}
			}
		});

//		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
//		
//		viewer.setContentProvider(ArrayContentProvider.getInstance());
//		viewer.setInput(new String[] { "One", "Two", "Three" });
//	viewer.setLabelProvider(new ViewLabelProvider());
//
//		// Create the help context id for the viewer's control
//		workbench.getHelpSystem().setHelp(viewer.getControl(), "gr.alexc.idelearn.viewer");
//		getSite().setSelectionProvider(viewer);
//		makeActions();
//		hookContextMenu();
//		hookDoubleClickAction();
//		contributeToActionBars();
	}

	private Composite createStatsComposite(Composite parent) {
		Composite statusComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		statusComposite.setLayout(layout);

		statusLabel = new Label(statusComposite, SWT.BORDER);
		statusLabel.setText("20% of the exercise completed");

		statusProgressBar = new ProgressBar(statusComposite, SWT.SMOOTH);

		return statusComposite;
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				ExerciseOverviewView.this.fillContextMenu(manager);
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
		MessageDialog.openInformation(viewer.getControl().getShell(), "Exercise OverviewView", message);
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void updateView() {
		// get the exercises data and update the view accordingly
	}
}
