package mx.com.att.model.vo;

import java.io.Serializable;

public class Contacts implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String input;
	private String wa_id;
	
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getWa_id() {
		return wa_id;
	}
	public void setWa_id(String wa_id) {
		this.wa_id = wa_id;
	}
	

}
