package com.maxruiz.app;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import com.maxruiz.config.ElevatorConfig;
import com.maxruiz.config.PassengerConfig;
import com.maxruiz.structures.Building;
import com.maxruiz.passengers.PassengerPriority;

public class TestBuilding 
{
  int valid_lowestFloor = 0;
  int valid_highestFloor = 5;
  int valid_maxNumPassengersPerFloor = 1;
  ArrayList<ElevatorConfig> valid_elevatorConfigList = new ArrayList<>() {{new ElevatorConfig(valid_lowestFloor, valid_highestFloor); }};

  ArrayList<Double> sickFactors = new ArrayList<>( List.of(0.01, 1.1));
  PassengerConfig pc0 = new PassengerConfig(PassengerPriority.get().getLowestPriority(), valid_lowestFloor, valid_lowestFloor, valid_highestFloor, 4, sickFactors);
  PassengerConfig pc1 = new PassengerConfig(PassengerPriority.get().getLowestPriority(), (valid_highestFloor - valid_lowestFloor) / 2, valid_lowestFloor, valid_highestFloor, 6, sickFactors);
  ArrayList<PassengerConfig> valid_passengerConfigs = new ArrayList<>(List.of(pc0, pc1));
  
  @Test
  void test_badInitFloorsTheSame()
  {
    int invalid_highestFloor = 1; //valid_lowestFloor;
    assertThrows(IllegalArgumentException.class, () ->  
                {new Building(valid_lowestFloor, invalid_highestFloor, valid_maxNumPassengersPerFloor, false, valid_elevatorConfigList, false, valid_passengerConfigs); 
                });
  }

  @Test 
  void test_badInitFloorsCrossed()
  {
    int invalid_highestFloor = valid_lowestFloor - 1;
    assertThrows(IllegalArgumentException.class, () ->  
                {new Building(valid_lowestFloor, invalid_highestFloor, valid_maxNumPassengersPerFloor, false, valid_elevatorConfigList, false, valid_passengerConfigs); 
                });
  }

  @Test 
  void test_badInitNumPassengersPerFloor()
  {
    int invalid_maxNumPassengersPerFloor = 0;
    ArrayList<ElevatorConfig> invalid_ElevatorConfigs = new ArrayList<>();
    assertThrows(IllegalArgumentException.class, () ->  
                {new Building(valid_lowestFloor, valid_highestFloor, invalid_maxNumPassengersPerFloor, false, invalid_ElevatorConfigs, false, valid_passengerConfigs); 
                });
  }

  @Test
  void test_defaultInitDoesNotThrow()
  {
    assertDoesNotThrow(() -> {new Building(); });
  }
}
