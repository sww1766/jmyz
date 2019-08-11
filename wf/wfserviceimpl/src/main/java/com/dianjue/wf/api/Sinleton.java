package com.dianjue.wf.api;

import java.util.HashMap;
import java.util.Map;

public class Sinleton {

  private Map<String,Object> map = new HashMap<>();

  private static Sinleton ourInstance = new Sinleton();

  public static Sinleton getInstance() {
    return ourInstance;
  }

  private Sinleton() {
  }

  public Map<String, Object> getMap() {
    return map;
  }

  public void setMap(Map<String, Object> map) {
    this.map = map;
  }
}
