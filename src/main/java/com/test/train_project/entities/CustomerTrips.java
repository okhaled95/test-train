package com.test.train_project.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class CustomerTrips {
  private final int customerId;
  private final List<Trips> trips = new ArrayList<>();
  private int totalCostInCents = 0;

  public CustomerTrips(int customerId) {
    this.customerId = customerId;
  }

  public void addTrip(String stationStart, String stationEnd, long startedJourneyAt, int costInCents, int zoneFrom, int zoneTo) {
    trips.add(new Trips(stationStart, stationEnd, startedJourneyAt, costInCents, zoneFrom, zoneTo));
    totalCostInCents += costInCents;
  }

  public JsonObject toJson() {
    JsonObject json = new JsonObject();
    json.addProperty("customerId", customerId);
    json.addProperty("totalCostInCents", totalCostInCents);

    JsonArray tripsJson = new JsonArray();
    for (Trips trip : trips) {
      tripsJson.add(trip.toJson());
    }
    json.add("trips", tripsJson);
    return json;
  }


}
