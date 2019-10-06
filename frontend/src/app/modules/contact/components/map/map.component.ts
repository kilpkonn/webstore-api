import {AfterViewInit, Component, OnInit} from '@angular/core';

import OlMap from 'ol/Map';
import OlXYZ from 'ol/source/XYZ';
import OlTileLayer from 'ol/layer/Tile';
import OlView from 'ol/View';
import OlPoint from 'ol/geom/Point';
import OlFeature from 'ol/Feature';
import OlVectorSource from 'ol/source/Vector';
import OlVectorLayer from 'ol/layer/Vector';

import { fromLonLat } from 'ol/proj';

@Component({
  selector: 'contact-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit, AfterViewInit {

  map: OlMap;
  source: OlXYZ;
  layer: OlTileLayer;
  view: OlView;

  marker: OlFeature;
  vectorSource: OlVectorSource;
  vectorLayer: OlVectorLayer;

  private markerLocation = [24.842638, 59.499353];

  constructor() { }

  ngOnInit() {
    /* XYZ */
    this.source = new OlXYZ({
      url: 'http://tile.osm.org/{z}/{x}/{y}.png'
    });

    this.layer = new OlTileLayer({
      source: this.source
    });

    /* Feature and vector */

    // TODO: fancy marker
    this.marker = new OlFeature({
      // Added fromLonLat
      geometry: new OlPoint(fromLonLat(this.markerLocation)),
    });


    this.vectorSource = new OlVectorSource({
      features: [this.marker]
    });

    this.vectorLayer = new OlVectorLayer({
      source: this.vectorSource,
    });

    /* View and map */

    this.view = new OlView({
      center: fromLonLat(this.markerLocation),
      resolutions: this.source.getTileGrid().getResolutions(),
      zoom: 16
    });

    this.map = new OlMap({
      target: 'map',
      layers: [this.layer, this.vectorLayer],
      view: this.view
    });
  }

  ngAfterViewInit() {
    this.map.setTarget('map');
  }
}
