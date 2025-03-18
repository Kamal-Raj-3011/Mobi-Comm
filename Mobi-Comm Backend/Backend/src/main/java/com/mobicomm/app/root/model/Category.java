package com.mobicomm.app.root.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "category")

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    private String categoryId;
    
    @Column(nullable = false, unique = true)
    private String categoryName;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
    @JsonIgnoreProperties({"category"})
    private List<Plan> plans;
    

}