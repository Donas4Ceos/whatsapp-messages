package mx.com.att.model.vo;

import java.io.Serializable;

public class RequestMessageSend implements Serializable{
	
	private String plantilla_id;
	private String mdn;
	private String phone_number_id;
	
	
	public String getPlantilla_id() {
		return plantilla_id;
	}


	public void setPlantilla_id(String plantilla_id) {
		this.plantilla_id = plantilla_id;
	}


	public String getMdn() {
		return mdn;
	}


	public void setMdn(String mdn) {
		this.mdn = mdn;
	}


	public String getPhone_number_id() {
		return phone_number_id;
	}


	public void setPhone_number_id(String phone_number_id) {
		this.phone_number_id = phone_number_id;
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
