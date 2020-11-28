package gr.alexc.idelearn.job;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.internal.wizards.datatransfer.ZipLeveledStructureProvider;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import gr.alexc.idelearn.IDELearnPlugin;
import gr.alexc.idelearn.Utils.ZipUtils;
import gr.alexc.idelearn.builder.LearnProjectNature;
import gr.alexc.idelearn.classanalysis.exercise.ExerciseParser;
import gr.alexc.idelearn.classanalysis.exercise.domain.Exercise;
import gr.alexc.idelearn.learn.LearnPlugin;

public class LoadExerciseJob extends WorkspaceJob {
	
	private String exerciseFilePath;

	public LoadExerciseJob(String exerciseFilePath, String name) {
		super(name);
		this.exerciseFilePath = exerciseFilePath;
	}

	@Override
	public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		
		// here we check if the nature is valid
		
		try {
			File file = new File(exerciseFilePath);
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			ZipInputStream zipInputStream = new ZipInputStream(bis);
			
			// unzip to temp directory
			Path tmpPath = Files.createTempDirectory(null);
			ZipUtils.unzipToDirectory(zipInputStream, tmpPath);
			
			ZipFile exerciseFile = new ZipFile(new File(exerciseFilePath));
			Enumeration<? extends ZipEntry> entries = exerciseFile.entries();
			while (entries.hasMoreElements()) {
			    System.out.println(entries.nextElement().getName());
			}
			
			
			
			ZipEntry exerciseJSON = exerciseFile.getEntry("exercise.json");
			InputStream exerciseJsonInputStream = exerciseFile.getInputStream(exerciseJSON);
//			String text = new BufferedReader(new InputStreamReader(exerciseJsonInputStream, StandardCharsets.UTF_8))
//					.lines()
//					.collect(Collectors.joining("\n"));
//			System.out.println(text);
			
			ExerciseParser exerciseJsonParser = new ExerciseParser();
			Exercise exercise = exerciseJsonParser.parseExercise(exerciseJsonInputStream);
			exerciseJsonInputStream.close();
			
//			if (exercise == null) {
//				
//			}
			
			LearnPlugin plugin = LearnPlugin.getInstance();
			
			// here we check if the project still exists in the workspace
			if (!plugin.checkExerciseExists(exercise.getId())) {
				
				ZipFile projectZipFile = new ZipFile(new File(tmpPath + "/project.zip"));
				
	 			IProjectDescription newProjectDescription = workspace.newProjectDescription("Execise Project");
				
			    IProject newProject = workspace.getRoot().getProject("Execise Project");
				newProject.create(newProjectDescription, monitor);
				newProject.open(monitor);
				
				IOverwriteQuery overwriteQuery = new IOverwriteQuery() {
				    public String queryOverwrite(String file) { return ALL; }
				};
				ZipLeveledStructureProvider provider = new ZipLeveledStructureProvider(projectZipFile);
				List<Object> fileSystemObjects = new ArrayList<Object>();
				Enumeration<? extends ZipEntry> exerciseZipEntries = projectZipFile.entries();
				while (exerciseZipEntries.hasMoreElements()) {
				    fileSystemObjects.add((Object)exerciseZipEntries.nextElement());
				}
				
				ImportOperation importOperation = new ImportOperation(newProject.getFullPath(), new ZipEntry("Execise Project"), provider, overwriteQuery, fileSystemObjects);
				importOperation.setCreateContainerStructure(false);
				importOperation.run(new NullProgressMonitor());
				
				newProjectDescription = newProject.getDescription();
				
				String[] natures = newProjectDescription.getNatureIds();
				String[] newNatures = new String[natures.length + 1];
				System.arraycopy(natures, 0, newNatures, 0, natures.length);
				newNatures[natures.length] = LearnProjectNature.NATURE_ID;
				
				newProjectDescription.setNatureIds(newNatures);
				newProject.setDescription(newProjectDescription, monitor);
				
				// set the exercise id variable in this project
				IScopeContext projectScope = new ProjectScope(newProject);
				Preferences projectNode = projectScope.getNode(IDELearnPlugin.PLUGIN_ID);
				if (projectNode != null) {
					projectNode.put("exersiceId", exercise.getId());
					try {
						projectNode.flush();
					} catch (BackingStoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace(); 
					}
				}
				
				
				plugin.addExercise(exercise);
				
				
			}
			
			
			
			
//			IStatus status = workspace.validateNatureSet(newNatures);
		
			
			
			// get the exercise file and parse it
			
			
			// get he exercise zip file and import it to the workspace
			
			// if the workspace doesen't exist create a new java project with the right configuration
			
//			ZipFile exerciseFile = new ZipFile(this.exerciseFilePath);
//			
//			Enumeration<? extends ZipEntry> entries = exerciseFile.entries();
//			
//			while(entries.hasMoreElements()){
//		        ZipEntry entry = entries.nextElement();
//		        InputStream stream = exerciseFile.getInputStream(entry);
//		        
//		       
//		        
//		    }
			
		} catch (IOException | InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Status.OK_STATUS;
	}
	
	private boolean exerciseExistsInWorkspace(String exerciseId) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		
		for (IProject project : projects) {
			IScopeContext projectScope = new ProjectScope(project);
			Preferences projectNode = projectScope.getNode(IDELearnPlugin.PLUGIN_ID);
			if (projectNode != null) {
				String projectExercieId = projectNode.get("exersiceId", "");
				if (exerciseId.equals(projectExercieId)) {
					// the exercise already exists in the workspace
					return true;
				}
			}
		}
		// the exercise is not in the workspace
		return false;
	}

}
