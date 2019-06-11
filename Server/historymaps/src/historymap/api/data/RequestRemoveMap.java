package historymap.api.data;

public class RequestRemoveMap {
	private String mapId;
	
	public RequestRemoveMap(String mapId) {
		this.mapId = mapId;
	}

	public String getMapId() {
		return mapId;
	}

	public void setMapId(String mapId) {
		this.mapId = mapId;
	}
}