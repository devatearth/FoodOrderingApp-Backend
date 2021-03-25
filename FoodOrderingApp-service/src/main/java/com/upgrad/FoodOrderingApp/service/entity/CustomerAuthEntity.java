package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Entity
@Table(name = "customer_auth")
@NamedQueries({
  @NamedQuery(name = "getEntityByToken", query = "SELECT u FROM CustomerAuthEntity u WHERE u.accessToken=:accessToken")
})
public class CustomerAuthEntity extends CustomerEntity{
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "uuid")
  @Size(max = 200)
  private String uuid;

  @Column(name = "access_token")
  @Size(max = 500)
  private String accessToken;

  @Column(name = "customer_id")
  private Integer customerId;

  @Column(name = "login_at")
  private ZonedDateTime loginAt;

  @Column(name = "logout_at")
  private ZonedDateTime logoutAt;

  @Column(name = "expires_at")
  private ZonedDateTime expiresAt;

  public CustomerAuthEntity(){}

  /* methods */
  public String toString() {
    String obj = "CustomerAuthEntity Object {\n";
    obj += "  id: " + this.id + ",\n";
    obj += "  uuid: " + this.uuid + ",\n";
    obj += "  accessToken: " + this.accessToken + ",\n";
    obj += "  customerId: " + this.customerId + ",\n";
    obj += "  loginAt: " + this.loginAt + ",\n";
    obj += "  logoutAt: " + this.logoutAt + ",\n";
    obj += "  expiresAt: " + this.expiresAt + ",\n";
    obj += "}";
    return obj;
  }
  public ZonedDateTime getExpiresAt() {
    return this.expiresAt;
  }
  public ZonedDateTime getLogoutAt() {
    return this.logoutAt;
  }
  public ZonedDateTime getLoginAt() {
    return this.loginAt;
  }
  public Integer getCustomerId() {
    return this.customerId;
  }
  public String getAccessToken() {
    return this.accessToken;
  }
  public String getUuid() {
    return this.uuid;
  }
  public Integer getId() {
    return this.id;
  }
  public void setExpiresAt(ZonedDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }
  public void setLogoutAt(ZonedDateTime logoutAt) {
    this.logoutAt = logoutAt;
  }
  public void setLoginAt(ZonedDateTime loginAt) {
    this.loginAt = loginAt;
  }
  public void setCustomerId(Integer customerId) {
    this.customerId = customerId;
  }
  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
  public void setUuid(String uuid) {
    this.uuid = uuid;
  }
  public void setId(Integer id) {
    this.id = id;
  }
}
