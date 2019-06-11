
		
		dynamicOverlay.prototype = new google.maps.OverlayView({editable: true});
		  function initMap() {
			var map = new google.maps.Map(document.getElementById('googleWrapper'),{});
			if (navigator.geolocation) {
				navigator.geolocation.getCurrentPosition(function(position) {
				pos = {lat: position.coords.latitude,lng: position.coords.longitude};
				map.setCenter(pos);
				map.setZoom(10);
				
				var triangleCoords = [
				  {lat: pos.lat, lng: pos.lng},
				  {lat: pos.lat+ 0.1, lng: pos.lng},
				  {lat: pos.lat + 0.1, lng: pos.lng + 0.1},
				  {lat: pos.lat, lng: pos.lng+ 0.1},
				  {lat: pos.lat, lng: pos.lng},
				  
				];


				rectangle = new google.maps.Polygon({
					strokeColor: '#DBBC15',
					visible: false,
					paths: triangleCoords,
					/*map: map,
						bounds: {
						north: pos.lat+0.1,
						south: pos.lat,
						east: pos.lng+0.1,
						west: pos.lng
						}*/
						
				});
				var bounds = new google.maps.LatLngBounds(
					new google.maps.LatLng(pos.lat, pos.lat + 0.1),
					new google.maps.LatLng(pos.lng, pos.lng + 0.1));
				
				
				
				$("#img").on("focusout", function(){
					if(typeof $("#url").val() == "string"){
						var test = $("#img").val();
						var re = new RegExp("/(http(s?):)|([/|.|\w|\s])*\.(?:jpg|gif|png)");
							if(re.test(test)){
								$("#img").prop('disabled', true);
								imgValid = true;
								rectangle.setDraggable(true);
								rectangle.setEditable(true);
								rectangle.setVisible(true);
								$("#img").css("background-color", "#00CED1");
								srcImage = $("#img").val();
								overlay = new dynamicOverlay(bounds, srcImage, map);
								overlay.setMap(map);
							}
							else{
								$("#img").css("background-color", "#FA0101");
							}
					}
					else
					{
						$("#img").css("background-color", "#FA0101");
					}
				});
				rectangle.setMap(map);
				rectangle.addListener('dragend', showNewRect);
				
				var image = './img/marker.png';
				var marker = new google.maps.Marker({
					position: pos,
					map: map,
					draggable: true,
					icon: image
				});
				marker.addListener('dragend',SetPositionwithIcon);	


				},function() {handleLocationError(true, infoWindow, map.getCenter());
				});
				} else {
			  
					handleLocationError(false, infoWindow, map.getCenter());
				}
				function handleLocationError(browserHasGeolocation, infoWindow, pos) {
				infoWindow.setPosition(pos);
				infoWindow.setContent(browserHasGeolocation ?
									  'Error: The Geolocation service failed.' :
									  'Error: Your browser doesn\'t support geolocation.');
				}

			function SetPositionwithIcon(event)
			{
				$("#locationLon").val(this.getPosition().lng());
				$("#locationLat").val(this.getPosition().lat());
			}

			function showNewRect(event) {
				
			
				
				bounds = getPolygoncoords(rectangle);
				console.log(bounds);
				
				
				/*var ne = rectangle.getBounds().getNorthEast();
				var sw = rectangle.getBounds().getSouthWest();
				var nw = new google.maps.LatLng(ne.lat(),sw.lng());
				var se = new google.maps.LatLng(ne.lat(),sw.lng());
				nwLat = ne.lat();
				nwLng = sw.lng();
				neLat = ne.lat();
				neLng = ne.lng();
				swLat = sw.lat();
				swLng = sw.lng();
				seLat = se.lat();
				seLng = ne.lng();
				bounds =  new google.maps.LatLngBounds(
					new google.maps.LatLng(swLat,swLng ),
					new google.maps.LatLng(neLat, neLng));*/
				overlay.updateBounds(bounds);	
					
				
			}
			
			
			
		}

		
		
		
		function getPolygoncoords(polygon) {
			var paths = polygon.getPaths();
			var bounds = new google.maps.LatLngBounds();
			paths.forEach(function(path) {
				var ar = path.getArray();
				for(var i = 0, l = ar.length;i < l; i++) {
					bounds.extend(ar[i]);
				}
				
		});return bounds;}
		
		
	
		function dynamicOverlay(bounds, image, map) {
				this.bounds_ = bounds;
				this.image_ = image;
				this.map_ = map;
				this.div_ = null;
				this.setMap(map);
			}
			
		dynamicOverlay.prototype.onAdd = function() {
				var div = document.createElement('div');
				div.setAttribute("id","picture")
				div.style.borderStyle = 'none';
				div.style.borderWidth = '0px';
				div.style.position = 'absolute';
				var img = document.createElement('img');
				img.src = this.image_;
				img.style.width = '100%';
				img.style.height = '100%';
				img.style.position = 'absolute';
				img.style.opacity = 1;
				div.appendChild(img);
				this.div_ = div;
				var panes = this.getPanes();
				panes.overlayLayer.appendChild(div);
		};
			
			
		dynamicOverlay.prototype.draw = function() {

			var overlayProjection = this.getProjection();
			var sw = overlayProjection.fromLatLngToDivPixel(this.bounds_.getSouthWest());
			var ne = overlayProjection.fromLatLngToDivPixel(this.bounds_.getNorthEast());
			var div = this.div_;
			div.style.left = sw.x + 'px';
			div.style.top = ne.y + 'px';
			div.style.width = (ne.x - sw.x) + 'px';
			div.style.height = (sw.y - ne.y) + 'px';
		};
		
		dynamicOverlay.prototype.onRemove = function() {
			this.div_.parentNode.removeChild(this.div_);
			this.div_ = null;
		};
		
		dynamicOverlay.prototype.updateBounds = function(bounds){
			this.bounds_ = bounds;
			this.draw();
		};
		google.maps.event.addDomListener(window, 'load', initMap);
			
