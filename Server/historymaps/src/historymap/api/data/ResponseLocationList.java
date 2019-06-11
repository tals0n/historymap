package historymap.api.data;

import java.util.ArrayList;

public class ResponseLocationList {
	private ArrayList<StructLocation> locationData;

	public ResponseLocationList(ArrayList<StructLocation> locationData) {
		this.locationData = locationData;
	}

	public ArrayList<StructLocation> getLocationData() {
		return locationData;
	}

	public void setLocationData(ArrayList<StructLocation> locationData) {
		this.locationData = locationData;
	}
}
