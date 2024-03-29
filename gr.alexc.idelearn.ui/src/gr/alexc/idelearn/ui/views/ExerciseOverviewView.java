package gr.alexc.idelearn.ui.views;

import java.text.DecimalFormat;

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
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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

import gr.alexc.idelearn.ui.classanalysis.exercise.domain.Exercise;
import gr.alexc.idelearn.ui.learn.LearnPlugin;
import gr.alexc.idelearn.ui.learn.listener.SingleChangeType;
import gr.alexc.idelearn.ui.learn.listener.SingleExerciseChangeEvent;
import gr.alexc.idelearn.ui.learn.listener.SingleExerciseChangedListener;


public class ExerciseOverviewView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "gr.alexc.idelearn.views.ExerciseOverviewView";

	private static final String EMPTY_DESCRIPTION_TEXT = "";

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

	@Override
	public void createPartControl(Composite parent) {

		GridLayout mainLayout = new GridLayout();
		parent.setLayout(mainLayout);

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
				IStructuredSelection selection = event.getStructuredSelection();
				if (selection.getFirstElement() == null) {
					descriptionText.setText(EMPTY_DESCRIPTION_TEXT);
				} else {
					selectedExercise = (Exercise) event.getStructuredSelection().getFirstElement();
					DecimalFormat df = new DecimalFormat();
					df.setMaximumFractionDigits(2);
					descriptionText.setText(selectedExercise.getDescription());
					Float percentage = selectedExercise.getExerciseCheckReport().getCompletedPercentage();
					if (percentage == null) percentage = 0.0f;
					statusLabel.setText(df.format(percentage)
							+ "% of the exercise completed");
					statusProgressBar.setSelection(
							Math.round(percentage));
					LearnPlugin.getInstance()
							.exerciseSelected((Exercise) event.getStructuredSelection().getFirstElement());
				}
			}
		});

		GridData comboBoxData = new GridData(SWT.FILL, SWT.FILL, true, false);
		comboBoxData.widthHint = 150;
//		comboBoxData.heightHint = 150;
		comboBoxData.minimumWidth = 1;
		comboBoxData.minimumHeight = 1;
//		exercisesListCombo.setLayoutData(comboBoxData);
		comboViewer.getCombo().setLayoutData(comboBoxData);

		descriptionText = new Text(parent, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);
		descriptionText.setText(EMPTY_DESCRIPTION_TEXT);
		GridData descriptionTextGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
//		descriptionTextGridData.heightHint = 250;
		descriptionTextGridData.minimumHeight = 1;
		descriptionText.setLayoutData(descriptionTextGridData);

		// Status Composite

		Composite statusComposite = createStatsComposite(parent);
		GridData statusCompositeGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		statusComposite.setLayoutData(statusCompositeGridData);
		
		// Initialize the data from the Learn plugin with the current exercises
		// that opened before the creation of this view
		LearnPlugin.getInstance().getWorkspaceExercises().forEach((e) -> {
			comboViewer.add(e);
		});

		// add the resource listeners
		LearnPlugin.getInstance().addSingleExerciseChangedListener(new SingleExerciseChangedListener() {

			@Override
			public void exerciseChanged(SingleExerciseChangeEvent event) {
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						if (event.getChangeType().equals(SingleChangeType.ADDED_EXERCISE)) {
							comboViewer.add(event.getExercise());
						} else if (event.getChangeType().equals(SingleChangeType.REMOVED_EXERCISE)) {
							if (comboViewer.getSelection().equals(event.getExercise())) {
								comboViewer.setSelection(null);
							}
							comboViewer.remove(event.getExercise());
						} else if (event.getChangeType().equals(SingleChangeType.AUDITED_EXERCISE)) {
							if (event.getExercise().equals(selectedExercise)) {
								DecimalFormat df = new DecimalFormat();
								df.setMaximumFractionDigits(2);
								statusLabel.setText(df.format(selectedExercise.getExerciseCheckReport().getCompletedPercentage())
										+ "% of the exercise completed");
								statusProgressBar.setSelection(
										Math.round(selectedExercise.getExerciseCheckReport().getCompletedPercentage()));
							}
						}
					}
				});
			}
		});
	}

	private Composite createStatsComposite(Composite parent) {
		Composite statusComposite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		statusComposite.setLayout(layout);

		statusLabel = new Label(statusComposite, SWT.BORDER);
		GridData statusLabelGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		statusLabel.setLayoutData(statusLabelGridData);
		statusLabel.setText("% completed");

		statusProgressBar = new ProgressBar(statusComposite, SWT.SMOOTH);
		Rectangle clientArea = parent.getClientArea();
		statusProgressBar.setBounds(clientArea.x, clientArea.y, 200, 32);
		statusProgressBar.setMinimum(0);
		statusProgressBar.setMaximum(100);
		GridData progressBarGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		statusLabel.setLayoutData(progressBarGridData);

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

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "Exercise Overview View", message);
	}

	@Override
	public void setFocus() {
		comboViewer.getControl().setFocus();
	}

}
