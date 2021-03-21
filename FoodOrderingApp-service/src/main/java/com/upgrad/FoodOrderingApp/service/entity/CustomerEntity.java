package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "customer")
@NamedQueries({
  @NamedQuery(name = "getUserByContactNumber", query = "SELECT u FROM CustomerEntity u WHERE u.contactNumber=:contactNumber"),
  @NamedQuery(name = "getUserByCustomerId", query = "SELECT u FROM CustomerEntity u WHERE u.id=:id"),
        @NamedQuery(name = "userByUuid", query = "select c from CustomerEntity c where c.uuid = :uuid"),
})
public class CustomerEntity {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "uuid")
  @Size(max = 200)
  private String uuid;

  @Column(name = "firstname")
  @Size(max = 100)
  private String firstName;

  @Column(name = "lastname")
  @Size(max = 100)
  private String lastName;
  
  @Column(name = "email")
  @Size(max = 100)
  private String email;

  @Column(name = "contact_number")
  @Size(max = 10)
  private String contactNumber;

  @Column(name = "password")
  @Size(max = 255)
  private String password;

  @Column(name = "salt")
  @Size(max = 200)
  private String salt;

  public CustomerEntity(){}

  /* methods */
  public String toString() {
    String obj = "UserEntity Object {\n";
    obj += "  id: " + this.id + ",\n";
    obj += "  uuid: " + this.uuid + ",\n";
    obj += "  firstName: " + this.firstName + ",\n";
    obj += "  lastName: " + this.lastName + ",\n";
    obj += "  email: " + this.email + ",\n";
    obj += "  contactNumber: " + this.contactNumber + ",\n";
    obj += "  password: " + this.password + ",\n";
    obj += "  salt: " + this.salt + ",\n";
    obj += "}";
    return obj;
  }
  public String getSalt() {
    return this.salt;
  }
  public String getPassword() {
    return this.password;
  }
  public String getContactNumber() {
    return this.contactNumber;
  }
  public String getEmail() {
    return this.email;
  }
  public String getLastName() {
    return this.lastName;
  }
  public String getFirstName() {
    return this.firstName;
  }
  public String getUuid() {
    return this.uuid;
  }
  public Integer getId() {
    return this.id;
  }
  public void setSalt(String salt) {
    this.salt = salt;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public void setContactNumber(String contactNumber) {
    this.contactNumber = contactNumber;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
  public void setId(Integer id) {
    this.id = id;
  }
}
