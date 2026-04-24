package com.fun.bookMyShow.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(name = "reset_password_token", length = 128)
    private String resetPasswordToken;

    @Column(name = "reset_password_expiry")
    private LocalDateTime resetPasswordExpiry;

    @Column(name = "reset_password_used_at")
    private LocalDateTime resetPasswordUsedAt;

   @Convert(converter = UserRoleConverter.class)
   @Column(nullable = false)
   private Role role = Role.USER;

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Booking>  bookings;

    public enum Role {
        USER,
        ADMIN

    }

}
