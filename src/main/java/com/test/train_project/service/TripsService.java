package com.test.train_project.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.test.train_project.entities.CustomerTrips;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class TripsService {

  private static final int BASE_COST_1_2 = 240; // Zone 1 to 2
  private static final int BASE_COST_3_4 = 200; // Zone 3 to 4
  private static final int BASE_COST_3_1_2 = 280; // Zone 3 to 1 or 2
  private static final int BASE_COST_4_1_2 = 300; // Zone 4 to 1 or 2
  private static final int BASE_COST_1_3 = 280; // Zone 1 or 2 to Zone 3
  private static final int BASE_COST_2_4 = 300; // Zone 2 to Zone 4

  private final Map<Integer, CustomerTrips> customerTripsMap = new HashMap<>();

  public String processTaps(String input) throws IOException {
    Gson gson = new Gson();
    JsonObject inputJson = gson.fromJson(input, JsonObject.class);

    if (!inputJson.has("taps") || !inputJson.get("taps").isJsonArray()) {
      throw new JsonSyntaxException("Expected 'taps' array in JSON input");
    }

    JsonArray taps = inputJson.getAsJsonArray("taps");

    for (int i = 0; i < taps.size(); i += 2) {
      if (i + 1 >= taps.size()) {
        throw new JsonSyntaxException("Missing exit tap for customerId: " +
                                      taps.get(i).getAsJsonObject().get("customerId").getAsInt());
      }

      JsonObject entryTap = taps.get(i).getAsJsonObject();
      JsonObject exitTap = taps.get(i + 1).getAsJsonObject();

      int customerId = entryTap.get("customerId").getAsInt();
      String stationStart = entryTap.get("station").getAsString();
      String stationEnd = exitTap.get("station").getAsString();
      long unixTimestamp = entryTap.get("unixTimestamp").getAsLong();

      int zoneFrom = determineZone(stationStart);
      int zoneTo = determineZone(stationEnd);
      int costInCents = calculateFare(zoneFrom, zoneTo);

      customerTripsMap.putIfAbsent(customerId, new CustomerTrips(customerId));
      customerTripsMap.get(customerId).addTrip(stationStart, stationEnd, unixTimestamp, costInCents, zoneFrom, zoneTo);
    }

    return generateOutput();
  }

  private int determineZone(String station) {
    switch (station) {
      case "A":
      case "B":
        return 1;
      case "C":
      case "D":
      case "E":
        return 2;
      case "F":
        return 3;
      case "G":
      case "H":
      case "I":
        return 4;
      default:
        throw new JsonSyntaxException("Unknown station: " + station);
    }
  }

  private int calculateFare(int zoneFrom, int zoneTo) {
    if (zoneFrom == 1 && zoneTo == 2) return BASE_COST_1_2;
    if (zoneFrom == 3 && zoneTo == 4) return BASE_COST_3_4;
    if ((zoneFrom == 1 || zoneFrom == 2) && zoneTo == 3) return BASE_COST_1_3;
    if (zoneFrom == 4 && (zoneTo == 1 || zoneTo == 2)) return BASE_COST_4_1_2;
    if (zoneFrom == 2 && zoneTo == 4) return BASE_COST_2_4;
    // Add more conditions based on the pricing rules
    return 0; // Default case
  }

  private String generateOutput() {
    Gson gson = new Gson();
    JsonObject outputJson = new JsonObject();
    JsonArray customerSummaries = new JsonArray();

    for (CustomerTrips summary : customerTripsMap.values()) {
      customerSummaries.add(summary.toJson());
    }

    outputJson.add("customerSummaries", customerSummaries);
    return gson.toJson(outputJson);
  }
}
