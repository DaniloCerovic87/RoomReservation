package com.university.room.reservation.ai.tool;

import com.university.room.reservation.ai.request.RoomSearchRequest;
import com.university.room.reservation.exception.ValidationException;
import com.university.room.reservation.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationTools {

    private final RoomService roomService;
    private final MessageSource messageSource;

    @Tool(description = """
              Finds available meeting rooms for a given date, time range, and capacity.
              Use this tool only when the user provided date, start time, end time, and capacity.
              """)
    public Object findAvailableRooms(RoomSearchRequest request) {
        try {
            return roomService.findAvailableRooms(
                    request.getDate(),
                    request.getStartTime(),
                    request.getEndTime(),
                    request.getCapacity()
            );
        }  catch (ValidationException e) {
                    String message = messageSource.getMessage(
                    e.getMessageKey(),
                    e.getParams(),
                    LocaleContextHolder.getLocale()
            );

            return "Invalid room search request: " + message;
        }
    }

}
