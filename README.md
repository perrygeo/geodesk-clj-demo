# GeoDesk with Clojure

[GeoDesk](https://docs.geodesk.com/why-geodesk) adds a spatial index to the traditional Open Street Map (OSM) data model.
This allows for fast queries directly from a Geographic Object Library (GOL) file.
The queries are stateless with no need to run a spatial database server.

The [Java API](https://docs.geodesk.com/java) already provides some wonderful high-level functionality for querying and visualizing the data.
But for interactive work, the Clojure REPL provides a more dynamic experience.

Let's call the GeoDesk API from Clojure! 

This repo is a demonstration of using Clojure to slice and dice OSM data to create custom interactive maps.

## Data

First, download from `openplanetdata.com` - this is over 90 GB; patience and/or fast internet required!

```
wget https://download.openplanetdata.com/osm/planet/gol/planet-latest.osm.gol
```

## Run the examples

```bash
clj -M:nrepl
```

Then open an nrepl-capable editor, open `src/main.clj`, and start evaluating.
