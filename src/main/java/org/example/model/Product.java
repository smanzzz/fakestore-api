package org.example.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Entity
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @Column
    private BigInteger price;

    @Column
    private String description;

    @Column
    private String image;

    @Column
    private String category;


}
