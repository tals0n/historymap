// actor admin

function addMap(webSocket, url, img, copyright, anno, scale, mapcoordNwLon, mapcoordNwLat, mapcoordNeLon, mapcoordNeLat, mapcoordSwLon, mapcoordSwLat, mapcoordSeLon, mapcoordSeLat, description, locations) {
	var obj = {};
	obj.AddMap = {};
	obj.AddMap.url = url;
	obj.AddMap.img = img;
	obj.AddMap.copyright = copyright;
	obj.AddMap.anno = anno;
	obj.AddMap.scale = scale;
	obj.AddMap.mapcoordNwLon = mapcoordNwLon;
	obj.AddMap.mapcoordNwLat = mapcoordNwLat;
	obj.AddMap.mapcoordNeLon = mapcoordNeLon;
	obj.AddMap.mapcoordNeLat = mapcoordNeLat;
	obj.AddMap.mapcoordSwLon = mapcoordSwLon;
	obj.AddMap.mapcoordSwLat = mapcoordSwLat;
	obj.AddMap.mapcoordSeLon = mapcoordSeLon;
	obj.AddMap.mapcoordSeLat = mapcoordSeLat;
	obj.AddMap.description = description;
	obj.AddMap.locations = locations;
	webSocket.send(JSON.stringify(obj));
	return false;
}

function modifyMap(webSocket, url, img, copyright, anno, scale, mapcoordNwLon, mapcoordNwLat, mapcoordNeLon, mapcoordNeLat, mapcoordSwLon, mapcoordSwLat, mapcoordSeLon, mapcoordSeLat, description, mapId, locations) {
	var obj = {};
	obj.ModifyMap = {};
	obj.ModifyMap.url = url;
	obj.ModifyMap.img = img;
	obj.ModifyMap.copyright = copyright;
	obj.ModifyMap.anno = anno;
	obj.ModifyMap.scale = scale;
	obj.ModifyMap.mapcoordNwLon = mapcoordNwLon;
	obj.ModifyMap.mapcoordNwLat = mapcoordNwLat;
	obj.ModifyMap.mapcoordNeLon = mapcoordNeLon;
	obj.ModifyMap.mapcoordNeLat = mapcoordNeLat;
	obj.ModifyMap.mapcoordSwLon = mapcoordSwLon;
	obj.ModifyMap.mapcoordSwLat = mapcoordSwLat;
	obj.ModifyMap.mapcoordSeLon = mapcoordSeLon;
	obj.ModifyMap.mapcoordSeLat = mapcoordSeLat;
	obj.ModifyMap.description = description;
	obj.ModifyMap.mapId = mapId;
	obj.ModifyMap.locations = locations;
	webSocket.send(JSON.stringify(obj));
	return false;
}


function removeMap(webSocket, mapId) {
	var obj = {};
	obj.RemoveMap = {};
	obj.RemoveMap.mapId = mapId;
	webSocket.send(JSON.stringify(obj));
	return false;
}

function addLocation(webSocket, locationName, lon, lat, description) {
	var obj = {};
	obj.AddLocation = {};
	obj.AddLocation.locationName = locationName;
	obj.AddLocation.lon = lon;
	obj.AddLocation.lat = lat;
	obj.AddLocation.description = description;
	webSocket.send(JSON.stringify(obj));
	return false;
}

function modifyLocation(webSocket, locationId, locationName, lon, lat, description) {
	var obj = {};
	obj.ModifyLocation = {};
	obj.ModifyLocation.locationId = locationId;
	obj.ModifyLocation.locationName = locationName;
	obj.ModifyLocation.lon = lon;
	obj.ModifyLocation.lat = lat;
	obj.ModifyLocation.description = description;
	webSocket.send(JSON.stringify(obj));
	return false;
}

function removeLocation(webSocket, locationId) {
	var obj = {};
	obj.RemoveLocation = {};
	obj.RemoveLocation.locationId = locationId;
	webSocket.send(JSON.stringify(obj));
	return false;
}

function getLocationsLike(webSocket, likeLocationName) {
	var obj = {};
	obj.GetLocationsLike = {};
	obj.GetLocationsLike.likeLocationName = likeLocationName;
	webSocket.send(JSON.stringify(obj));
	return false;
}

function getMapById(webSocket, mapId) {
	var obj = {};
	obj.GetMapById = {};
	obj.GetMapById.mapId = mapId;
	webSocket.send(JSON.stringify(obj));
	return false;
}

function getLocationById(webSocket, locationId) {
	var obj = {};
	obj.GetLocationById = {};
	obj.GetLocationById.locationId = locationId;
	webSocket.send(JSON.stringify(obj));
	return false;
}

// TODO

function getMapsByCoords(webSocket, lon, lat, offset, limit) {
	var obj = {};
	obj.GetMapsByCoords = {};
	obj.GetMapsByCoords.lon = lon;
	obj.GetMapsByCoords.lat = lat;
	obj.GetMapsByCoords.offset = offset;
	obj.GetMapsByCoords.limit = limit;
	webSocket.send(JSON.stringify(obj));
	return false;
}

// actor user

function getMapsByLocationName(webSocket, locationName, offset, limit) {
	var obj = {};
	obj.GetMapsByLocationName = {};
	obj.GetMapsByLocationName.locationName = locationName;
	obj.GetMapsByLocationName.offset = offset;
	obj.GetMapsByLocationName.limit = limit;
	webSocket.send(JSON.stringify(obj));
	return false;
}