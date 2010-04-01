package org.apertium.api.translate;

public class Language {
	private String name;
	private String code;
	
	//public Language(String name, String code) {
	//	this.name = name;
	//	this.code = code;
	//}
	
	public Language(String code) {
		this.code = code;
		ISO639 iso639 = new ISO639();
		this.name = iso639.getLanguage(code);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	@Override
	public boolean equals(Object aThat) {
		if (this == aThat)
			return true;
		if (!(aThat instanceof Language))
			return false;
		Language that = (Language) aThat;
		boolean ret = name.equals(that.getName()) && code.equals(that.getCode());
		return ret;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(name + " (" + code + ")");
		return result.toString();
	}
}
