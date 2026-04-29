package com.fun.bookMyShow.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(
        name="theaters",
        indexes = {
                @Index(name = "idx_theater_city", columnList = "city")
        }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String address;


    private String city;

    private Integer totalScreen;

    @OneToMany(mappedBy = "theater",cascade = CascadeType.ALL)
    private List<Screen> screens;
}
