package historymaps.api;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletContext;

import com.google.gson.Gson;

import historymap.api.data.RequestAddLocation;
import historymap.api.data.RequestAddMap;
import historymap.api.data.RequestGetLocationById;
import historymap.api.data.RequestGetLocationsLike;
import historymap.api.data.RequestGetMapById;
import historymap.api.data.RequestGetMapsByLocationName;
import historymap.api.data.RequestModifyLocation;
import historymap.api.data.RequestModifyMap;
import historymap.api.data.RequestRemoveLocation;
import historymap.api.data.RequestRemoveMap;
import historymap.api.data.ResponseId;
import historymap.api.data.ResponseLocationList;
import historymap.api.data.ResponseMap;
import historymap.api.data.ResponseMapList;
import historymap.api.data.StructLocation;
import historymaps.websocket.HistoryMapWebsocket.ApiCommand;

public class HistoryMapApi {
	private static Connection connection;
	private static HistoryMapApi INSTANCE;
	
	private HistoryMapApi(ServletContext servletContext) {
		Gson gson = new Gson();
		SqlAuthenticationData sqlAuthenticationData = gson.fromJson(new InputStreamReader(servletContext.getResourceAsStream("/WEB-INF/sql.conf")), SqlAuthenticationData.class);
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://" + sqlAuthenticationData.host + ":" + sqlAuthenticationData.port + "/" + sqlAuthenticationData.database + "", sqlAuthenticationData.userName, sqlAuthenticationData.password);
			executeSqlScriptFromWebInf(servletContext, "initiateDB.sql");
		} catch (ClassNotFoundException | SQLException | IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	private static void executeSqlScriptFromWebInf(ServletContext servletContext, String file) throws SQLException, IOException {
		InputStreamReader inputStreamReader = new InputStreamReader(servletContext.getResourceAsStream("/WEB-INF/" + file));
		ScriptRunner scriptRunner = new ScriptRunner(connection, false, false);
		scriptRunner.runScript(inputStreamReader);
		scriptRunner.setErrorLogWriter(new PrintWriter(System.err));
		scriptRunner.setLogWriter(new PrintWriter(System.out));
	}
	
	public static HistoryMapApi getHistoryMapApi(ServletContext servletContext) {
		if(INSTANCE == null) {
			INSTANCE = new HistoryMapApi(servletContext);
		}
		return INSTANCE;
	}

	private static String idToString(long id) {
		return "" + id;
	}
	
	private static int stringToId(String string) throws Exception {
		try {
			return Integer.parseInt(string);
		} catch(RuntimeException ex) {
			throw new Exception("id format not valid");
		}
	}
	
	@ApiCommand(descriptor = "AddLocation")
	public ResponseId addLocation(RequestAddLocation requestAddLocation) throws Exception {
		if(requestAddLocation.getLat() == null || requestAddLocation.getLon() == null || requestAddLocation.getLocationName() == null) {
			throw new Exception("lat, lon, locationName, cannot be null");
		}
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO history_location (name, lon, lat, description) VALUES (?, ?, ?, ?) RETURNING id");
			preparedStatement.setString(1, requestAddLocation.getLocationName());
			preparedStatement.setFloat(2, requestAddLocation.getLon());
			preparedStatement.setFloat(3, requestAddLocation.getLat());
			preparedStatement.setString(4, requestAddLocation.getDescription());
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet != null && resultSet.next()) {
				ResponseId responseId = new ResponseId(idToString(resultSet.getLong(1)));
				return responseId;
			} else {
				// TODO: log this crap
				throw new Exception("internal error");
			}
		} catch (SQLException ex) {
			ex.printStackTrace(); // TODO: log this crap
			throw new Exception("internal db error: " + ex.getSQLState());
		}
	}
	
	@ApiCommand(descriptor = "AddMap")
	public ResponseId addMap(RequestAddMap requestAddMap) throws Exception {
		if(requestAddMap.getImg() == null) {
			throw new Exception("img cannot be null");
		}
		try {
			PreparedStatement preparedStatementAddMap = connection.prepareStatement("INSERT INTO history_map (url, img, copyright, anno, scale, mapcoord_nw_lon, mapcoord_nw_lat, mapcoord_ne_lon, mapcoord_ne_lat, mapcoord_sw_lon, mapcoord_sw_lat, mapcoord_se_lon, mapcoord_se_lat, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id");
			preparedStatementAddMap.setString(1, requestAddMap.getUrl());
			preparedStatementAddMap.setString(2, requestAddMap.getImg());
			preparedStatementAddMap.setBoolean(3, requestAddMap.getCopyright());
			preparedStatementAddMap.setInt(4, requestAddMap.getAnno());
			preparedStatementAddMap.setFloat(5, requestAddMap.getScale());
			preparedStatementAddMap.setFloat(6, requestAddMap.getMapcoordNwLon());
			preparedStatementAddMap.setFloat(7, requestAddMap.getMapcoordNwLat());
			preparedStatementAddMap.setFloat(8, requestAddMap.getMapcoordNeLon());
			preparedStatementAddMap.setFloat(9, requestAddMap.getMapcoordNeLat());
			preparedStatementAddMap.setFloat(10, requestAddMap.getMapcoordSwLon());
			preparedStatementAddMap.setFloat(11, requestAddMap.getMapcoordSwLat());
			preparedStatementAddMap.setFloat(12, requestAddMap.getMapcoordSeLon());
			preparedStatementAddMap.setFloat(13, requestAddMap.getMapcoordSeLat());
			preparedStatementAddMap.setString(14, requestAddMap.getDescription());
			ResultSet resultSet = preparedStatementAddMap.executeQuery();
			if(resultSet != null && resultSet.next()) {
				String mapID = idToString(resultSet.getInt(1));
				PreparedStatement preparedStatementAddLocations = connection.prepareStatement("INSERT INTO history_map_location (map_id, location_id) VALUES (?, ?)");
				for (String location: requestAddMap.getLocations()) {
					preparedStatementAddLocations.setInt(1, stringToId(mapID));
					preparedStatementAddLocations.setInt(2, stringToId(location));
					preparedStatementAddLocations.addBatch();
				}
				preparedStatementAddLocations.executeBatch();
				return new ResponseId(mapID);
			} else {
				// TODO: log this crap
				throw new Exception("internal error");
			}
		} catch (SQLException ex) {
			if(ex.getSQLState().equals("23503")) {
				throw new Exception("one or more maps or locations with given ids does not exists");
			} else if(ex.getSQLState().equals("23505")) {
				throw new Exception("one or more maps and locations are already linked");
			} else {
				ex.printStackTrace(); // TODO: log this crap
				throw new Exception("internal db error: " + ex.getSQLState());
			}
		}
	}
	
	@ApiCommand(descriptor = "ModifyMap")
	public Void modifyMap(RequestModifyMap requestModifyMap) throws Exception {
		if(requestModifyMap.getImg() == null) {
			throw new Exception("img cannot be null");
		}
		try {
			String mapID = requestModifyMap.getMapId();
			PreparedStatement preparedStatementReplaceMap = connection.prepareStatement("UPDATE history_map SET url = ?, img = ?, copyright = ?, anno = ?, scale = ?, mapcoord_nw_lon = ?, mapcoord_nw_lat = ?, mapcoord_ne_lon = ?, mapcoord_ne_lat = ?, mapcoord_sw_lon = ?, mapcoord_sw_lat = ?, mapcoord_se_lon = ?, mapcoord_se_lat = ?, description = ? WHERE id = ?");
			preparedStatementReplaceMap.setString(1, requestModifyMap.getUrl());
			preparedStatementReplaceMap.setString(2, requestModifyMap.getImg());
			preparedStatementReplaceMap.setBoolean(3, requestModifyMap.getCopyright());
			preparedStatementReplaceMap.setInt(4, requestModifyMap.getAnno());
			preparedStatementReplaceMap.setFloat(5, requestModifyMap.getScale());
			preparedStatementReplaceMap.setFloat(6, requestModifyMap.getMapcoordNwLon());
			preparedStatementReplaceMap.setFloat(7, requestModifyMap.getMapcoordNwLat());
			preparedStatementReplaceMap.setFloat(8, requestModifyMap.getMapcoordNeLon());
			preparedStatementReplaceMap.setFloat(9, requestModifyMap.getMapcoordNeLat());
			preparedStatementReplaceMap.setFloat(10, requestModifyMap.getMapcoordSwLon());
			preparedStatementReplaceMap.setFloat(11, requestModifyMap.getMapcoordSwLat());
			preparedStatementReplaceMap.setFloat(12, requestModifyMap.getMapcoordSeLon());
			preparedStatementReplaceMap.setFloat(13, requestModifyMap.getMapcoordSeLat());
			preparedStatementReplaceMap.setString(14, requestModifyMap.getDescription());
			preparedStatementReplaceMap.setInt(15, stringToId(requestModifyMap.getMapId()));
			preparedStatementReplaceMap.execute();
			PreparedStatement preparedStatementDeleteLocations = connection.prepareStatement("DELETE FROM history_map_location WHERE map_id = ?");
			preparedStatementDeleteLocations.setInt(1, stringToId(mapID));
			preparedStatementDeleteLocations.execute();
			PreparedStatement preparedStatementAddLocations = connection.prepareStatement("INSERT INTO history_map_location (map_id, location_id) VALUES (?, ?)");
			for (String location: requestModifyMap.getLocations()) {
				preparedStatementAddLocations.setInt(1, stringToId(mapID));
				preparedStatementAddLocations.setInt(2, stringToId(location));
				preparedStatementAddLocations.addBatch();
			}
			preparedStatementAddLocations.executeBatch();
		} catch (SQLException ex) {
			if(ex.getSQLState().equals("23503")) {
				throw new Exception("one or more maps or locations with given ids does not exists");
			} else if(ex.getSQLState().equals("23505")) {
				throw new Exception("one or more maps and locations are already linked");
			} else {
				ex.printStackTrace(); // TODO: log this crap
				throw new Exception("internal db error: " + ex.getSQLState());
			}
		}
		return null;
	}

	@ApiCommand(descriptor = "GetLocationsLike")
	public ResponseLocationList getLocationsLike(RequestGetLocationsLike requestGetLocationsLike) throws Exception {
		ArrayList<StructLocation> structsLocations = new ArrayList<StructLocation>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM history_location WHERE name LIKE ?"); // Where History_'map_location != Null ?? 
			preparedStatement.setString(1, "%" + requestGetLocationsLike.getLikeLocationName() + "%");
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				String id = "" + resultSet.getInt("id");
				String url = resultSet.getString("name");
				Float lon = resultSet.getFloat("lon");
				Float lat = resultSet.getFloat("lat");
				String description = resultSet.getString("description");
				structsLocations.add(new StructLocation(id, url, lon, lat, description));
			}
		} catch (SQLException ex) {
			ex.printStackTrace(); // TODO: log this crap
			throw new Exception("internal db error: " + ex.getSQLState());
		}
		return new ResponseLocationList(structsLocations);
	}

	@ApiCommand(descriptor = "GetMapsByLocationName")
	public ResponseMapList getMapsByLocationName(RequestGetMapsByLocationName requestGetMapsByLocationName) throws Exception {
		HashMap<String, ResponseMapList.ResponseMapListElement> hashMap = new HashMap<String, ResponseMapList.ResponseMapListElement>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM (SELECT DISTINCT history_map.id AS map_id, history_map.url AS url, history_map.img AS img, history_map.copyright AS copyright, history_map.anno AS anno, history_map.scale AS scale,history_map.mapcoord_nw_lon AS mapcoord_nw_lon, history_map.mapcoord_nw_lat AS mapcoord_nw_lat,history_map.mapcoord_ne_lon AS mapcoord_ne_lon, history_map.mapcoord_ne_lat AS mapcoord_ne_lat,history_map.mapcoord_sw_lon AS mapcoord_sw_lon, history_map.mapcoord_sw_lat AS mapcoord_sw_lat, history_map.mapcoord_se_lon AS mapcoord_se_lon, history_map.mapcoord_se_lat AS mapcoord_se_lat, history_map.description AS description, history_location.name AS location_name, history_location.id AS location_id FROM history_location, history_map_location, history_map, (SELECT DISTINCT history_map.id AS id FROM history_location, history_map_location, history_map WHERE UPPER(history_location.name) LIKE ? AND history_map.id = history_map_location.map_id AND history_location.id = history_map_location.location_id) AS onmap WHERE onmap.id = history_map.id AND history_map.id = history_map_location.map_id AND history_location.id = history_map_location.location_id) AS results LIMIT ? OFFSET ?");
			preparedStatement.setString(1, "%" + requestGetMapsByLocationName.getLocationName().toUpperCase() + "%");
			preparedStatement.setInt(2, requestGetMapsByLocationName.getLimit());
			preparedStatement.setInt(3, requestGetMapsByLocationName.getOffset());
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				String id = "" + resultSet.getInt("map_id");
				String locationId = resultSet.getString("location_id");
				String locationName = resultSet.getString("location_name");
				ResponseMapList.ResponseMapListElement map = hashMap.get(id);
				if(map == null) {
					String url = resultSet.getString("url");
					String img = resultSet.getString("img");
					Boolean copyright = resultSet.getBoolean("copyright");
					Integer anno = resultSet.getInt("anno");
					Float scale = resultSet.getFloat("scale");
					Float mapcoordNwLon = resultSet.getFloat("mapcoord_nw_lon");
					Float mapcoordNwLat = resultSet.getFloat("mapcoord_nw_lat");
					Float mapcoordNeLon = resultSet.getFloat("mapcoord_ne_lon");
					Float mapcoordNeLat = resultSet.getFloat("mapcoord_ne_lat");
					Float mapcoordSwLon = resultSet.getFloat("mapcoord_sw_lon");
					Float mapcoordSwLat = resultSet.getFloat("mapcoord_sw_lat");
					Float mapcoordSeLon = resultSet.getFloat("mapcoord_se_lon");
					Float mapcoordSeLat = resultSet.getFloat("mapcoord_se_lat");
					String description = resultSet.getString("description");
					map = new ResponseMapList.ResponseMapListElement(id, url, img, copyright, anno, scale,mapcoordNwLon, mapcoordNwLat, mapcoordNeLon, mapcoordNeLat, mapcoordSwLon, mapcoordSwLat,mapcoordSeLon, mapcoordSeLat, description, new ArrayList<ResponseMapList.ResponseMapListElement.Location>());
					hashMap.put(id, map);
				}
				map.getLocations().add(new ResponseMapList.ResponseMapListElement.Location(locationId, locationName));
			}
		} catch (SQLException ex) {
			ex.printStackTrace(); // TODO: log this crap
			throw new Exception("internal db error: " + ex.getSQLState());
		}
		return new ResponseMapList(new ArrayList<ResponseMapList.ResponseMapListElement>(hashMap.values()));
	}
		
	@ApiCommand(descriptor = "GetMapById")
	public ResponseMap getMapById(RequestGetMapById requestGetMapById) throws Exception {
		ResponseMap responseMap = null;
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT history_map.id AS map_id, history_map.url AS url, history_map.img AS img, history_map.copyright AS copyright, history_map.anno AS anno, history_map.scale AS scale, history_map.mapcoord_nw_lon AS mapcoord_nw_lon, history_map.mapcoord_nw_lat AS mapcoord_nw_lat, history_map.mapcoord_ne_lon AS mapcoord_ne_lon, history_map.mapcoord_ne_lat AS mapcoord_ne_lat, history_map.mapcoord_sw_lon AS mapcoord_sw_lon, history_map.mapcoord_sw_lat AS mapcoord_sw_lat, history_map.mapcoord_se_lon AS mapcoord_se_lon, history_map.mapcoord_se_lat AS mapcoord_se_lat, history_map.description AS description, history_location.name AS location_name, history_location.id AS location_id, history_location.lat AS location_lat, history_location.lon AS location_lon, history_location.description AS location_description FROM history_location, history_map_location, history_map, (SELECT DISTINCT history_map.id AS id FROM history_location, history_map_location, history_map WHERE history_map.id = ? AND history_map.id = history_map_location.map_id AND history_location.id = history_map_location.location_id) AS onmap WHERE onmap.id = history_map.id AND history_map.id = history_map_location.map_id AND history_location.id = history_map_location.location_id");preparedStatement.setInt(1, stringToId(requestGetMapById.getMapId()));
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				if(responseMap == null) {
					String id = "" + resultSet.getInt("map_id");
					String url = resultSet.getString("url");
					String img = resultSet.getString("img");
					Boolean copyright = resultSet.getBoolean("copyright");
					Integer anno = resultSet.getInt("anno");
					Float scale = resultSet.getFloat("scale");
					Float mapcoordNwLon = resultSet.getFloat("mapcoord_nw_lon");
					Float mapcoordNwLat = resultSet.getFloat("mapcoord_nw_lat");
					Float mapcoordNeLon = resultSet.getFloat("mapcoord_ne_lon");
					Float mapcoordNeLat = resultSet.getFloat("mapcoord_ne_lat");
					Float mapcoordSwLon = resultSet.getFloat("mapcoord_sw_lon");
					Float mapcoordSwLat = resultSet.getFloat("mapcoord_sw_lat");
					Float mapcoordSeLon = resultSet.getFloat("mapcoord_se_lon");
					Float mapcoordSeLat = resultSet.getFloat("mapcoord_se_lat");
					String description = resultSet.getString("description");
					responseMap = new ResponseMap(id, url, img, copyright, anno, scale,mapcoordNwLon, mapcoordNwLat ,mapcoordNeLon, mapcoordNeLat, mapcoordSwLon, mapcoordSwLat, mapcoordSeLon, mapcoordSeLat, description, new ArrayList<ResponseMap.Location>());
				}
				String locationId = resultSet.getString("location_id");
				String locationName = resultSet.getString("location_name");
				Float locationLon = resultSet.getFloat("location_lon");
				Float locationLat = resultSet.getFloat("location_lat");
				String locationDescription = resultSet.getString("location_description");
				responseMap.getLocations().add(new ResponseMap.Location(locationId, locationName, locationLon, locationLat, locationDescription));
			}
		} catch (SQLException ex) {
			ex.printStackTrace(); // TODO: log this crap
			throw new Exception("internal db error: " + ex.getSQLState());
		}
		return responseMap;
	}
	
	@ApiCommand(descriptor = "ModifyLocation")
	public Void modifyLocation(RequestModifyLocation requestModifyLocation) throws Exception {
		if(requestModifyLocation.getLocationName() == null) {
			throw new Exception("location name cannot be null");
		}
		try {
			PreparedStatement preparedStatementReplaceLocation = connection.prepareStatement("UPDATE history_location SET name = ?, lon = ?, lat = ?, description = ? WHERE id = ?");
			preparedStatementReplaceLocation.setString(1, requestModifyLocation.getLocationName());
			preparedStatementReplaceLocation.setFloat(2, requestModifyLocation.getLon());
			preparedStatementReplaceLocation.setFloat(3, requestModifyLocation.getLat());
			preparedStatementReplaceLocation.setString(4, requestModifyLocation.getDescription());
			preparedStatementReplaceLocation.setInt(5, stringToId(requestModifyLocation.getLocationId()));
			preparedStatementReplaceLocation.execute();
		} catch (SQLException ex) {
			ex.printStackTrace(); // TODO: log this crap
			throw new Exception("internal db error: " + ex.getSQLState());
		}
		return null;
	}
	
	@ApiCommand(descriptor = "RemoveLocation")
	public Void removeLocation(RequestRemoveLocation requestRemoveLocation) throws Exception {
		try {
			PreparedStatement preparedStatementRemoveLocation = connection.prepareStatement("DELETE FROM history_location WHERE id = ?");
			preparedStatementRemoveLocation.setInt(1, stringToId(requestRemoveLocation.getLocationId()));
			preparedStatementRemoveLocation.execute();
		} catch (SQLException ex) {
			ex.printStackTrace(); // TODO: log this crap
			throw new Exception("internal db error: " + ex.getSQLState());
		}
		return null;
	}
	
	@ApiCommand(descriptor = "RemoveMap")
	public Void removeMap(RequestRemoveMap requestRemoveMap) throws Exception {
		try {
			PreparedStatement preparedStatementRemoveMap = connection.prepareStatement("DELETE FROM history_map WHERE id = ?");
			preparedStatementRemoveMap.setInt(1, stringToId(requestRemoveMap.getMapId()));
			preparedStatementRemoveMap.execute();
		} catch (SQLException ex) {
			ex.printStackTrace(); // TODO: log this crap
			throw new Exception("internal db error: " + ex.getSQLState());
		}
		return null;
	}
	
	@ApiCommand(descriptor = "GetLocationById")
	public StructLocation getLocationById(RequestGetLocationById requestGetLocationById) throws Exception {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM history_location WHERE id = ?");
			preparedStatement.setInt(1, stringToId(requestGetLocationById.getLocationId()));
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				String id = "" + resultSet.getInt("id");
				String name = resultSet.getString("name");
				Float lon = resultSet.getFloat("lon");
				Float lat = resultSet.getFloat("lat");
				String description = resultSet.getString("description");
				return new StructLocation(id, name, lon, lat, description);
			}
		} catch (SQLException ex) {
			ex.printStackTrace(); // TODO: log this crap
			throw new Exception("internal db error: " + ex.getSQLState());
		}
		return null;
	}
	
	/* 
	
	@ApiCommand(descriptor = "AddMapLocationRelation")
	public Void addMapLocationRelation(RequestAddMapLocationRelation requestAddMapLocationRelation) throws Exception {
		if(requestAddMapLocationRelation.getLocationId() == null || requestAddMapLocationRelation.getMapId() == null) {
			throw new Exception("locationId, mapId, cannot be null");
		}
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO history_map_location (map_id, location_id) VALUES (?, ?)");
			preparedStatement.setInt(1, stringToId(requestAddMapLocationRelation.getMapId()));
			preparedStatement.setInt(2, stringToId(requestAddMapLocationRelation.getLocationId()));
			preparedStatement.execute();
			return null;
		} catch (SQLException ex) {
			if(ex.getSQLState().equals("23503")) {
				throw new Exception("map or location with given id not exists");
			} else if(ex.getSQLState().equals("23505")) {
				throw new Exception("map and location already linked");
			} else {
				ex.printStackTrace(); // TODO: log this crap
				throw new Exception("internal db error: " + ex.getSQLState());
			}
		}
	}

	// TODO: im moment gibt uns die methode alle maps zurück (sie soll aber nur die "leeren" zurück geben)
	@ApiCommand(descriptor = "GetEmptyMaps")
	public ResponseMapList getLocations(RequestGetEmptyMaps requestGetEmptyMaps) throws Exception {
		ArrayList<StructMap> structMaps = new ArrayList<StructMap>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM history_map"); // Where History_'map_location != Null ?? 
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				String id = "" + resultSet.getInt("id");
				String url = resultSet.getString("url");
				int anno = resultSet.getInt("anno");
				float scale = resultSet.getFloat("scale");
				String description = resultSet.getString("description");
				structMaps.add(new StructMap(id, url, anno, scale, description));
			}
		} catch (SQLException ex) {
			ex.printStackTrace(); // TODO: log this crap
			throw new Exception("internal db error: " + ex.getSQLState());
		}
		return new ResponseMapList(structMaps);
	}
	
	@ApiCommand(descriptor = "GetLocations")
	public ResponseLocationList getLocations(RequestGetLocations requestGetLocations) throws Exception {
		ArrayList<StructLocation> structLocations = new ArrayList<StructLocation>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM history_location");
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				String id = "" + resultSet.getInt("id");
				String name = resultSet.getString("name");
				float lon = resultSet.getFloat("lon");
				float lat = resultSet.getFloat("lat");
				String description = resultSet.getString("description");
				structLocations.add(new StructLocation(id, name, lon, lat, description));
			}
		} catch (SQLException ex) {
			ex.printStackTrace(); // TODO: log this crap
			throw new Exception("internal db error: " + ex.getSQLState());
		}
		return new ResponseLocationList(structLocations);
	}
	
	@ApiCommand(descriptor = "GetMapLocationsById")
	public ArrayList<StructLocation> getMapById(RequestGetMapLocationsById requestGetMapLocationsById) throws Exception {
		ArrayList<StructLocation> structLocations = new ArrayList<StructLocation>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM history_map_location, history_location WHERE history_map_location.map_id = ? AND history_map_location.location_id = history_location.id");
			preparedStatement.setInt(1, stringToId(requestGetMapLocationsById.getMapId()));
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				String id = "" + resultSet.getInt("id");
				String name = resultSet.getString("name");
				float lon = resultSet.getFloat("lon");
				float lat = resultSet.getFloat("lat");
				String description = resultSet.getString("description");
				structLocations.add(new StructLocation(id, name, lon, lat, description));
			}
			return structLocations;
		} catch (SQLException ex) {
			ex.printStackTrace(); // TODO: log this crap
			throw new Exception("internal db error: " + ex.getSQLState());
		}
	}
	
	@ApiCommand(descriptor = "RemoveLocation")
	public Void removeLocation(RequestRemoveLocation requestRemoveLocation) throws Exception {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM history_location WHERE history_location.id = ?");
			preparedStatement.setInt(1, stringToId(requestRemoveLocation.getLocationId()));
			preparedStatement.execute();
			return null;
		} catch (SQLException ex) {
			if(ex.getSQLState().equals("23503")) {
				throw new Exception("location cannot be removed because its linked to at least one map");
			} else {
				ex.printStackTrace();
				throw new Exception("internal db error: " + ex.getSQLState());
			}
		}
	}
	
	@ApiCommand(descriptor = "RemoveMap")
	public Void removeLocation(RequestRemoveMap requestRemoveMap) throws Exception {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM history_map WHERE history_map.id = ?");
			preparedStatement.setInt(1, stringToId(requestRemoveMap.getMapId()));
			preparedStatement.execute();
			return null;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new Exception("internal db error: " + ex.getSQLState());
		}
	}
	
	@ApiCommand(descriptor = "RemoveMapLocationRelation")
	public Void removeLocation(RequestRemoveMapLocationRelation requestRemoveMapLocationRelation) throws Exception {
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM history_map_location WHERE history_location.map_id = ? AND history_location.location_id = ?");
			preparedStatement.setInt(1, stringToId(requestRemoveMapLocationRelation.getMapId()));
			preparedStatement.setInt(2, stringToId(requestRemoveMapLocationRelation.getLocationId()));
			preparedStatement.execute();
			return null;
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new Exception("internal db error: " + ex.getSQLState());
		}
	}
	
	// Implement 
	
	//GetEmptyMapsByAnno
	@ApiCommand(descriptor = "GetEmptyMapsByAnno")
	public ResponseMapList getLocations(RequestGetEmptyMapsByAnno requestGetEmptyMaps) throws Exception {
		ArrayList<StructMap> structMaps = new ArrayList<StructMap>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM history_map WHERE anno = ?");
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				String id = "" + resultSet.getInt("id");
				String url = resultSet.getString("url");
				int anno = resultSet.getInt("anno");
				float scale = resultSet.getFloat("scale");
				String description = resultSet.getString("description");
				structMaps.add(new StructMap(id, url, anno, scale, description));
			}
		} catch (SQLException ex) {
			ex.printStackTrace(); // TODO: log this crap
			throw new Exception("internal db error: " + ex.getSQLState());
		}
		return new ResponseMapList(structMaps);
	}
	
	//GetLocationsByPhytagoras
	
	//GetMapById
	@ApiCommand(descriptor = "GetMapById")
	public ResponseMapList getLocations(RequestGetMapById requestGetMapById) throws Exception {
		ArrayList<StructMap> structMaps = new ArrayList<StructMap>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM history_map WHERE history_map.id = ?");
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				String id = "" + resultSet.getInt("id");
				String url = resultSet.getString("url");
				int anno = resultSet.getInt("anno");
				float scale = resultSet.getFloat("scale");
				String description = resultSet.getString("description");
				structMaps.add(new StructMap(id, url, anno, scale, description));
			}
		} catch (SQLException ex) {
			ex.printStackTrace(); // TODO: log this crap
			throw new Exception("internal db error: " + ex.getSQLState());
		}
		return new ResponseMapList(structMaps);
	}
	
	
	//GetMapsByLocationName
	@ApiCommand(descriptor = "GetMapsByLocationName")
	public ResponseMapList getLocations(RequestGetMapsByLocationName requestGetMapById) throws Exception {
		ArrayList<StructMap> structMaps = new ArrayList<StructMap>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT *  FROM history_map WHERE  "); // welcher join oder unsauber über geschachtelte anweisung
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				String id = "" + resultSet.getInt("id");
				String url = resultSet.getString("url");
				int anno = resultSet.getInt("anno");
				float scale = resultSet.getFloat("scale");
				String description = resultSet.getString("description");
				structMaps.add(new StructMap(id, url, anno, scale, description));
			}
		} catch (SQLException ex) {
			ex.printStackTrace(); // TODO: log this crap
			throw new Exception("internal db error: " + ex.getSQLState());
		}
		return new ResponseMapList(structMaps);
	}
	
	
	//ModifyLocation
	@ApiCommand(descriptor = "ModifyLocation")
	public ResponseLocationList getLocations(RequestModifyLocation requestGetLocations) throws Exception {
		ArrayList<StructLocation> structLocations = new ArrayList<StructLocation>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE location SET name = ?, lon = ?, lat = ?, description = ?");
			// wie an id ? 2. Statment oder nicht über Update
			ResultSet resultSet = preparedStatement.executeQuery();
			String id = "" + resultSet.getInt("id");
			String name = resultSet.getString("name");
			float lon = resultSet.getFloat("lon");
			float lat = resultSet.getFloat("lat");
			String description = resultSet.getString("description");
			structLocations.add(new StructLocation(id,name, lon, lat, description));			
		} catch (SQLException ex) {
			ex.printStackTrace(); // TODO: log this crap
			throw new Exception("internal db error: " + ex.getSQLState());
		}
		return new ResponseLocationList(structLocations);
	}
	
	*/
	
	
	
	
}
