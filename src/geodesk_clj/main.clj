(ns geodesk-clj.main
  "Basic usage of the Java GeoDesk API,
  used for querying and visualizing Open Street Map data."
  (:gen-class)
  (:require
   [geodesk-clj.render :refer [make-leaflet-map]])
  (:import
   [com.geodesk.feature FeatureLibrary]
   [com.geodesk.geom Box]))

;;;;;;;;;;;
;; OSM data
;; Download from openplanetdata (90+ GB!)
;; (def planet-url "https://download.openplanetdata.com/osm/planet/gol/planet-latest.osm.gol")

(def planet-path "./planet-latest.osm.gol")

(defonce planet (FeatureLibrary. planet-path))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Name some generic map "features"
;; using GOQL Query
;; See https://docs.geodesk.com/goql
;; and https://wiki.openstreetmap.org/wiki/Map_features

(def major-cities
  (.select planet "na[place=city][population>=999000]"))

(def bridges
  (.select planet "w[highway][bridge=yes]"))

(def highways
  (.select planet "w[highway][highway=motorway,primary]"))

(def lakes
  (.select planet "na[natural=water]"))

(def cafes
  (.select planet "na[amenity=cafe,coffee]"))

(def thai-restaurants
  (.select planet "na[amenity=restaurant][cuisine=thai]"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; US state bounding boxes
;; approximate to 1/10 degree
(def states
  {:alabama        (Box/ofWSEN  -88.5  30.2  -84.9  35.0)
   :alaska         (Box/ofWSEN -179.1  51.2 -129.9  71.4)
   :arizona        (Box/ofWSEN -114.8  31.3 -109.0  37.0)
   :arkansas       (Box/ofWSEN  -94.6  33.0  -89.6  36.5)
   :california     (Box/ofWSEN -124.5  32.5 -114.1  42.0)
   :colorado       (Box/ofWSEN -109.1  37.0 -102.0  41.0)
   :connecticut    (Box/ofWSEN  -73.7  40.9  -71.8  42.1)
   :delaware       (Box/ofWSEN  -75.8  38.4  -75.0  39.8)
   :florida        (Box/ofWSEN  -87.6  24.4  -80.0  31.0)
   :georgia        (Box/ofWSEN  -85.6  30.4  -80.8  35.0)
   :hawaii         (Box/ofWSEN -178.3  18.9 -154.8  28.4)
   :idaho          (Box/ofWSEN -117.2  42.0 -111.0  49.0)
   :illinois       (Box/ofWSEN  -91.5  37.0  -87.0  42.5)
   :indiana        (Box/ofWSEN  -88.1  37.8  -84.8  41.8)
   :iowa           (Box/ofWSEN  -96.6  40.4  -90.1  43.5)
   :kansas         (Box/ofWSEN -102.1  37.0  -94.6  40.0)
   :kentucky       (Box/ofWSEN  -89.6  36.5  -81.9  39.1)
   :louisiana      (Box/ofWSEN  -94.0  28.9  -88.8  33.0)
   :maine          (Box/ofWSEN  -71.1  43.1  -66.9  47.5)
   :maryland       (Box/ofWSEN  -79.5  37.9  -75.0  39.7)
   :massachusetts  (Box/ofWSEN  -73.5  41.2  -69.9  42.9)
   :michigan       (Box/ofWSEN  -90.4  41.7  -82.1  48.2)
   :minnesota      (Box/ofWSEN  -97.2  43.5  -89.5  49.4)
   :mississippi    (Box/ofWSEN  -91.7  30.2  -88.1  35.0)
   :missouri       (Box/ofWSEN  -95.8  36.0  -89.1  40.6)
   :montana        (Box/ofWSEN -116.1  45.0 -104.0  49.0)
   :nebraska       (Box/ofWSEN -104.1  40.0  -95.3  43.0)
   :nevada         (Box/ofWSEN -120.0  35.0 -114.0  42.0)
   :new-hampshire  (Box/ofWSEN  -72.6  42.7  -70.6  45.3)
   :new-jersey     (Box/ofWSEN  -75.6  38.9  -73.9  41.4)
   :new-mexico     (Box/ofWSEN -109.1  31.3 -103.0  37.0)
   :new-york       (Box/ofWSEN  -79.8  40.5  -71.8  45.0)
   :north-carolina (Box/ofWSEN  -84.3  33.8  -75.5  36.6)
   :north-dakota   (Box/ofWSEN -104.1  45.9  -96.6  49.0)
   :ohio           (Box/ofWSEN  -84.8  38.4  -80.5  42.3)
   :oklahoma       (Box/ofWSEN -103.0  33.6  -94.4  37.0)
   :oregon         (Box/ofWSEN -124.6  41.9 -116.5  46.3)
   :pennsylvania   (Box/ofWSEN  -80.5  39.7  -74.7  42.3)
   :rhode-island   (Box/ofWSEN  -71.9  41.1  -71.1  42.0)
   :south-carolina (Box/ofWSEN  -83.4  32.0  -78.5  35.2)
   :south-dakota   (Box/ofWSEN -104.1  42.5  -96.4  45.9)
   :tennessee      (Box/ofWSEN  -90.3  35.0  -81.6  36.7)
   :texas          (Box/ofWSEN -106.6  25.8  -93.5  36.5)
   :utah           (Box/ofWSEN -114.1  37.0 -109.0  42.0)
   :vermont        (Box/ofWSEN  -73.4  42.7  -71.5  45.0)
   :virginia       (Box/ofWSEN  -83.7  36.5  -75.2  39.5)
   :washington     (Box/ofWSEN -124.8  45.5 -116.9  49.0)
   :west-virginia  (Box/ofWSEN  -82.6  37.2  -77.7  40.6)
   :wisconsin      (Box/ofWSEN  -92.9  42.5  -86.2  47.3)
   :wyoming        (Box/ofWSEN -111.1  41.0 -104.1  45.0)})

(def places
  {:fort-collins (Box/ofWSEN -105.2 40.5 -105.0 40.7)})

;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Make an interactive map
;; opens in web browser

(comment
  ;; without any spatial filter (careful about what you pass here, this is the entire planet!)
  (make-leaflet-map major-cities)

  ;; with a spatial filter, ie Thai restaurants in California
  (make-leaflet-map (.in thai-restaurants (states :california)))

  ;; etc...
  (make-leaflet-map (.in lakes    (states :rhode-island)))
  (make-leaflet-map (.in bridges  (states :louisiana)))
  (make-leaflet-map (.in highways (states :vermont)))
  (make-leaflet-map (.in cafes    (places :fort-collins))))
