package gr.alexc.idelearn.job;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.ISchedulingRule;

public class ExerciseAuditorJob extends WorkspaceJob implements ISchedulingRule {

	public ExerciseAuditorJob(String name) {
		super(name);
		
	}

	@Override
	public boolean contains(ISchedulingRule rule) {
		return rule instanceof ExerciseAuditorJob;
	}

	@Override
	public boolean isConflicting(ISchedulingRule rule) {
		return rule instanceof ExerciseAuditorJob;
	}

	@Override
	public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
		return null;
	}

}
