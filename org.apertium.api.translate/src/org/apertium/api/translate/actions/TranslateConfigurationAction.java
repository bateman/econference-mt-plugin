package org.apertium.api.translate.actions;

import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class TranslateConfigurationAction implements
		IWorkbenchWindowActionDelegate, IActionFilter {

	public TranslateConfigurationAction() {
	}

	public void run(IAction action) {
		System.out.println("ConfigTranslateAction.run()");
		if (NetworkPlugin.getDefault().getHelper().getRegistry()
				.getDefaultBackend().isConnected()) {
			final TranslateConfigurationDialog dialog = new TranslateConfigurationDialog();
			dialog.open();
		} else {
			UiPlugin.getUIHelper()
					.showMessage(
							"Please, first connect to a server in oder to configure the MT plugin");
		}
	}

	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public boolean testAttribute(Object target, String name, String value) {
		// TODO Auto-generated method stub
		return false;
	}

}
