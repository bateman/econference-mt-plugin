package org.apertium.api.translate.actions;

import it.uniba.di.cdg.xcore.network.NetworkPlugin;
import it.uniba.di.cdg.xcore.ui.UiPlugin;

import org.apertium.api.translate.TranslatePlugin;
import org.apertium.api.translate.internal.TranslateM2MHelper;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class WizardMTAction extends AbstractHandler implements
        IWorkbenchWindowActionDelegate {

    @Override
    public void addHandlerListener( IHandlerListener handlerListener ) {

    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public void run( IAction action ) {
        if (NetworkPlugin.getDefault().getHelper().getOnlineBackends().size() == 0) {
            UiPlugin.getUIHelper().showErrorMessage( "Please, connect first!" );
            return;
        }
        TranslatePlugin defaultPlugin = TranslatePlugin.getDefault();
        defaultPlugin.setHelper( new TranslateM2MHelper( UiPlugin.getUIHelper(), NetworkPlugin
                .getDefault().getHelper() ) );
        TranslatePlugin.getDefault().getHelper().openInviteWizard();
    }

    @Override
    public Object execute( ExecutionEvent event ) throws ExecutionException {
        run( null );
        return null;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean isHandled() {
        return false;
    }

    @Override
    public void removeHandlerListener( IHandlerListener handlerListener ) {

    }

    @Override
    public void selectionChanged( IAction action, ISelection selection ) {

    }

    @Override
    public void init( IWorkbenchWindow window ) {

    }

}
