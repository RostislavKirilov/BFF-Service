package com.tinqinacademy.bff.api.base.messages;

public class RoomAlreadyReserved extends RuntimeException {
    public RoomAlreadyReserved() {
        super(ExceptionMessages.ROOM_ALREADY_BOOKED);
    }
}
