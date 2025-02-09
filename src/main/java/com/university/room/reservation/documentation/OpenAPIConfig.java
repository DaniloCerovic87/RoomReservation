package com.university.room.reservation.documentation;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.Arrays;


@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Room Reservation API")
                        .version("1.0")
                        .description("API documentation for managing room reservation"))
                .paths(new Paths()
                        .addPathItem("/api/reservations", generateReservationsPathItem())
                        .addPathItem("/api/reservations/{id}", generateReservationsByIdPathItem())
                );
    }

    private PathItem generateReservationsPathItem() {
        return new PathItem()
                .get(getReservationsOperation())
                .post(createReservationOperation());
    }

    private Operation getReservationsOperation() {
        return new Operation()
                .operationId("findReservations")
                .summary("Find reservations based on search filters")
                .description("Retrieve a list of reservations based on provided filter criteria")
                .parameters(Arrays.asList(
                        new Parameter()
                                .name("roomName")
                                .in("query")
                                .description("Name of the room")
                                .required(false)
                                .schema(new StringSchema()),
                        new Parameter()
                                .name("startTime")
                                .in("query")
                                .description("Start time for filtering reservations (format: yyyy-MM-dd'T'HH:mm:ss)")
                                .required(false)
                                .schema(new StringSchema().format("date-time")),
                        new Parameter()
                                .name("endTime")
                                .in("query")
                                .description("End time for filtering reservations (format: yyyy-MM-dd'T'HH:mm:ss)")
                                .required(false)
                                .schema(new StringSchema().format("date-time")),
                        new Parameter()
                                .name("status")
                                .in("query")
                                .description("Reservation status (Pending, Approved, Rejected)")
                                .required(false)
                                .schema(new StringSchema()),
                        new Parameter()
                                .name("reservationPurpose")
                                .in("query")
                                .description("Purpose of the reservation (Class, Exam, Meeting, Event)")
                                .required(false)
                                .schema(new StringSchema())
                ))
                .responses(new ApiResponses()
                        .addApiResponse(String.valueOf(HttpStatus.OK.value()), new ApiResponse()
                                .description("Reservations successfully retrieved")
                                .content(new Content()
                                        .addMediaType("application/json", new MediaType()
                                                .schema(new ArraySchema()
                                                        .items(new Schema<>().$ref("#/components/schemas/ReservationDTO"))
                                                )
                                        )
                                )
                        )
                        .addApiResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), new ApiResponse()
                                .description("Invalid filter parameters")
                        )
                );
    }

    private Operation createReservationOperation() {
        return new Operation()
                .operationId("createReservation")
                .summary("Create a new reservation")
                .description("Create a new reservation in the system")
                .requestBody(new RequestBody()
                        .description("Create reservation details")
                        .required(true)
                        .content(new Content()
                                .addMediaType("application/json", new MediaType()
                                        .schema(new Schema<>().$ref("#/components/schemas/ReservationRequest"))
                                )
                        )
                )
                .responses(new ApiResponses()
                        .addApiResponse(String.valueOf(HttpStatus.CREATED.value()), new ApiResponse()
                                .description("Reservation successfully created")
                                .content(new Content().addMediaType(
                                        "application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/ReservationDTO"))
                                ))
                        )
                        .addApiResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), new ApiResponse()
                                .description("Invalid input")));
    }

    private Operation updateReservationOperation() {
        return new Operation()
                .operationId("updateReservation")
                .summary("Update a reservation")
                .description("Update an existing reservation")
                .requestBody(new RequestBody()
                        .description("Updated reservation details")
                        .required(true)
                        .content(new Content()
                                .addMediaType("application/json", new MediaType()
                                        .schema(new Schema<>().$ref("#/components/schemas/ReservationRequest"))
                                )
                        )
                )
                .responses(new ApiResponses()
                        .addApiResponse(String.valueOf(HttpStatus.OK.value()), new ApiResponse()
                                .description("Reservation updated successfully")
                                .content(new Content()
                                        .addMediaType("application/json", new MediaType()
                                                .schema(new Schema<>()
                                                        .$ref("#/components/schemas/ReservationDTO"))
                                        )
                                )
                        )
                        .addApiResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), new ApiResponse()
                                .description("Invalid input")
                        ));
    }

    private PathItem generateReservationsByIdPathItem() {
        return new PathItem()
                .get(getReservationByIdOperation())
                .put(updateReservationOperation())
                .delete(deleteReservationByIdOperation());

    }

    private Operation getReservationByIdOperation() {
        return new Operation()
                .operationId("getReservationById")
                .summary("Get a reservation by ID")
                .description("Retrieve reservation details based on reservation ID")
                .addParametersItem(new Parameter()
                        .name("id")
                        .description("Reservation ID")
                        .required(true)
                        .in(ParameterIn.PATH.toString()))
                .responses(new ApiResponses()
                        .addApiResponse(String.valueOf(HttpStatus.CREATED.value()), new ApiResponse()
                                .description("Reservation details retrieved successfully")
                                .content(new Content().addMediaType(
                                        "application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/ReservationDTO"))
                                ))
                        )
                        .addApiResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), new ApiResponse()
                                .description("Employee not found")));
    }

    private Operation deleteReservationByIdOperation() {
        return new Operation()
                .operationId("deleteReservation")
                .summary("Delete a reservation by ID")
                .description("Delete a reservation from the system")
                .addParametersItem(new Parameter()
                        .name("id")
                        .description("Reservation ID")
                        .required(true)
                        .in(ParameterIn.PATH.toString()))
                .responses(new ApiResponses()
                        .addApiResponse(String.valueOf(HttpStatus.NO_CONTENT.value()),
                                new ApiResponse().description(("Reservation successfully deleted")))
                        .addApiResponse(String.valueOf(HttpStatus.NOT_FOUND.value()),
                                new ApiResponse().description(("Reservation not found"))));
    }

}
