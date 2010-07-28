package org.apertium.api.translate.actions;

import it.uniba.di.cdg.xcore.network.NetworkPlugin;

import java.util.Set;
import java.util.TreeSet;

import org.apertium.api.translate.ISO639;
import org.apertium.api.translate.Language;
import org.apertium.api.translate.LanguagePair;
import org.apertium.api.translate.Services;
import org.apertium.api.translate.Services.ServiceType;
import org.apertium.api.translate.TranslatePlugin;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class TranslateConfigurationDialog extends TitleAreaDialog {

	private static final String CONFIGURATION_NODE_QUALIFIER = "it.uniba.di.cdg.xcore.econference.mt";
	private static final String MT_CONFIG_PATH_NODE = "mt_";
	private static final String MT_SERVICE_PROVIDER = "mt_service_provider";
	private static final String MT_SERVICE_URL = "mt_service_url";
	private static final String MT_USER_LANGUAGE = "mt_user_language";
	private static final String MT_PREFERENCES_HASH = "mt_preference_hash";
	private Services services = null;
	private ISO639 iso = null;

	private Combo mtServiceProviderCombo;
	private Combo languagesCombo;
	private Text mtServiceUrl;
	private String mtServiceUrlString = "";
	int mtServiceSelectionIndex = -1;
	int languageSelectionIndex = -1;

	public TranslateConfigurationDialog() {
		super(null);
		System.out.println("TranslateConfigDialog()");
		services = new Services();
		iso = new ISO639();
	}

	@Override
	public void create() {
		super.create();
		// Set the title
		setTitle("Machine Translation Configuration");
		// Set the message
		setMessage("Choose the MT service to use and your own language",
				IMessageProvider.INFORMATION);

	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		// layout.horizontalAlignment = GridData.FILL;
		parent.setLayout(layout);

		// The text fields will grow with the size of the dialog
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;

		Label mtServiceProviderLabel = new Label(parent, SWT.NONE);
		mtServiceProviderLabel.setText("MT Service Provider");
		mtServiceProviderCombo = new Combo(parent, SWT.READ_ONLY);
		mtServiceProviderCombo.setLayoutData(gridData);
		mtServiceProviderCombo.setItems(createServiceModel());
		mtServiceProviderCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				mtServiceSelectionIndex = mtServiceProviderCombo
						.getSelectionIndex();
				checkUrl();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// nothing
			}
		});
		Label mtServiceUrlLabel = new Label(parent, SWT.NONE);
		mtServiceUrlLabel.setText("Url");
		mtServiceUrl = new Text(parent, SWT.BORDER);
		mtServiceUrl.setLayoutData(gridData);
		mtServiceUrl.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				mtServiceUrlString = mtServiceUrl.getText();
			}

			@Override
			public void focusGained(FocusEvent e) {
				// Nothing
			}
		});

		Label languageLabel = new Label(parent, SWT.NONE);
		languageLabel.setText("Your own language");
		languagesCombo = new Combo(parent, SWT.READ_ONLY);
		languagesCombo.setLayoutData(gridData);
		languagesCombo.setItems(createLanguageModel());
		languagesCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				languageSelectionIndex = languagesCombo.getSelectionIndex();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// Nothing
			}
		});

		loadProperties();
		return parent;

	}

	private void saveProperties() {
		TranslateConfiguration data = getData();
		TranslateConfiguration configuration = TranslatePlugin.getDefault()
				.getConfiguration();

		System.out.println("TranslateConfigurationDialog.saveProperties(): "
				+ data.getService());

		configuration.setService(data.getService());
		configuration.setLangPair(data.getLangPair());
		configuration.setUrl(data.getUrl());

		System.out.println("Storing MT preferences");
		Preferences preferences = new ConfigurationScope()
				.getNode(CONFIGURATION_NODE_QUALIFIER);
		String userAccountId = NetworkPlugin.getDefault().getRegistry()
				.getDefaultBackend().getUserId();
		System.out.println("STORE: id: " + userAccountId);

		Preferences connections = preferences.node(MT_CONFIG_PATH_NODE
				+ userAccountId);
		System.out.println("STORE: path node:" + MT_CONFIG_PATH_NODE
				+ userAccountId);

		Preferences connection = connections.node(MT_PREFERENCES_HASH);

		connection.putInt(MT_SERVICE_PROVIDER, configuration.getService()
				.ordinal());
		connection.put(MT_SERVICE_URL, configuration.getUrl());
		connection.put(MT_USER_LANGUAGE, configuration.getLangPair()
				.getSrcLang().getCode());
		try {
			connections.flush();
		} catch (BackingStoreException e) {
			System.err
					.println("Error storing MT preferences. Next time defaults will be used.");
			e.printStackTrace();
		}

	}

	private void loadProperties() {
		System.out.println("TranslateConfigDialog.loadProperties()");
		// loads default
		TranslateConfiguration configuration = TranslatePlugin.getDefault()
				.getConfiguration();

		System.out.println("Loading entries");
		Preferences preferences = new ConfigurationScope()
				.getNode(CONFIGURATION_NODE_QUALIFIER);
		String id = NetworkPlugin.getDefault().getRegistry()
				.getDefaultBackend().getUserId();

		Preferences connections = preferences.node(MT_CONFIG_PATH_NODE + id);
		System.out.println("LOAD: path node: " + MT_CONFIG_PATH_NODE + id);
		Preferences node = connections.node(MT_PREFERENCES_HASH);

		System.out.println("LOAD: path node: " + node);

		int mtServiceProvider = node.getInt(MT_SERVICE_PROVIDER, -1);
		if (-1 != mtServiceProvider) {
			ServiceType serviceType = services
					.getServiceType(mtServiceProvider);
			configuration.setService(serviceType);
		}
		String mtServiceUrl = node.get(MT_SERVICE_URL, null);
		if (null != mtServiceUrl)
			configuration.setUrl(mtServiceUrl);
		String mtUserlLanguageCode = node.get(MT_USER_LANGUAGE, null);
		if (null != mtUserlLanguageCode) {
			Language userLang = new Language(mtUserlLanguageCode);
			LanguagePair lp = new LanguagePair(userLang, null);
			configuration.setLangPair(lp);
		}

		setData(configuration);
		checkUrl();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.CENTER;

		parent.setLayoutData(gridData);
		// Create Ok button
		Button okButton = createButton(parent, OK, "Save", true);
		// Add a SelectionListener
		okButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (isValidInput()) {
					okPressed();
				}
			}
		});

		// Create Cancel button
		Button cancelButton = createButton(parent, CANCEL, "Cancel", false);
		// Add a SelectionListener
		cancelButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setReturnCode(CANCEL);
				close();
			}
		});
	}

	private boolean isValidInput() {
		return mtServiceUrl.equals("") ? false : true;
	}

	// We allow the user to resize this dialog
	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void okPressed() {
		saveProperties();
		super.okPressed();
	}

	private void checkUrl() {
		try {
			String selected = mtServiceProviderCombo
					.getItem(mtServiceProviderCombo.getSelectionIndex());
			Services.ServiceType ser = services.getServiceType(selected);

			if (ser == Services.ServiceType.APERTIUM) {
				mtServiceUrl.setEditable(true);
				mtServiceUrl.setEnabled(true);
			} else {
				mtServiceUrl.setEditable(false);
				mtServiceUrl.setEnabled(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String[] setToArray(Set<String> set) {
		String[] ret = new String[set.size()];
		set.toArray(ret);
		return ret;
	}

	private String[] createServiceModel() {
		Set<String> items = null;

		System.out.println("TranslateConfigurationForm.createServiceModel()");

		try {
			items = services.getServices();
		} catch (Exception e) {
			items = new TreeSet<String>();
		}

		String[] ret = setToArray(items);
		java.util.Arrays.sort(ret);
		return ret;
	}

	private String[] createLanguageModel() {
		Set<String> items;
		try {
			items = iso.getLanguages();
		} catch (Exception e) {
			items = new TreeSet<String>();
		}
		String[] ret = setToArray(items);
		java.util.Arrays.sort(ret);
		return ret;
	}

	private void setData(TranslateConfiguration data) {

		System.out.println("TranslateConfigurationForm.setData()");

		if (data.getLangPair() != null) {
			String[] model = languagesCombo.getItems();
			boolean ok = false;

			for (int i = 0; i < model.length && !ok; i++) {
				String item = (String) model[i];

				if (item.equals(data.getLangPair().getSrcLang().getName())) {
					System.out.println("Received "
							+ data.getLangPair().getSrcLang().getName());
					languagesCombo.select(i);
					languageSelectionIndex = i;
					System.out.println("Set "
							+ languagesCombo.getItem(languagesCombo
									.getSelectionIndex()));
					ok = true;
				}
			}

		}

		if (data.getService() != null) {
			String[] model = mtServiceProviderCombo.getItems();
			boolean ok = false;

			for (int i = 0; i < model.length && !ok; i++) {
				String item = (String) model[i];
				if (item.equals(services.getService(data.getService()))) {
					System.out.println("Received "
							+ data.getService());
					mtServiceProviderCombo.select(i);
					mtServiceSelectionIndex = i;
					System.out.println("Set "
							+ mtServiceProviderCombo.getItem(mtServiceProviderCombo
									.getSelectionIndex()));
					ok = true;
				}
			}
		}

		if (data.getUrl() != null) {
			mtServiceUrl.setText(data.getUrl().trim());
			mtServiceUrlString = mtServiceUrl.getText();
			System.out.println("Set " + mtServiceUrlString);
		}
	}

	private TranslateConfiguration getData() {
		TranslateConfiguration ret = new TranslateConfiguration();
		System.out.println("TranslateConfigurationForm.getData()");
		
		if (mtServiceSelectionIndex != -1) {
			String[] servicesArray = (String[]) createServiceModel();
			String serviceSelectedItem = (String) servicesArray[mtServiceSelectionIndex];
			System.out.println("Selected " + serviceSelectedItem);
			ret.setService(services.getServiceType(serviceSelectedItem));
		}

		if (languageSelectionIndex != -1) {
			String[] langArray = createLanguageModel();
			String srcSelectedItem = langArray[languageSelectionIndex];
			System.out.println("Selected " + srcSelectedItem);
			ISO639 iso = new ISO639();
			// our language
			Language src = new Language(iso.getCode(srcSelectedItem));
			// the others' language, we don't know it upfront
			Language dest = null;
			LanguagePair pair = new LanguagePair(src, dest);
			ret.setLangPair(pair);
		}

		if (mtServiceUrl != null) {
			ret.setUrl(mtServiceUrlString);
			System.out.println("Selected " + mtServiceUrlString);
		}

		return ret;
	}

	public static void main(String[] args) {
		TranslateConfigurationDialog c = new TranslateConfigurationDialog();
		c.open();
	}
}