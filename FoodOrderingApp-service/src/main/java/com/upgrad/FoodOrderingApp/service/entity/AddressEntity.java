package com.upgrad.FoodOrderingApp.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "address")
@NamedQueries({
  @NamedQuery(name = "selectAllAddresses", query = "SELECT u FROM AddressEntity u")
})
public class AddressEntity {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "state_id")
  private StateEntity state;

  @Column(name = "active")
  private int active;

  @Column(name = "locality")
  private String locality;

  @Column(name = "city")
  private String city;
  
  @Column(name = "pincode")
  private String pincode;

  @Column(name = "uuid")
  private String uuid;

  @Column(name = "flat_buil_number")
  private String flatBuildingNumber;

  /* methods */
  public String getFlatBuildingNumber() {
    return this.flatBuildingNumber;
  }
  public String getUuid() {
    return this.uuid;
  }
  public String getPincode() {
    return this.pincode;
  }
  public String getCity() {
    return this.city;
  }
  public String getLocality() {
    return this.locality;
  }
  public int getActive() {
    return this.active;
  }
  public StateEntity getState() {
    return this.state;
  }
  public int getId() {
    return this.id;
  }
  public void setFlatBuildingNumber(String flatBuildingNumber) {
    this.flatBuildingNumber = flatBuildingNumber;
  }
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
  public void setPincode(String pincode) {
    this.pincode = pincode;
  }
  public void setCity(String city) {
    this.city = city;
  }
  public void setLocality(String locality) {
    this.locality = locality;
  }
  public void setActive(int active) {
    this.active = active;
  }
  public void setState(StateEntity state) {
    this.state = state;
  }
  public void setId(int id) {
    this.id = id;
  }
  public String toString() {
    String obj = "AddressEntity Object {\n";
    obj += "  id: " + this.id + ",\n";
    obj += "  state: " + this.state + ",\n";
    obj += "  active: " + this.active + ",\n";
    obj += "  locality: " + this.locality + ",\n";
    obj += "  city: " + this.city + ",\n";
    obj += "  pincode: " + this.pincode + ",\n";
    obj += "  uuid: " + this.uuid + ",\n";
    obj += "  flat_buil_number: " + this.flatBuildingNumber + ",\n";
    obj += "}";
    return obj;
  }
}
