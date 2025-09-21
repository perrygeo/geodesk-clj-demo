(ns geodesk-clj.render
  (:require
   [clojure.java.shell :as shell]
   [clojure.string :as string])
  (:import
   [com.geodesk.util MapMaker]))

(defn- launch-in-browswer
  [url]
  ;; on MacOS
  #_(shell/sh "/usr/bin/open" url)
  ;; on Linux
  (shell/sh "xdg-open" url))

(defn- render-tags [feature]
  (-> (.tags feature)
      (.toMap)
      (string/replace "," "<br>")))

(defn make-leaflet-map
  "Use the GeoDesk MapMaker to create a simple, barebones interactive map
  rendering each feature with a tooltip containing all of its tags."
  [features & {:keys [launch] :or {launch true}}]
  (let [map (MapMaker.)
        outpath "/tmp/repl.html"]
    (doseq [f features]
      (-> (.add map f) (.tooltip (render-tags f))))
    (.save map outpath)
    (when launch
      (launch-in-browswer (str "file://" outpath)))
    outpath))
