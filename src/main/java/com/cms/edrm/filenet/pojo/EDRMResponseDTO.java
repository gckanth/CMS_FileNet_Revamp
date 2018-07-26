package com.cms.edrm.filenet.pojo;

/*
 * POJO class to return FileNet response
 */
public class EDRMResponseDTO {

	private String responseCode;
	private String responseMessage;
	private String exceptionCode;
	private String exceptionMessage;
	
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getExceptionCode() {
		return exceptionCode;
	}
	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
	/*
	 * POJO class Constructor to Read properties and return Object
	 */
	public EDRMResponseDTO(String responseCode, String responseMessage, String exceptionCode,
			String exceptionMessage) {
		super();
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.exceptionCode = exceptionCode;
		this.exceptionMessage = exceptionMessage;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * return POJO Class response
	 */
	@Override
	public String toString() {
		if(exceptionCode!=null && exceptionMessage!=null)
		{
		return "FilenetResultDTO [responseCode=" + responseCode + ", responseMessage=" + responseMessage
				+ ", exceptionCode=" + exceptionCode + ", exceptionMessage=" + exceptionMessage + "]";
		}
		else
		{
		return "FilenetResultDTO [responseCode=" + responseCode + ", responseMessage=" + responseMessage + "]";
		}
	}
	/*
	 * POJO class zero parameterized constructor
	 */
	public EDRMResponseDTO() {
		super();
	}
}
