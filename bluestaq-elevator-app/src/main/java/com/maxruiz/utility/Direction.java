package com.maxruiz.utility;

import java.util.Random;
import java.util.Map;
import java.util.HashMap;

/**
 * This enumeration is used to describe directions.
 * It was created so that it could have a value assigned to it, thus allowing
 * instances to be randomly created.
 */
public enum Direction
{
  UP(0),
  DOWN(1),
  IDLE(2),
  SIZE(3);

  public final int Value;

  /**
   * Private constructor for the enum will take in an integer value
   * @param value int - numerical value of the enum
   */
  private Direction(int value)
  {
    Value = value;
  }

  /**
   * Build a map to go between the enumeration type and the integer value it holds.
   */
  private static final Map<Integer, Direction> _map = new HashMap<Integer, Direction>();
  static 
  {
    for (Direction direction : Direction.values()) 
    {
      _map.put(direction.Value, direction);
    }
  }
  
  /**
   * @param value int - convert an integer value into the enumeration type 'Direction'
   * @return Direction - return the enumeration type from the integer value
   */
  public static Direction from(int value)
  {
    if (value > 0 && value < SIZE.Value)
    {
      return _map.get(value);
    }
    
    return IDLE;
  }

  /**
   * This is a utility function to procure a randomly generated direction
   * @return Direction - a randomly generated direction
   */
  public static Direction getRandomDirection()
  {
    Random random = new Random();

    int randomIdx = random.nextInt(SIZE.Value);

    return from(randomIdx);
  }
}
