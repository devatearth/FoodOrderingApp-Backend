package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "customer_address")
@NamedQueries({
  @NamedQuery(name = "getEntityByAddressId", query = "SELECT u FROM CustomerAddressEntity u WHERE address_id=:address_id")
})
public class CustomerAddressEntity {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "customer_id")
  private CustomerEntity customerEntity;

  @ManyToOne
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinColumn(name = "address_id")
  private AddressEntity addressEntity;

  /* methods */
  public String toString() {
    String obj = "CustomerAddressEntity Object {\n";
    obj += "  id: " + this.id + ",\n";
    obj += "  customer_id: " + this.customerEntity+ ",\n";
    obj += "  address_id: " + this.addressEntity+ ",\n";
    obj += "}";
    return obj;
  }
  public AddressEntity getAddressEntity() {
    return this.addressEntity;
  }
  public CustomerEntity getCustomerEntity() {
    return this.customerEntity;
  }
  public Integer getId() {
    return this.id;
  }
  public void setAddressEntity(AddressEntity addressEntity) {
    this.addressEntity = addressEntity;
  }
  public void setCustomerEntity(CustomerEntity customerEntity) {
    this.customerEntity = customerEntity;
  }
  public void setId(Integer id) {
    this.id = id;
  }
}
