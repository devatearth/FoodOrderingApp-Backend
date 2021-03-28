package com.upgrad.FoodOrderingApp.service.entity;

import com.upgrad.FoodOrderingApp.service.common.ItemType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;
@NamedNativeQueries({
        // Using native query as named queries do not support LIMIT in nested statements.
        @NamedNativeQuery(
                name = "topFivePopularItemsByRestaurant",
                query =
                        "select * from item where id in "
                                + "(select ITEM_ID from order_item where ORDER_ID in "
                                + "(select ID from orders where RESTAURANT_ID = ? ) "
                                + "group by order_item.ITEM_ID "
                                + "order by (count(order_item.ORDER_ID)) "
                                + "desc LIMIT 5)",
                resultClass = ItemEntity.class)
})

@Entity
@Table(name = "item")
public class ItemEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    private String uuid;

    @Column(name = "item_name")
    @Size(max = 200)
    private String itemName;

    @Column(name = "price")
    private Integer price;

    @Column(name = "type")
    @Size(max = 10)
    private ItemType type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }


}
