package com.test.train_project;

import com.test.train_project.service.TripsService;
import org.junit.jupiter.api.Test;


public class TripsControllerTests {

  private final TripsService tripsService = new TripsService();

  @Test
  public void testProcessTaps() throws Exception {
    String Output = tripsService.processTaps("ressources/inputFile.txt");
   System.out.println(Output);

  }
}
