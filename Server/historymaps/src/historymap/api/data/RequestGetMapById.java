package historymap.api.data;

public class RequestGetMapById {
	private String mapId;

	public RequestGetMapById(String mapId) {
		this.mapId = mapId;
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
}