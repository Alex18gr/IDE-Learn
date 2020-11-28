package gr.alexc.idelearn;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Scanner;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.e4.core.services.log.Logger;
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
	
	private Logger logger;
	

	public IDELearnPlugin() {
		super();
		plugin = this;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		logger = PlatformUI.getWorkbench().getService(Logger.class);
		logger.info("Using the eclipse logger");
		
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
					
					logger.info("Hello World!!!");
					
					try {
						processContainer(project);
					} catch (CoreException e1) {
						e1.printStackTrace();
					}
					
					
					IFile file = project.getFile(".exercise");
					if (file.exists()) {
						try {
							InputStream inputStream = file.getContents();
							logger.info(inputStreamToString(file.getContents()));
							System.out.println(file.getContents().readAllBytes().toString());
							inputStream.close();
						} catch (IOException | CoreException e) {
							e.printStackTrace();
						}
					}
					
				}
				
			}
		});
		
		
		
		
		
		
		
	}
	
	private String inputStreamToString(InputStream inputStream) {
		Scanner s = new Scanner(inputStream).useDelimiter("\\A");
		String result = s.hasNext() ? s.next() : "";
		return result;
	}
	
	private void processContainer(IContainer container) throws CoreException
	{
	   IResource [] members = container.members();

	   for (IResource member : members)
	    {
	      if (member instanceof IContainer) 
	       {
	         processContainer((IContainer)member);
	       }
	      else if (member instanceof IFile)
	       {
	    	  logger.info(member.getName());
	       }
	    }
	}
	
	
	
	
	
}
