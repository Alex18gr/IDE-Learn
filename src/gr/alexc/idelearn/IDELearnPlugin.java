package gr.alexc.idelearn;

import javax.inject.Inject;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.equinox.log.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class IDELearnPlugin extends AbstractUIPlugin {
	
	/** Identifier of the plugin. */
	public static final String PLUGIN_ID = "gr.alexc.idelearn";
	
	private static IDELearnPlugin plugin;
	
	@Inject Logger logger;

	public IDELearnPlugin() {
		super();
		plugin = this;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		MessageDialog.openInformation(
				null,
				"Exercise Task Status View",
				"Plugin Started");
		
		System.out.println("Plugin Started");
		
		final IWorkbench workbench = PlatformUI.getWorkbench();
		
		workbench.getDisplay().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				
				IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
				
				for (IProject project : projects) {
					// logger.debug(project.getName());
					
					
					IScopeContext projectScope = new ProjectScope(project);
					Preferences projectNode = projectScope.getNode(PLUGIN_ID);
					if (projectNode != null) {
						projectNode.put("exersiceId", "001");
						try {
							projectNode.flush();
						} catch (BackingStoreException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}
		});
		
		
		
		
		
		
		
		
	}
	
	
	
	
	
}
