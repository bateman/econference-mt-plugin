package org.apertium.api.translate;

public class LanguagePair {
	
	private Language srcLang;
	private Language destLang;
	
	public LanguagePair(Language srcLang, Language destLang) {
		this.srcLang = srcLang;
		this.destLang = destLang;
	}

	public LanguagePair(String src, String dest) {
		this.srcLang = new Language(src);
		this.destLang = new Language(dest);
	}

	
	public Language getSrcLang() {
		return srcLang;
	}

	public void setSrcLang(Language srcLang) {
		this.srcLang = srcLang;
	}

	public Language getDestLang() {
		return destLang;
	}

	public void setdestLang(Language destLang) {
		this.destLang = destLang;
	}
	
	@Override
	public boolean equals(Object aThat) {
		if (this == aThat)
			return true;
		if (!(aThat instanceof LanguagePair))
			return false;
		LanguagePair that = (LanguagePair) aThat;
		boolean ret = srcLang.equals(that.getSrcLang()) && destLang.equals(that.getDestLang());
		return ret;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(srcLang + " - " + destLang);
		return result.toString();
	}
	
	public LanguagePair invert() {
		return new LanguagePair(destLang, srcLang);
	}
}
