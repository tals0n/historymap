package historymap.api.data;

public class RequestGetLocationsLike {
	private String likeLocationName;

	public RequestGetLocationsLike(String likeLocationName) {
		this.likeLocationName = likeLocationName;
	}

	public String getLikeLocationName() {
		return likeLocationName;
	}

	public void setLikeLocationName(String likeLocationName) {
		this.likeLocationName = likeLocationName;
	}
}
