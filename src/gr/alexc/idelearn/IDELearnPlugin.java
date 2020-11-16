package gr.alexc.idelearn;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class IDELearnPlugin extends AbstractUIPlugin {
	
	/** Identifier of the plugin. */
	public static final String PLUGIN_ID = "gr.alexc.idelearn";
	
	private static IDELearnPlugin plugin;

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
	}
	
	
	
	
	
}
