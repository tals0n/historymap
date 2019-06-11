package old;

import java.util.ArrayList;

import historymap.api.data.StructLocation;
import historymap.api.data.StructMap;

// abstract-product
public abstract interface HistoryMapBinding {
	// add/modify/remove map
	String addMap(StructMap map, ArrayList<String> locationIds) throws Exception;
	void modifyMap(String mapId, StructMap map) throws Exception;
	void removeMap(String mapId) throws Exception;
	// add/modify/remove location
	String addLocation(StructLocation location) throws Exception;
	void modifyLocation(String locationId, StructLocation location) throws Exception;
	void removeLocation(String locationId) throws Exception;
	// add/remove map-location-relation
	void addMapLocationRelation(String mapId, String locationId) throws Exception;
	void removeMapLocationRelation(String mapId, String locationId) throws Exception;
	// get location/location-list
	ArrayList<StructLocation> getLocations(float lon, float lat, float distance) throws Exception;
	ArrayList<StructLocation> getLocations(String locationName) throws Exception;
	/* for update
	    ArrayList<Location> getLocationsLike(String locationName);
	 */
	// get map/map-list
	ArrayList<StructMap> getMaps(float lon, float lat, float distance) throws Exception;
	ArrayList<StructMap> getMaps(String locationName) throws Exception;
	ArrayList<StructMap> getMaps(int fromAnno, int toAnno) throws Exception;
	/* for update
		ArrayList<Map> getMaps(float lon, float lat, float distance, float scale) throws Exception;
		ArrayList<Map> getMaps(String locationName, float scale) throws Exception;
		ArrayList<Map> getMaps(int fromAnno, int toAnno, float scale) throws Exception;
	*/
}
