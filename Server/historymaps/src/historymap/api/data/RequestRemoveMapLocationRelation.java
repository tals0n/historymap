package historymap.api.data;

public class RequestRemoveMapLocationRelation {
	private String mapId;
	private String locationId;
	
	public RequestRemoveMapLocationRelation(String mapId, String locationId) {
		this.mapId = mapId;
		this.locationId = locationId;
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
}