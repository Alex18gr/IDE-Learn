package gr.alexc.idelearn.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import gr.alexc.idelearn.ui.job.LoadExerciseJob;

public class OpenExerciseHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		FileDialog dialog = new FileDialog(window.getShell(), SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.exercisepackage" });
		String result = dialog.open();
		
		// check if the project exists in the workspace
		if (result != null) {
			LoadExerciseJob loadExerciseJob = new LoadExerciseJob(result, "Open Exercise Job");

			// start the import project job
			loadExerciseJob.schedule();
			
		}
		
		return null;
	}
}
