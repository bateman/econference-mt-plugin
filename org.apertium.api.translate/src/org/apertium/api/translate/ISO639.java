package org.apertium.api.translate;

import java.util.*;

public class ISO639 {
	private Map<String, String> codeToLanguage = null;
	private Map<String, String> languageToCode = null;
	
	public ISO639() {
		codeToLanguage = new HashMap<String, String>();
		
		codeToLanguage.put("xh", "Xhosa");
		codeToLanguage.put("nr", "Ndebele, South; South Ndebele");
		codeToLanguage.put("no", "Norwegian");
		codeToLanguage.put("nn", "Norwegian Nynorsk; Nynorsk, Norwegian");
		codeToLanguage.put("fy", "Western Frisian");
		codeToLanguage.put("gd", "Gaelic; Scottish Gaelic");
		codeToLanguage.put("ny", "Chichewa; Chewa; Nyanja");
		codeToLanguage.put("ga", "Irish");
		codeToLanguage.put("nv", "Navajo; Navaho");
		codeToLanguage.put("fj", "Fijian");
		codeToLanguage.put("oc", "Occitan (post 1500)");
		codeToLanguage.put("ff", "Fulah");
		codeToLanguage.put("fi", "Finnish");
		codeToLanguage.put("om", "Oromo");
		codeToLanguage.put("fr", "French");
		codeToLanguage.put("fo", "Faroese");
		codeToLanguage.put("oj", "Ojibwa");
		codeToLanguage.put("os", "Ossetian; Ossetic");
		codeToLanguage.put("or", "Oriya");
		codeToLanguage.put("wo", "Wolof");
		codeToLanguage.put("he", "Hebrew");
		codeToLanguage.put("ha", "Hausa");
		codeToLanguage.put("gn", "Guarani");
		codeToLanguage.put("gl", "Galician");
		codeToLanguage.put("pa", "Panjabi; Punjabi");
		codeToLanguage.put("pl", "Polish");
		codeToLanguage.put("gv", "Manx");
		codeToLanguage.put("gu", "Gujarati");
		codeToLanguage.put("wa", "Walloon");
		codeToLanguage.put("pi", "Pali");
		codeToLanguage.put("lo", "Lao");
		codeToLanguage.put("ln", "Lingala");
		codeToLanguage.put("dv", "Divehi; Dhivehi; Maldivian");
		codeToLanguage.put("vi", "Vietnamese");
		codeToLanguage.put("dz", "Dzongkha");
		codeToLanguage.put("lv", "Latvian");
		codeToLanguage.put("lu", "Luba-Katanga");
		codeToLanguage.put("lt", "Lithuanian");
		codeToLanguage.put("vo", "Volapük");
		codeToLanguage.put("de", "German");
		codeToLanguage.put("uz", "Uzbek");
		codeToLanguage.put("ve", "Venda");
		codeToLanguage.put("mg", "Malagasy");
		codeToLanguage.put("mh", "Marshallese");
		codeToLanguage.put("mk", "Macedonian");
		codeToLanguage.put("ml", "Malayalam");
		codeToLanguage.put("mi", "Maori");
		codeToLanguage.put("mn", "Mongolian");
		codeToLanguage.put("uk", "Ukrainian");
		codeToLanguage.put("eu", "Basque");
		codeToLanguage.put("mo", "Moldavian; Moldovan");
		codeToLanguage.put("mr", "Marathi");
		codeToLanguage.put("ug", "Uighur; Uyghur");
		codeToLanguage.put("mt", "Maltese");
		codeToLanguage.put("ms", "Malay");
		codeToLanguage.put("ur", "Urdu");
		codeToLanguage.put("fa", "Persian");
		codeToLanguage.put("my", "Burmese");
		codeToLanguage.put("ty", "Tahitian");
		codeToLanguage.put("na", "Nauru");
		codeToLanguage.put("ee", "Ewe");
		codeToLanguage.put("nb", "Bokmål, Norwegian; Norwegian Bokmål");
		codeToLanguage.put("el", "Greek, Modern (1453-)");
		codeToLanguage.put("tt", "Tatar");
		codeToLanguage.put("tw", "Twi");
		codeToLanguage.put("nd", "Ndebele, North; North Ndebele");
		codeToLanguage.put("ne", "Nepali");
		codeToLanguage.put("ng", "Ndonga");
		codeToLanguage.put("eo", "Esperanto");
		codeToLanguage.put("en", "English");
		codeToLanguage.put("et", "Estonian");
		codeToLanguage.put("es", "Spanish; Castilian");
		codeToLanguage.put("nl", "Dutch; Flemish");
		codeToLanguage.put("to", "Tonga (Tonga Islands)");
		codeToLanguage.put("tn", "Tswana");
		codeToLanguage.put("jv", "Javanese");
		codeToLanguage.put("tl", "Tagalog");
		codeToLanguage.put("ca", "Catalan; Valencian");
		codeToLanguage.put("ts", "Tsonga");
		codeToLanguage.put("tr", "Turkish");
		codeToLanguage.put("tg", "Tajik");
		codeToLanguage.put("te", "Telugu");
		codeToLanguage.put("tk", "Turkmen");
		codeToLanguage.put("bs", "Bosnian");
		codeToLanguage.put("br", "Breton");
		codeToLanguage.put("ti", "Tigrinya");
		codeToLanguage.put("th", "Thai");
		codeToLanguage.put("bn", "Bengali");
		codeToLanguage.put("kj", "Kuanyama; Kwanyama");
		codeToLanguage.put("bo", "Tibetan");
		codeToLanguage.put("ki", "Kikuyu; Gikuyu");
		codeToLanguage.put("kg", "Kongo");
		codeToLanguage.put("ta", "Tamil");
		codeToLanguage.put("bm", "Bambara");
		codeToLanguage.put("su", "Sundanese");
		codeToLanguage.put("sv", "Swedish");
		codeToLanguage.put("bg", "Bulgarian");
		codeToLanguage.put("ka", "Georgian");
		codeToLanguage.put("ss", "Swati");
		codeToLanguage.put("bh", "Bihari");
		codeToLanguage.put("st", "Sotho, Southern");
		codeToLanguage.put("bi", "Bislama");
		codeToLanguage.put("sw", "Swahili");
		codeToLanguage.put("be", "Belarusian");
		codeToLanguage.put("sl", "Slovenian");
		codeToLanguage.put("kw", "Cornish");
		codeToLanguage.put("sk", "Slovak");
		codeToLanguage.put("sn", "Shona");
		codeToLanguage.put("da", "Danish");
		codeToLanguage.put("ky", "Kirghiz; Kyrgyz");
		codeToLanguage.put("sm", "Samoan");
		codeToLanguage.put("ks", "Kashmiri");
		codeToLanguage.put("so", "Somali");
		codeToLanguage.put("sr", "Serbian");
		codeToLanguage.put("ku", "Kurdish");
		codeToLanguage.put("sq", "Albanian");
		codeToLanguage.put("kv", "Komi");
		codeToLanguage.put("sd", "Sindhi");
		codeToLanguage.put("ko", "Korean");
		codeToLanguage.put("cy", "Welsh");
		codeToLanguage.put("sc", "Sardinian");
		codeToLanguage.put("se", "Northern Sami");
		codeToLanguage.put("kr", "Kanuri");
		codeToLanguage.put("cv", "Chuvash");
		codeToLanguage.put("kk", "Kazakh");
		codeToLanguage.put("sg", "Sango");
		codeToLanguage.put("cu", "Church Slavic; Old Slavonic; Church Slavonic; Old Bulgarian; Old Church Slavonic");
		codeToLanguage.put("kl", "Kalaallisut; Greenlandic");
		codeToLanguage.put("km", "Central Khmer");
		codeToLanguage.put("si", "Sinhala; Sinhalese");
		codeToLanguage.put("cs", "Czech");
		codeToLanguage.put("kn", "Kannada");
		codeToLanguage.put("li", "Limburgan; Limburger; Limburgish");
		codeToLanguage.put("cr", "Cree");
		codeToLanguage.put("co", "Corsican");
		codeToLanguage.put("sa", "Sanskrit");
		codeToLanguage.put("lg", "Ganda");
		codeToLanguage.put("la", "Latin");
		codeToLanguage.put("ru", "Russian");
		codeToLanguage.put("lb", "Luxembourgish; Letzeburgesch");
		codeToLanguage.put("ch", "Chamorro");
		codeToLanguage.put("ce", "Chechen");
		codeToLanguage.put("rw", "Kinyarwanda");
		codeToLanguage.put("hr", "Croatian");
		codeToLanguage.put("ro", "Romanian");
		codeToLanguage.put("zh", "Chinese");
		codeToLanguage.put("rn", "Rundi");
		codeToLanguage.put("rm", "Romansh");
		codeToLanguage.put("ht", "Haitian; Haitian Creole");
		codeToLanguage.put("hu", "Hungarian");
		codeToLanguage.put("za", "Zhuang; Chuang");
		codeToLanguage.put("hi", "Hindi");
		codeToLanguage.put("ho", "Hiri Motu");
		codeToLanguage.put("id", "Indonesian");
		codeToLanguage.put("ia", "Interlingua (International Auxiliary Language Association)");
		codeToLanguage.put("ig", "Igbo");
		codeToLanguage.put("zu", "Zulu");
		codeToLanguage.put("ie", "Interlingue; Occidental");
		codeToLanguage.put("hz", "Herero");
		codeToLanguage.put("qu", "Quechua");
		codeToLanguage.put("hy", "Armenian");
		codeToLanguage.put("az", "Azerbaijani");
		codeToLanguage.put("is", "Icelandic");
		codeToLanguage.put("ay", "Aymara");
		codeToLanguage.put("it", "Italian");
		codeToLanguage.put("ba", "Bashkir");
		codeToLanguage.put("iu", "Inuktitut");
		codeToLanguage.put("ii", "Sichuan Yi; Nuosu");
		codeToLanguage.put("as", "Assamese");
		codeToLanguage.put("ar", "Arabic");
		codeToLanguage.put("ik", "Inupiaq");
		codeToLanguage.put("av", "Avaric");
		codeToLanguage.put("io", "Ido");
		codeToLanguage.put("ak", "Akan");
		codeToLanguage.put("am", "Amharic");
		codeToLanguage.put("an", "Aragonese");
		codeToLanguage.put("pt", "Portuguese");
		codeToLanguage.put("yo", "Yoruba");
		codeToLanguage.put("aa", "Afar");
		codeToLanguage.put("ab", "Abkhazian");
		codeToLanguage.put("ae", "Avestan");
		codeToLanguage.put("ja", "Japanese");
		codeToLanguage.put("yi", "Yiddish");
		codeToLanguage.put("ps", "Pushto; Pashto");
		codeToLanguage.put("af", "Afrikaans");
		
		languageToCode = new HashMap<String, String>();
		
		for (String code : codeToLanguage.keySet()) {
			languageToCode.put(codeToLanguage.get(code), code);
		}
	}
	
	public Set<String> getLanguages() {
		return languageToCode.keySet();
	}
	
	public String getCode(String language) {
		String ret = null;
		if (languageToCode.containsKey(language)) {
			ret = languageToCode.get(language);
		}
		return ret;
	}
	
	public Set<String> getCodes() {
		return codeToLanguage.keySet();
	}
	
	public String getLanguage(String code) {
		String ret = null;
		if (codeToLanguage.containsKey(code)) {
			ret = codeToLanguage.get(code);
		}
		return ret;
	}
}
