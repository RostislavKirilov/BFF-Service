package com.tinqinacademy.bff.api.base.messages;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingNotFoundException extends RuntimeException{


    private final String message = ExceptionMessages.BOOKING_NOT_FOUND;
}
