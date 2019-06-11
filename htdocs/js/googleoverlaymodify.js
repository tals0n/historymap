
		
		dynamicOverlay.prototype = new google.maps.OverlayView({editable: true});
		  function initMap() {

			map = new google.maps.Map(document.getElementById('googleWrapper'),{
			center: {lat:swLng, lng:swLat}
			});
			map.setMapTypeId('hybrid');
			pos = {lat: swLng,lng: swLat};
			var image = './img/marker.png';
			map.setZoom(10);
			locationList.forEach(function(entry){
				var latlng = new google.maps.LatLng(entry.lat, entry.lon);
				var marker = new google.maps.Marker({
					position: latlng,
					map: map,
					icon: image
				});
			var contentString = '<div id="content" style="width:400px; background-color:red;">' + entry.description + '</div>';
			marker.addListener('click', function() {
				infowindow.open(map, marker);
			});	
			var infowindow = new google.maps.InfoWindow({
				content: contentString
				});
			});
			var rectangle = new google.maps.Rectangle({
				strokeColor: '#DBBC15',
				editable: true,
				draggable: true,
				map: map,
					bounds: {
					north: neLng,
					south: swLng,
					east: neLat,
					west: swLat
				}
			});	
			var bounds = new google.maps.LatLngBounds(
				new google.maps.LatLng(swLng, swLat),
				new google.maps.LatLng(neLng, neLat));
			$("#img").on("focusout",function(){
				if(typeof $("#url").val() == "string"){
						var test = $("#img").val();
						var re = new RegExp("/(http(s?):)|([/|.|\w|\s])*\.(?:jpg|gif|png)");
							if(re.test(test)){
								mapUrl = $("#img").val();
								imgValid = true;
								$("#img").css("background-color", "#00CED1");
								overlay.setMap(null);
								overlay = new dynamicOverlay(bounds, mapUrl, map);
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
			overlay = new dynamicOverlay(bounds, mapUrl, map);
			rectangle.addListener('bounds_changed', showNewRect);
			function addOverlay() {
				overlay.setMap(map);
			}
	
			function SetPositionwithIcon(event)
			{
				$("#locationLon").val(this.getPosition().lng());
				$("#locationLat").val(this.getPosition().lat());
			}

			function showNewRect(event) {
				var ne = rectangle.getBounds().getNorthEast();
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
					new google.maps.LatLng(neLat, neLng));
				overlay.updateBounds(bounds);
			}
			
			
		}

		
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
			
