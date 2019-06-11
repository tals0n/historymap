	var overlay;
	var position;
	var range;
    dynamicOverlay.prototype = new google.maps.OverlayView();
	
      function initMap() {
        var map = new google.maps.Map(document.getElementById('googleMap'), {
          zoom: 11,
          center: {lat:swLng, lng:swLat},
          mapTypeId: 'hybrid'
        });
		var image = './img/marker.png';
		locationList.forEach(function(entry){
			var latlng = new google.maps.LatLng(entry.lat, entry.lon);
			var marker = new google.maps.Marker({
				position: latlng,
				map: map,
				icon: image
			});
			var contentString = '<div id="content" style="width:400px;">'+'<p><u><b><h4>'+entry.name+'</u></p></b></h4>'+'<hr>'+entry.description+'</hr>'+'</div>';
			//div style="background-color:#00CED1;" !important class="jumbotron">'+'<div style= "color:#F8F8FF;" class="container mt-1"><h1>'+entry.name+'</h1></div>'+'</div>'+
			marker.addListener('click', function() {
				infowindow.open(map, marker);
			});	
			var infowindow = new google.maps.InfoWindow({
				content: contentString
			});
		});
		
		
        var bounds = new google.maps.LatLngBounds(
            new google.maps.LatLng(swLng, swLat),	// SW
            new google.maps.LatLng(neLng, neLat));		// NE
        var srcImage = mapUrl;
        overlay = new dynamicOverlay(bounds, srcImage, map);
      }

      /** @constructor */
      function dynamicOverlay(bounds, image, map) {
        this.bounds_ = bounds;
        this.image_ = image;
        this.map_ = map;
        this.div_ = null;
        this.setMap(map);
      }

      /**
       * onAdd is called when the map's panes are ready and the overlay has been
       * added to the map.
       */
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
		img.style.opacity = 100;
        div.appendChild(img);
        this.div_ = div;
        var panes = this.getPanes();
        panes.overlayLayer.appendChild(div);
      };

      dynamicOverlay.prototype.draw = function() {
        var dynamicOverlay = this.getProjection();
        var sw = dynamicOverlay.fromLatLngToDivPixel(this.bounds_.getSouthWest());
        var ne = dynamicOverlay.fromLatLngToDivPixel(this.bounds_.getNorthEast());
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

      google.maps.event.addDomListener(window, 'load', initMap);
	  