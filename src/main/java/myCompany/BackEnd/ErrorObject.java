package myCompany.BackEnd;

import org.springframework.http.HttpStatus;

public class ErrorObject {
	private String message;
    private int status;
    
	public ErrorObject(String string, HttpStatus status) {
		setMessage(string);
		setStatus(status.value());
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
}
