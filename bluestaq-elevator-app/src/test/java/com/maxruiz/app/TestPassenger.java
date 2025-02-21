package com.maxruiz.app;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.maxruiz.passengers.Passenger;
import com.maxruiz.structures.Building;

/*
 * Integer priority, int originFloor, int lowestFloor, 
                   int highestFloor, int sqft)
 */
public class TestPassenger 
{
  Integer valid_priority = 0;
  int valid_originFloor = 0;
  int valid_lowestFloor = 0;
  int valid_highestFloor = 10;
  int valid_sqft = 4;

  @Test 
  void test_badInitOriginFloor()
  {
    int invalid_originFloor = valid_lowestFloor - 1;
    assertThrows(IllegalArgumentException.class, () ->  
                {new Passenger(valid_priority, invalid_originFloor, valid_lowestFloor, valid_highestFloor, valid_sqft);
                });
  }

  @Test 
  void test_badInitFloorsTheSame()
  {
    int invalid_highestFloor = valid_lowestFloor;
    assertThrows(IllegalArgumentException.class, () ->  
                {new Passenger(valid_priority, valid_originFloor, valid_lowestFloor, invalid_highestFloor, valid_sqft);
                });
  }

  @Test 
  void test_badInitFloorsCrossed()
  {
    int invalid_highestFloor = valid_lowestFloor - 1;
    assertThrows(IllegalArgumentException.class, () ->  
                {new Passenger(valid_priority, valid_originFloor, valid_lowestFloor, invalid_highestFloor, valid_sqft);
                });
  }

  @Test 
  void test_badInitSqft()
  {
    int invalid_sqft = 0;
    assertThrows(IllegalArgumentException.class, () ->  
                {new Passenger(valid_priority, valid_originFloor, valid_lowestFloor, valid_highestFloor, invalid_sqft);
                });
  }
}
