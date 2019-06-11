package historymap.api.data;

public class RequestGetMapsByPhytagoras {
	private Float lon;
	private Float lat;
	private Float distance;
	private Integer fromAnno;
	private Integer toAnno;
	
	public RequestGetMapsByPhytagoras(Float lon, Float lat, Float distance, Integer fromAnno, Integer toAnno) {
		this.lon = lon;
		this.lat = lat;
		this.distance = distance;
		this.fromAnno = fromAnno;
		this.toAnno = toAnno;
	}

	public Float getLon() {
		return lon;
	}

	public void setLon(Float lon) {
		this.lon = lon;
	}

	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public Float getDistance() {
		return distance;
	}

	public void setDistance(Float distance) {
		this.distance = distance;
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