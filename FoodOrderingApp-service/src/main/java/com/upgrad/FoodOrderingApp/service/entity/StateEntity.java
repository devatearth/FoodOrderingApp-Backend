package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "state")
@NamedQueries({
  @NamedQuery(name = "getAllStates", query = "SELECT state FROM StateEntity state"),
  @NamedQuery(name = "getStateByUuid", query = "SELECT state FROM StateEntity state WHERE uuid=:uuid")
})
public class StateEntity {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "uuid")
  @Size(max = 200)
  private String uuid;

  @Column(name = "state_name")
  private String stateName;
  
  /* methods */
  public String toString() {
    String obj = "StateEntity Object {\n";
    obj += "  id: " + this.id + ",\n";
    obj += "  uuid: " + this.uuid + ",\n";
    obj += "  state_name: " + this.stateName + ",\n";
    obj += "}";
    return obj;
  }
  public String getStateName() {
    return this.stateName;
  }
  public String getUuid() {
    return this.uuid;
  }
  public int getId() {
    return this.id;
  }
  public void setStateName(String stateName) {
    this.stateName = stateName;
  }
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
  public void setId(int id) {
    this.id = id;
  }
}
