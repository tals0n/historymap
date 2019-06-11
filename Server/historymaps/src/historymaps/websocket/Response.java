package historymaps.websocket;

public class Response {
	private Status status;
	private Integer for_request;
	private String message;
	private Object data;
	
	public enum Status {
		ok, failed
	}
	
	public Response(int request, Object data) {
		this.status = Status.ok;
		this.message = null;
		this.data = data;
		this.for_request = request;
	}
	
	public Response(int request, String error) {
		this.status = Status.failed;
		this.message = error;
		this.data = null;
		this.for_request = request;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}

	public Integer getRequest() {
		return for_request;
	}

	public void setRequest(Integer request) {
		this.for_request = request;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}