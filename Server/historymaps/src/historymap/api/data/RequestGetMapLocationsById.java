package historymap.api.data;

public class RequestGetMapLocationsById {
	private String mapId;

	public RequestGetMapLocationsById(String mapId) {
		this.mapId = mapId;
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
}
