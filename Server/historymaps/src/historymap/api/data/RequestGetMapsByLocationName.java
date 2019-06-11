package historymap.api.data;

public class RequestGetMapsByLocationName {
	private String locationName;
	private Integer offset;
	private Integer limit;

	public RequestGetMapsByLocationName(String locationName, Integer offset, Integer limit) {
		this.locationName = locationName;
		this.setOffset(offset);
		this.setLimit(limit);
	}

	public String getLocationName() {
		return locationName;
	}
	
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}
}