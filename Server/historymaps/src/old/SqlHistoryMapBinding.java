package old;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import historymap.api.data.StructLocation;
import historymap.api.data.StructMap;

public class SqlHistoryMapBinding implements HistoryMapBinding {

	@SuppressWarnings("unused")
	private Connection connection;
	
	public SqlHistoryMapBinding(SqlAuthenticationData sqlAuthenticationData) throws RuntimeException {
		initiateSqlDriver();
		connect(sqlAuthenticationData);
	}
	
	private void initiateSqlDriver() throws RuntimeException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private void connect(SqlAuthenticationData sqlAuthenticationData) {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://" + sqlAuthenticationData.host + ":" + sqlAuthenticationData.port + "/" + sqlAuthenticationData.database + "", sqlAuthenticationData.userName, sqlAuthenticationData.password);
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public String addMap(StructMap map, ArrayList<String> locationIds) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modifyMap(String mapId, StructMap map) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeMap(String mapId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String addLocation(StructLocation location) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modifyLocation(String locationId, StructLocation location) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeLocation(String locationId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addMapLocationRelation(String mapId, String locationId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeMapLocationRelation(String mapId, String locationId) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<StructLocation> getLocations(float lon, float lat, float distance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<StructLocation> getLocations(String locationName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<StructMap> getMaps(float lon, float lat, float distance) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<StructMap> getMaps(String locationName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<StructMap> getMaps(int fromAnno, int toAnno) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
