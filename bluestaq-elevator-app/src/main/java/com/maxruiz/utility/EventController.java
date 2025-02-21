package com.maxruiz.utility;

import java.util.Random;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * This class contains events that affect the system and how it plays out.
 * @author Max Ruiz
 */
public class EventController
{
  public enum EventType
  {
    IDLE("IDLE"),
    PASSENGERS("PASSENGERS"),
    STUCK("STUCK"),
    FIRE("FIRE"),
    SIZE("SIZE");

    public final String Representation;

    private EventType(String rep)
    {
      Representation = rep;
    }
  }

  private Map<EventType, Double> m_eventChanceMap = new LinkedHashMap<EventType, Double>();
  
  private Random m_randomGen = new Random();

  private ArrayList<EventType> m_customEventList = new ArrayList<>();
  private int m_customEventListIndex = 0;

  /**
   * Constructor for EventController. Here, it loads the possible events.
   */
  public EventController()
  {
    loadEventsMap();
  }

  /**
   * This method initializes a set of events with their probabilities or
   * creates a default set of events and probabilities
   */
  private void loadEventsMap()
  {
    // DESIRED FEATURE: Extract values from a json file

    loadEventsMapDefaults();

    sortEventMap();
  }

  /**
   * This method creates a default map of events and probabilities
   */
  private void loadEventsMapDefaults()
  {

    // Adds to 100% but it doesn't have to
    m_eventChanceMap.put(EventType.IDLE, 0.8);
    m_eventChanceMap.put(EventType.PASSENGERS, 0.19);
    m_eventChanceMap.put(EventType.STUCK, 0.0089);
    m_eventChanceMap.put(EventType.FIRE, 0.0011);
  }

  /**
   * This method sorts the event map such that the event with the least probability
   * is first in an ascending order. This is to fascilitate with randomly procuring values
   * if a loaded list of events and probabilities are out of order.
   */
  private void sortEventMap()
  {
    // Sort the Events based on chance of happening
    List<Map.Entry<EventType, Double>> list = new ArrayList<>(m_eventChanceMap.entrySet());
      
    list.sort(Map.Entry.comparingByValue());

    m_eventChanceMap.clear();

    for (Map.Entry<EventType, Double> entry : list) 
    {
      m_eventChanceMap.put(entry.getKey(), entry.getValue());
    }
  }

  /**
   * This method returns a randomly geneerated event for the system.
   * @return EventType - enumeration value that is an event
   */
  public EventType getNextRandomEvent()
  {
    double diceRoll = m_randomGen.nextDouble();

    for (Map.Entry<EventType, Double> eventEntry : m_eventChanceMap.entrySet())
    {
      
      if (diceRoll < eventEntry.getValue())
      {
        System.out.println("\n----- Random Event: " + eventEntry.getKey().Representation);

        return eventEntry.getKey();
      }
    }

    return EventType.IDLE;
  } 

  /**
   * @param loop boolean - determines if the returned custom event wraps to
   *             to the beginning of the provided list when it reaches the
   *             end, or if it should always return the last event in the 
   *             list after the end is reached.
   * @return EventType
   */
  public EventType getNextCustomEvent(boolean loop)
  {
    EventType et = EventType.IDLE;

    if (m_customEventListIndex >= m_customEventList.size())
    {
      if (loop)
      {
        // Loop back around
        m_customEventListIndex = 0;
        et =  m_customEventList.get(m_customEventListIndex);
      }
      else 
      {
        // Repeat last event in list
        et = m_customEventList.get(m_customEventListIndex-1);
      }
    }
    else 
    {
      et = m_customEventList.get(m_customEventListIndex++);
    }

    System.out.println("\n----- Custom Event: " + et.Representation);

    return et;
  }

  /**
   * Overloaded method of getNextCustomEvent that assumed the parameter
   * 'loop' is false
   * @return EventType
   * @see getNextCustomEvent
   */
  public EventType getNextCustomEvent()
  {
    return getNextCustomEvent(false);
  }

  /**
   * Load a list of events defined in a config file, if possible
   * Otherwise, load a default list
   */
  public void loadCustomEventList()
  {

    loadDefaultCustomEventList();
  }

  /**
   * Load a default list of custom events. 
   * This is supposed to be hardcoded.
   */
  private void loadDefaultCustomEventList()
  {
    m_customEventListIndex = 0;
    m_customEventList.clear();
    m_customEventList.add(EventController.EventType.IDLE);
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.IDLE);
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.IDLE);

    /*
    m_customEventList.add(EventController.EventType.IDLE);
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.PASSENGERS); // 10
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.PASSENGERS);
    m_customEventList.add(EventController.EventType.PASSENGERS); // 20
    m_customEventList.add(EventController.EventType.IDLE);
    */
  }

  /**
   * @param eventList ArrayList<EventType> - if the user wishes to control
   *                  the procurement of events in the system, then they
   *                  can submit their list of events here, to later be
   *                  procured.
   */
  public void setCustomEventList(ArrayList<EventType> eventList)
  {
    m_customEventListIndex = 0;
    m_customEventList.clear();
    m_customEventList = eventList;
  }
}


