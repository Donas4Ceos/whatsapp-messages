package mx.com.att.model.vo;

public class WhatsMessageTemplate {
	
	private String name;
	private LanguageMessage language;
	private Components[] components;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LanguageMessage getLanguage() {
		return language;
	}
	public void setLanguage(LanguageMessage language) {
		this.language = language;
	}
	public Components[] getComponents() {
		return components;
	}
	public void setComponents(Components[] components) {
		this.components = components;
	}

}
