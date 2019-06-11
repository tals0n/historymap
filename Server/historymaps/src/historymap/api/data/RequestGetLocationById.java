package historymap.api.data;

public class RequestGetLocationById {
	private String locationId;

	public RequestGetLocationById(String locationId) {
		this.locationId = locationId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
}