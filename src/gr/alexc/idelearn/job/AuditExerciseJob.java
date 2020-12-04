package gr.alexc.idelearn.job;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.service.prefs.Preferences;

import com.github.javaparser.utils.SourceRoot;

import gr.alexc.idelearn.IDELearnPlugin;
import gr.alexc.idelearn.classanalysis.exercise.ExerciseAnalyser;
import gr.alexc.idelearn.classanalysis.exercise.ExerciseCheckReport;
import gr.alexc.idelearn.classanalysis.exercise.domain.Exercise;
import gr.alexc.idelearn.classanalysis.parser.ClassChecker;
import gr.alexc.idelearn.learn.LearnPlugin;

public class AuditExerciseJob extends WorkspaceJob {
	
	private IProject project;

	public AuditExerciseJob(IProject project, String name) {
		super(name);
		this.project = project;
	}

	@Override
	public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
		
		LearnPlugin learnPlugin = LearnPlugin.getInstance();
		
		// check if the exercise object already exists
		IScopeContext projectScope = new ProjectScope(project);
		Preferences projectNode = projectScope.getNode(IDELearnPlugin.PLUGIN_ID);
		if (projectNode == null) {
			return Status.CANCEL_STATUS;
		}
		
		String projectId = projectNode.get("exersiceId", "");
		
		if (projectId.equals("")) {
			return Status.CANCEL_STATUS;
		}
		
		Exercise exercise = learnPlugin.getExerciseById(projectId);
		
		if (exercise == null) {
			return Status.CANCEL_STATUS;
		}
		
		
		
		
		// check if the exercise exists in the learn plugin from the .exercise file
		project.getLocationURI();
		IFile exerciseDescriptionFile = project.getFile(".exercise");
		if (exerciseDescriptionFile.exists()) {
			URI exerciseDescriptionFileURI = exerciseDescriptionFile.getLocationURI();
			
			// parse the exercise
			
		}
		
		// if the .exercise doesen't exists create it
		
		
			// if it exists continue
		
			// if it doesen't exist, check the .exercise file and create it
		
		try {
			// get the project root
			project.getLocationURI();
			IPath projectIPath = project.getLocation();
			Path projectPath = projectIPath.makeAbsolute().toFile().toPath();
			SourceRoot sourceRoot = new SourceRoot(projectPath);
			
			// run the java parser procedure
			ClassChecker checker = new ClassChecker();
			checker.checkClasses(sourceRoot);
			
			// after parsing, check the requirements
			ExerciseAnalyser analyser = new ExerciseAnalyser();
			ExerciseCheckReport checkReport = analyser.analyseExerciseRequirements(exercise, checker);
			
			// get and publish the results of the requirements
			
			
			return Status.OK_STATUS;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 
		
		
		return Status.CANCEL_STATUS;
	}

}
