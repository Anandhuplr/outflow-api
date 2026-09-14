package com.outFlow.outFlow.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String googleId;

    @Column(nullable = false, unique = true)
    private String email;

    private String name;

    private String picture;

    private String currency;

    public User() {
    }

    public User(
            String googleId,
            String email,
            String name,
            String picture,
            String currency
    ) {
        this.googleId = googleId;
        this.email = email;
        this.name = name;
        this.picture = picture;
        this.currency = currency;
    }
}
