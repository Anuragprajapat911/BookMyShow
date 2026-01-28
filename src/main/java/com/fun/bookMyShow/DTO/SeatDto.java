package com.fun.bookMyShow.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SeatDto {

    private Long id;
    private String seatNumber;
    private String seatType;
    private Integer basePrice;
}
