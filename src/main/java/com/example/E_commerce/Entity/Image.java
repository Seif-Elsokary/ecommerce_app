package com.example.E_commerce.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;

    @Lob
    private Blob image;

    private String downloadUrl;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
