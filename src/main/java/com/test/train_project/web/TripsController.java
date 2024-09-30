package com.test.train_project.web;



import com.test.train_project.service.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/trip")
public class TripsController {

  private final TripsService   tripsService;
  @Autowired
  private       ResourceLoader resourceLoader;
  public TripsController(TripsService tripsService) {
    this.tripsService = tripsService;

  }

  @PostMapping("/process")
  public String processTaps(@RequestParam String name) throws IOException
  {
    Path   path  = resourceLoader.getResource("classpath:ressources/" + name).getFile().toPath();
    String input = Files.readString(path);
    try {
      return tripsService.processTaps(input);
    } catch (IOException e) {
      return "Error  " + e.getMessage();
    }
  }
}
