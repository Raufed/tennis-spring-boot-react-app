package by.payyzau.tennis.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

@Entity
public class Dish {
    public Dish(Long id, String name, double price, String imageId, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageId = imageId;
        this.imageUrl = imageUrl;
    }
    public Dish(String name, double price, String imageId, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageId = imageId;
        this.imageUrl = imageUrl;
    }
    public Dish() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name")
    @NotBlank(message = "Вы не задали название")
    @NotNull(message = "Вы не задали название")
    private String name;
    @Column(name = "price")
    @NotNull(message = "Цена не задана")
    @Positive(message = "Задайте цену")
    private double price;
    @Column(name = "imageUrl")
    @NotNull(message = "Вы не загрузи картинку блюда")
    private String imageUrl;
    @Column(name = "imageId")
    private String imageId;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", imageId='" + imageId + '\'' +
                '}';
    }
}
