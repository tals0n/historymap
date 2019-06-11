package historymap.api.data;

public class RequestRemoveLocation {
	private String locationId;

	public RequestRemoveLocation(String locationId) {
		this.locationId = locationId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
}