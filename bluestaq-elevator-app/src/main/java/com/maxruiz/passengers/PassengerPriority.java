package com.maxruiz.passengers;

import java.util.Map;
import java.util.HashMap;

/**
 * This class is a utility class to build a map of priorities and assign
 * them to some human readable value (String). This class is meant to be
 * able to load a file that contains information on the types of passengers
 * that can exists in the system.
 * 
 * This class is a singleton, it contains all necessary static passenger 
 * priority information that the application could use.
 */
public class PassengerPriority
{
  private Map<String, Integer> m_priorityMap = new HashMap<>();
  public final int HIGHEST_PRIORITY = 0;
  private int m_lowestPriority = 0;

  private static PassengerPriority self = null;

  /**
   * Constructor for PassengerPriority which builds the map of passenger
   * titles to integer priority value
   */
  private PassengerPriority()
  {
    // Desired feature: Extract data from a json config file.
    
    setDefaultPriorityMap();
  }

  /**
   * This method builds the default map of title and priority
   */
  private void setDefaultPriorityMap()
  {
    // Set the lowest priority to highest priority as it is changed within this function.
    m_lowestPriority = HIGHEST_PRIORITY;
    m_priorityMap.clear();

    m_priorityMap.put("firefighter", Integer.valueOf(m_lowestPriority++));
    m_priorityMap.put("maintenance", Integer.valueOf(m_lowestPriority++));
    m_priorityMap.put("civilian", Integer.valueOf(m_lowestPriority));
  }

  /**
   * This method returns the singleton instance of this class.
   * @return PassengerPriority
   */
  public static PassengerPriority get()
  {
    if (null == self)
    {
      self = new PassengerPriority();
    }

    return self;
  }

  /**
   * @param title String - the title of the passenger with reference to some
   *              integer priority value
   * @return Priority - the enumeration value of title
   */
  public Integer getPriorityFromTitle(String title)
  {
    if (title.isBlank() || !m_priorityMap.containsKey(title))
    {
      return null;
    }
    
    return m_priorityMap.get(title);
  }

  public Integer getLowestPriority()
  {
    return m_lowestPriority;
  }

  public Integer getHighestPriority()
  {
    return HIGHEST_PRIORITY;
  }
}
