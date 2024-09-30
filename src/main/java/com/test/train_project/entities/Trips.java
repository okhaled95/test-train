package com.test.train_project.entities;

import com.google.gson.JsonObject;

public class Trips
{

    private final String stationStart;
    private final String stationEnd;
    private final long startedJourneyAt;
    private final int costInCents;
    private final int zoneFrom;
    private final int zoneTo;

    public Trips(String stationStart, String stationEnd, long startedJourneyAt, int costInCents, int zoneFrom,
                 int zoneTo) {
      this.stationStart = stationStart;
      this.stationEnd = stationEnd;
      this.startedJourneyAt = startedJourneyAt;
      this.costInCents = costInCents;
      this.zoneFrom = zoneFrom;
      this.zoneTo = zoneTo;
    }

    public JsonObject toJson() {
      JsonObject json = new JsonObject();
      json.addProperty("stationStart", stationStart);
      json.addProperty("stationEnd", stationEnd);
      json.addProperty("startedJourneyAt", startedJourneyAt);
      json.addProperty("costInCents", costInCents);
      json.addProperty("zoneFrom", zoneFrom);
      json.addProperty("zoneTo", zoneTo);
      return json;
    }
  }

