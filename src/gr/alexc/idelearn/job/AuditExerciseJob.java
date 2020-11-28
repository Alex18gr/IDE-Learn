package gr.alexc.idelearn.job;

import java.net.URI;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

public class AuditExerciseJob extends WorkspaceJob {
	
	private IProject project;

	public AuditExerciseJob(IProject project, String name) {
		super(name);
		this.project = project;
	}

	@Override
	public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
		
		// check if the exercise exists in the learn plugin from the .exercise file
		project.getLocationURI();
		IFile exerciseDescriptionFile = project.getFile(".exercise");
		if (exerciseDescriptionFile.exists()) {
			URI exerciseDescriptionFileURI = exerciseDescriptionFile.getLocationURI();
			
			// parse the exercise as 
		}
		
		// if the .exercise doesen't exists create it
		
		
			// if it exists continue
		
			// if it doesen't exist, check the .exercise file and create it
		
		// get the project root
		
		// run the java parser procedure
		
		// after parsing, check the requirements
		
		// get and publish the results of the requirements
		
		return null;
	}

}
