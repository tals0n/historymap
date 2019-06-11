package historymap.api.data;

public class RequestGetMapsByLocationId {
	private String mapId;
	private String locationId;
	private Integer fromAnno;
	private Integer toAnno;
	
	public RequestGetMapsByLocationId(String mapId, String locationId, Integer fromAnno, Integer toAnno) {
		this.mapId = mapId;
		this.locationId = locationId;
		this.fromAnno = fromAnno;
		this.toAnno = toAnno;
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

	public Integer getFromAnno() {
		return fromAnno;
	}

	public void setFromAnno(Integer fromAnno) {
		this.fromAnno = fromAnno;
	}

	public Integer getToAnno() {
		return toAnno;
	}

	public void setToAnno(Integer toAnno) {
		this.toAnno = toAnno;
	}
}