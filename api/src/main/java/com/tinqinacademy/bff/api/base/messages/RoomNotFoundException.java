package com.tinqinacademy.bff.api.base.messages;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RoomNotFoundException extends RuntimeException{

    private final String message = ExceptionMessages.ROOM_NOT_FOUND;


}
