package gr.alexc.idelearn.ui;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.Preferences;

import gr.alexc.idelearn.ui.builder.LearnProjectNature;
import gr.alexc.idelearn.ui.classanalysis.exercise.ExerciseParser;
import gr.alexc.idelearn.ui.classanalysis.exercise.domain.Exercise;
import gr.alexc.idelearn.ui.job.AuditExerciseJob;
import gr.alexc.idelearn.ui.learn.LearnPlugin;

public class IDELearnPlugin extends AbstractUIPlugin {

	/** Identifier of the plugin. */
	public static final String PLUGIN_ID = "gr.alexc.idelearn.ui";

	private static IDELearnPlugin plugin;

	private Logger logger;

	public IDELearnPlugin() {
		super();
		plugin = this;
	}
	
//	@PostContextCreate
//    public void postContextCreate() throws IllegalStateException, IOException {
//        
//        // check if the instance location is already set,
//        // otherwise setting another one will throw an IllegalStateException
//        if (!Platform.getInstanceLocation().isSet()) {
//            String defaultPath = System.getProperty("user.home");
//
//            // build the desired path for the workspace
//            String path = defaultPath + "/ide-learn-workspace/";
//            URL instanceLocationUrl = new URL("file", null, path);
//            Platform.getInstanceLocation().set(instanceLocationUrl, false);
//        }
//    }

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		logger = PlatformUI.getWorkbench().getService(Logger.class);
		logger.info("Using the eclipse logger");

//		MessageDialog.openInformation(null, "Exercise Task Status View", "Plugin Started");

		System.out.println("Plugin Started");

		final IWorkbench workbench = PlatformUI.getWorkbench();

		workbench.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {

				IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

				for (IProject project : projects) {
					// logger.debug(project.getName());

//					IScopeContext projectScope = new ProjectScope(project);
//					Preferences projectNode = projectScope.getNode(PLUGIN_ID);
//					if (projectNode != null) {
//						projectNode.put("exersiceId", "001");
//						try {
//							projectNode.flush();
//						} catch (BackingStoreException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace(); 
//						}
//					}

					try {
						if (project.hasNature(LearnProjectNature.NATURE_ID)) {
//							processContainer(project);
							IFile file = project.getFile(".exercise");
							if (file.exists()) {
								InputStream inputStream = file.getContents();
								ExerciseParser exerciseJsonParser = new ExerciseParser();
								Exercise exercise = exerciseJsonParser.parseExercise(inputStream);
								LearnPlugin plugin = LearnPlugin.getInstance();
								plugin.addExercise(exercise);
//								logger.info(inputStreamToString(file.getContents()));
//								System.out.println(file.getContents().readAllBytes().toString());
								inputStream.close();
								
								// run the audit job for the project
								AuditExerciseJob job = new AuditExerciseJob(project, "Check Exercise Job");
								job.schedule();
							}
						}
					} catch (CoreException | IOException e2) {
						e2.printStackTrace();
					}
				}
			}
		});

		// subscribe to resources changes
		addRemoveExerciseProjectListener();
	}

	private void addRemoveExerciseProjectListener() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IResourceChangeListener rcl = new IResourceChangeListener() {
			public void resourceChanged(IResourceChangeEvent event) {
				if (event.getType() == IResourceChangeEvent.PRE_DELETE) {
					IProject deleteProject = (IProject) event.getResource();
					IScopeContext projectScope = new ProjectScope(deleteProject);
					Preferences projectNode = projectScope.getNode(PLUGIN_ID);
					if (projectNode != null) {
						String deleteProjectId = projectNode.get("exersiceId", "");
						if (!deleteProjectId.equals("")) {
							LearnPlugin.getInstance().removeExerciseById(deleteProjectId);
						}
					}
				}
			}
		};
		workspace.addResourceChangeListener(rcl);
	}

	private String inputStreamToString(InputStream inputStream) {
		Scanner s = new Scanner(inputStream).useDelimiter("\\A");
		String result = s.hasNext() ? s.next() : "";
		return result;
	}

	private void processContainer(IContainer container) throws CoreException {
		IResource[] members = container.members();

		for (IResource member : members) {
			if (member instanceof IContainer) {
				processContainer((IContainer) member);
			} else if (member instanceof IFile) {
				logger.info(member.getName());
			}
		}
	}
	
	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static IDELearnPlugin getDefault() {
		return plugin;
	}
	
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

}
