package com.maxruiz.app;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import com.maxruiz.config.ElevatorConfig;
import com.maxruiz.structures.Building;

public class TestBuilding 
{
  int valid_lowestFloor = 0;
  int valid_highestFloor = 5;
  int valid_maxNumPassengersPerFloor = 1;
  ArrayList<ElevatorConfig> valid_elevatorConfigList = new ArrayList<>() {{new ElevatorConfig(valid_lowestFloor, valid_highestFloor); }};
  
  @Test
  void test_badInitFloorsTheSame()
  {
    int invalid_highestFloor = 1; //valid_lowestFloor;
    assertThrows(IllegalArgumentException.class, () ->  
                {new Building(valid_lowestFloor, invalid_highestFloor, valid_maxNumPassengersPerFloor, false, valid_elevatorConfigList); 
                });
  }

  @Test 
  void test_badInitFloorsCrossed()
  {
    int invalid_highestFloor = valid_lowestFloor - 1;
    assertThrows(IllegalArgumentException.class, () ->  
                {new Building(valid_lowestFloor, invalid_highestFloor, valid_maxNumPassengersPerFloor, false, valid_elevatorConfigList); 
                });
  }

  @Test 
  void test_badInitNumPassengersPerFloor()
  {
    int invalid_maxNumPassengersPerFloor = 0;
    ArrayList<ElevatorConfig> invalid_ElevatorConfigs = new ArrayList<>();
    assertThrows(IllegalArgumentException.class, () ->  
                {new Building(valid_lowestFloor, valid_highestFloor, invalid_maxNumPassengersPerFloor, false, invalid_ElevatorConfigs); 
                });
  }

  @Test
  void test_defaultInitDoesNotThrow()
  {
    assertDoesNotThrow(() -> {new Building(); });
  }
}
