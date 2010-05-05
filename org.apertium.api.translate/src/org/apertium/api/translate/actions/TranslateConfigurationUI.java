package org.apertium.api.translate.actions;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.SWT;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class TranslateConfigurationUI extends org.eclipse.swt.widgets.Composite {
	private Label MTServiceLabel;
	private Combo MTServiceCombo;
	private Group MTServiceConfigurationGroup;
	private Combo DestinationLanguageCombo;
	private Combo SourceLanguageCombo;
	private Label DestinationLanguageLabel;
	private Label SourceLanguageLabel;
	private Button CancelButton;
	private Button OkButton;
	private Button ApplyButton;
	private Text MTServiceURLText;
	private Label MTServiceURLLabel;

	/**
	* Auto-generated main method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void main(String[] args) {
		showGUI();
	}
	
	/**
	* Overriding checkSubclass allows this class to extend org.eclipse.swt.widgets.Composite
	*/	
	protected void checkSubclass() {
	}
	
	/**
	* Auto-generated method to display this 
	* org.eclipse.swt.widgets.Composite inside a new Shell.
	*/
	public static void showGUI() {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		TranslateConfigurationUI inst = new TranslateConfigurationUI(shell, SWT.NULL);
		Point size = inst.getSize();
		shell.setLayout(new FillLayout());
		shell.layout();
		if(size.x == 0 && size.y == 0) {
			inst.pack();
			shell.pack();
		} else {
			Rectangle shellBounds = shell.computeTrim(0, 0, size.x, size.y);
			shell.setSize(shellBounds.width, shellBounds.height);
		}
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public TranslateConfigurationUI(org.eclipse.swt.widgets.Composite parent, int style) {
		super(parent, style);
		initGUI();
	}

	private void initGUI() {
		try {
			FormLayout thisLayout = new FormLayout();
			this.setLayout(thisLayout);
			{
				CancelButton = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData CancelButtonLData = new FormData();
				CancelButtonLData.left =  new FormAttachment(0, 1000, 345);
				CancelButtonLData.top =  new FormAttachment(0, 1000, 161);
				CancelButtonLData.width = 55;
				CancelButtonLData.height = 29;
				CancelButton.setLayoutData(CancelButtonLData);
				CancelButton.setText("Cancel");
			}
			{
				OkButton = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData OkButtonLData = new FormData();
				OkButtonLData.left =  new FormAttachment(0, 1000, 310);
				OkButtonLData.top =  new FormAttachment(0, 1000, 161);
				OkButtonLData.width = 29;
				OkButtonLData.height = 29;
				OkButton.setLayoutData(OkButtonLData);
				OkButton.setText("Ok");
			}
			{
				ApplyButton = new Button(this, SWT.PUSH | SWT.CENTER);
				FormData ApplyButtonLData = new FormData();
				ApplyButtonLData.left =  new FormAttachment(0, 1000, 257);
				ApplyButtonLData.top =  new FormAttachment(0, 1000, 161);
				ApplyButtonLData.width = 47;
				ApplyButtonLData.height = 29;
				ApplyButton.setLayoutData(ApplyButtonLData);
				ApplyButton.setText("Apply");
			}
			{
				MTServiceConfigurationGroup = new Group(this, SWT.NONE);
				GridLayout MTServiceConfigurationGroupLayout = new GridLayout();
				MTServiceConfigurationGroupLayout.makeColumnsEqualWidth = true;
				MTServiceConfigurationGroupLayout.numColumns = 2;
				MTServiceConfigurationGroup.setLayout(MTServiceConfigurationGroupLayout);
				FormData MTServiceConfigurationGroupLData = new FormData();
				MTServiceConfigurationGroupLData.left =  new FormAttachment(0, 1000, 0);
				MTServiceConfigurationGroupLData.width = 399;
				MTServiceConfigurationGroupLData.height = 136;
				MTServiceConfigurationGroupLData.top =  new FormAttachment(0, 1000, 0);
				MTServiceConfigurationGroupLData.right =  new FormAttachment(1000, 1000, 0);
				MTServiceConfigurationGroupLData.bottom =  new FormAttachment(1000, 1000, -41);
				MTServiceConfigurationGroup.setLayoutData(MTServiceConfigurationGroupLData);
				MTServiceConfigurationGroup.setText("MT Service Configuration");
				{
					MTServiceLabel = new Label(MTServiceConfigurationGroup, SWT.NONE);
					GridData MTServiceLabelLData = new GridData();
					MTServiceLabel.setLayoutData(MTServiceLabelLData);
					MTServiceLabel.setText("MT Service:");
				}
				{
					MTServiceCombo = new Combo(MTServiceConfigurationGroup, SWT.NONE);
					GridData MTServiceComboLData = new GridData();
					MTServiceComboLData.horizontalAlignment = GridData.END;
					MTServiceComboLData.widthHint = 191;
					MTServiceComboLData.heightHint = 29;
					MTServiceCombo.setLayoutData(MTServiceComboLData);
				}
				{
					MTServiceURLLabel = new Label(MTServiceConfigurationGroup, SWT.NONE);
					GridData MTServiceURLLabelLData = new GridData();
					MTServiceURLLabel.setLayoutData(MTServiceURLLabelLData);
					MTServiceURLLabel.setText("MT Service URL:");
				}
				{
					GridData MTServiceURLTextLData = new GridData();
					MTServiceURLTextLData.widthHint = 185;
					MTServiceURLTextLData.heightHint = 17;
					MTServiceURLText = new Text(MTServiceConfigurationGroup, SWT.NONE);
					MTServiceURLText.setLayoutData(MTServiceURLTextLData);
				}
				{
					SourceLanguageLabel = new Label(MTServiceConfigurationGroup, SWT.NONE);
					GridData SourceLanguageLabelLData = new GridData();
					SourceLanguageLabel.setLayoutData(SourceLanguageLabelLData);
					SourceLanguageLabel.setText("Source Language:");
				}
				{
					GridData SourceLanguageComboLData = new GridData();
					SourceLanguageCombo = new Combo(MTServiceConfigurationGroup, SWT.NONE);
					SourceLanguageCombo.setLayoutData(SourceLanguageComboLData);
				}
				{
					DestinationLanguageLabel = new Label(MTServiceConfigurationGroup, SWT.NONE);
					GridData DestinationLanguageLabelLData = new GridData();
					DestinationLanguageLabel.setLayoutData(DestinationLanguageLabelLData);
					DestinationLanguageLabel.setText("DestinationLanguage:");
				}
				{
					GridData DestinationLanguageComboLData = new GridData();
					DestinationLanguageCombo = new Combo(MTServiceConfigurationGroup, SWT.NONE);
					DestinationLanguageCombo.setLayoutData(DestinationLanguageComboLData);
				}
			}
			this.layout();
			pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
