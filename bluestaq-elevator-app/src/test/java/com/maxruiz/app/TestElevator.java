package com.maxruiz.app;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.maxruiz.structures.Elevator;

public class TestElevator 
{
  int valid_id = 0;
  int valid_sqft = 10;
  int valid_lowestFloor = 0;
  int valid_highestFloor = 5;
  int valid_startingFloor = 1;
  int valid_atFloorFrames = 1;
  int valid_movingFrames = 1;

  @Test
  void test_badInitSqft()
  {
    int invalid_sqft = 0;
    assertThrows(IllegalArgumentException.class, () ->  
                {new Elevator(valid_id, invalid_sqft, valid_lowestFloor, valid_highestFloor, valid_startingFloor, valid_atFloorFrames, valid_movingFrames ); 
                });
  }

  @Test
  void test_badInitFloorsTheSame()
  {
    int invalid_highestFloor = valid_lowestFloor;
    assertThrows(IllegalArgumentException.class, () ->  
                {new Elevator(valid_id, valid_sqft, valid_lowestFloor, invalid_highestFloor, valid_startingFloor, valid_atFloorFrames, valid_movingFrames ); 
                });
  }

  @Test
  void test_badInitFloorsCrossed()
  {
    int invalid_highestFloor = valid_lowestFloor - 1;
    assertThrows(IllegalArgumentException.class, () ->  
                {new Elevator(valid_id, valid_sqft, valid_lowestFloor, invalid_highestFloor, valid_startingFloor, valid_atFloorFrames, valid_movingFrames ); 
                });
  }

  @Test
  void test_badInitStartingFloor()
  {
    int invalid_startingFloor = valid_lowestFloor - 1;
    assertThrows(IllegalArgumentException.class, () ->  
                {new Elevator(valid_id, valid_sqft, valid_lowestFloor, valid_highestFloor, invalid_startingFloor, valid_atFloorFrames, valid_movingFrames ); 
                });
  }

  @Test 
  void test_badInitAtFloorFrames()
  {
    int invalid_atFloorFrames = 0;
    assertThrows(IllegalArgumentException.class, () ->  
                {new Elevator(valid_id, valid_sqft, valid_lowestFloor, valid_highestFloor, valid_startingFloor, invalid_atFloorFrames, valid_movingFrames ); 
                });
  }

  @Test 
  void test_badInitMovingFrames()
  {
    int invalid_movingFrames = 0;
    assertThrows(IllegalArgumentException.class, () ->  
                {new Elevator(valid_id, valid_sqft, valid_lowestFloor, valid_highestFloor, valid_startingFloor, valid_atFloorFrames, invalid_movingFrames ); 
                });
  }

  
}
