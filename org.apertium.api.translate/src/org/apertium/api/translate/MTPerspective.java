package org.apertium.api.translate;

import it.uniba.di.cdg.xcore.econference.ui.views.AgendaView;

import org.apertium.api.translate.views.TranslateChatRoomView;
import org.apertium.api.translate.views.TranslateM2MHandRaiseView;
import org.apertium.api.translate.views.TranslateM2Mview;
import org.apertium.api.translate.views.TranslateWhiteBoardView;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class MTPerspective implements IPerspectiveFactory {

	public static final String ID = "org.apertium.api.translate.MTPerspective";
	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorAreaId = layout.getEditorArea();
        layout.setEditorAreaVisible( false );
        
        //Adding ChatRoomView
        layout.addPlaceholder( TranslateChatRoomView.ID + ":*", IPageLayout.LEFT, 0.2f, editorAreaId );
        layout.addView( TranslateChatRoomView.ID, IPageLayout.LEFT, 0.2f, editorAreaId );
        layout.addShowViewShortcut( TranslateChatRoomView.ID );
        layout.getViewLayout(TranslateChatRoomView.ID).setCloseable(false);
        
        layout.addPlaceholder(AgendaView.ID, IPageLayout.TOP, 0.35f, TranslateChatRoomView.ID);  
        layout.getViewLayout(AgendaView.ID).setCloseable(false);
        
        
        //adding the TranslateM2Mview == messageBoard
        layout.addPlaceholder( TranslateM2Mview.ID, IPageLayout.RIGHT, 0.5f, editorAreaId );
        layout.getViewLayout(TranslateM2Mview.ID).setCloseable(false);
        
        
        //adding the backlog view (this view has to be located under the ChatRoomView and the WhiteBoardView)
        layout.addPlaceholder(TranslateM2MHandRaiseView.ID, IPageLayout.BOTTOM, 0.7f, TranslateM2Mview.ID);  
        layout.getViewLayout(TranslateM2MHandRaiseView.ID).setCloseable(false);
        
        //adding the whiteboard view
        layout.addPlaceholder(TranslateWhiteBoardView.ID, IPageLayout.RIGHT,0.7f, TranslateM2Mview.ID);
        layout.getViewLayout(TranslateWhiteBoardView.ID).setCloseable(false);
        
        
        
        
        
        layout.addPerspectiveShortcut( ID );
	}

}
