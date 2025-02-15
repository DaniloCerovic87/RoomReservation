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
import java.util.Collections;


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
                        .addPathItem("/api/reservations/rooms/{id}", generateReservationsByRoomIdPathItem())

                        .addPathItem("/api/rooms", generateRoomsPathItem())
                        .addPathItem("/api/rooms/{id}", generateRoomsByIdPathItem())

                        .addPathItem("/api/import/employees", generateImportEmployeesPathItem())

                        .addPathItem("/api/employee-files", generateEmployeeFilesPathItem())

                        .addPathItem("/api/employee-rows/{fileId}", generateEmployeeRowsByFileIdPathItem())
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

    private PathItem generateReservationsByIdPathItem() {
        return new PathItem()
                .get(getReservationByIdOperation())
                .put(updateReservationOperation())
                .delete(deleteReservationOperation());
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
                        .addApiResponse(String.valueOf(HttpStatus.OK.value()), new ApiResponse()
                                .description("Reservation details retrieved successfully")
                                .content(new Content().addMediaType(
                                        "application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/ReservationDTO"))
                                ))
                        )
                        .addApiResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), new ApiResponse()
                                .description("Reservation not found")));
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

    private Operation deleteReservationOperation() {
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

    private PathItem generateReservationsByRoomIdPathItem() {
        return new PathItem()
                .get(getReservationsByRoomOperation());
    }

    private Operation getReservationsByRoomOperation() {
        return new Operation()
                .operationId("getReservationsByRoom")
                .summary("Get reservations by room ID")
                .description("Retrieve list of reservations by room ID")
                .addParametersItem(new Parameter()
                        .name("id")
                        .description("Room ID")
                        .required(true)
                        .in(ParameterIn.PATH.toString()))
                .responses(new ApiResponses()
                        .addApiResponse(String.valueOf(HttpStatus.OK.value()), new ApiResponse()
                                .description("Reservations successfully retrieved")
                                .content(new Content().addMediaType(
                                        "application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/ReservationDTO"))
                                ))
                        )
                );
    }


    private PathItem generateRoomsPathItem() {
        return new PathItem()
                .get(getRoomsOperation())
                .post(createRoomOperation());
    }

    private Operation createRoomOperation() {
        return new Operation()
                .operationId("createRoom")
                .summary("Create a new room")
                .description("Create a new room in the system")
                .requestBody(new RequestBody()
                        .description("Create room details")
                        .required(true)
                        .content(new Content()
                                .addMediaType("application/json", new MediaType()
                                        .schema(new Schema<>().$ref("#/components/schemas/RoomRequest"))
                                )
                        )
                )
                .responses(new ApiResponses()
                        .addApiResponse(String.valueOf(HttpStatus.CREATED.value()), new ApiResponse()
                                .description("Room successfully created")
                                .content(new Content().addMediaType(
                                        "application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/RoomDTO"))
                                ))
                        )
                        .addApiResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), new ApiResponse()
                                .description("Invalid input")));
    }

    private Operation getRoomsOperation() {
        return new Operation()
                .operationId("getAllRooms")
                .summary("Find all rooms")
                .description("Retrieve a list of all rooms in the system")
                .responses(new ApiResponses()
                        .addApiResponse(String.valueOf(HttpStatus.OK.value()), new ApiResponse()
                                .description("Rooms successfully retrieved")
                                .content(new Content()
                                        .addMediaType("application/json", new MediaType()
                                                .schema(new ArraySchema()
                                                        .items(new Schema<>().$ref("#/components/schemas/RoomDTO"))
                                                )
                                        )
                                )
                        )
                );
    }

    private PathItem generateRoomsByIdPathItem() {
        return new PathItem()
                .get(getRoomByIdOperation())
                .put(updateRoomOperation())
                .delete(deleteRoomOperation());
    }

    private Operation getRoomByIdOperation() {
        return new Operation()
                .operationId("getRoomById")
                .summary("Get a room by ID")
                .description("Retrieve room details based on room ID")
                .addParametersItem(new Parameter()
                        .name("id")
                        .description("Room ID")
                        .required(true)
                        .in(ParameterIn.PATH.toString()))
                .responses(new ApiResponses()
                        .addApiResponse(String.valueOf(HttpStatus.OK.value()), new ApiResponse()
                                .description("Room details retrieved successfully")
                                .content(new Content().addMediaType(
                                        "application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/RoomDTO"))
                                ))
                        )
                        .addApiResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), new ApiResponse()
                                .description("Room not found")));
    }

    private Operation updateRoomOperation() {
        return new Operation()
                .operationId("updateRoom")
                .summary("Update a room")
                .description("Update an existing room")
                .requestBody(new RequestBody()
                        .description("Updated room details")
                        .required(true)
                        .content(new Content()
                                .addMediaType("application/json", new MediaType()
                                        .schema(new Schema<>().$ref("#/components/schemas/RoomRequest"))
                                )
                        )
                )
                .responses(new ApiResponses()
                        .addApiResponse(String.valueOf(HttpStatus.OK.value()), new ApiResponse()
                                .description("Room updated successfully")
                                .content(new Content()
                                        .addMediaType("application/json", new MediaType()
                                                .schema(new Schema<>()
                                                        .$ref("#/components/schemas/RoomDTO"))
                                        )
                                )
                        )
                        .addApiResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), new ApiResponse()
                                .description("Invalid input")
                        ));
    }

    private Operation deleteRoomOperation() {
        return new Operation()
                .operationId("deleteRoom")
                .summary("Delete a room by ID")
                .description("Delete a room from the system")
                .addParametersItem(new Parameter()
                        .name("id")
                        .description("Room ID")
                        .required(true)
                        .in(ParameterIn.PATH.toString()))
                .responses(new ApiResponses()
                        .addApiResponse(String.valueOf(HttpStatus.NO_CONTENT.value()),
                                new ApiResponse().description(("Room successfully deleted")))
                        .addApiResponse(String.valueOf(HttpStatus.NOT_FOUND.value()),
                                new ApiResponse().description(("Room not found"))));
    }

    private PathItem generateImportEmployeesPathItem() {
        return new PathItem()
                .post(importEmployeesOperation());
    }

    private Operation importEmployeesOperation() {
        return new Operation()
                .operationId("importEmployees")
                .summary("Import employee data from an Excel file")
                .description("Import employees from an Excel (.xlsx) file containing columns: firstName, lastName, personalId, email, title, department.")
                .requestBody(new RequestBody()
                        .description("Excel file (.xlsx) containing employee data to be imported. The file should have the following columns: firstName, lastName, personalId, email, title, department.")
                        .required(true)
                        .content(new Content()
                                .addMediaType("multipart/form-data", new MediaType())
                        )
                )
                .parameters(Collections.singletonList(
                        new Parameter()
                                .name("userId")
                                .description("ID of the user initiating the import")
                                .required(true)
                                .in(ParameterIn.PATH.toString()))
                )
                .responses(new ApiResponses()
                        .addApiResponse(String.valueOf(HttpStatus.OK.value()), new ApiResponse()
                                .description("File successfully uploaded. Processing has started asynchronously."))
                        .addApiResponse(String.valueOf(HttpStatus.NOT_FOUND.value()), new ApiResponse()
                                .description("User not found"))
                        .addApiResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), new ApiResponse()
                                .description("Another import file is currently being processed. Please wait until the operation is complete before proceeding with a new one.")
                        )
                );
    }

    private PathItem generateEmployeeFilesPathItem() {
        return new PathItem()
                .get(findAllEmployeeFilesOperation());
    }

    private Operation findAllEmployeeFilesOperation() {
        return new Operation()
                .operationId("findAllEmployeeFiles")
                .summary("Find all employee import files")
                .description("""
                        Retrieve a list of all employee import files that have been uploaded to the system. Status of data processing:
                         - IN_PROGRESS: The data processing is ongoing
                         - FAILED: An unexpected error occurred during the file upload process
                         - FINISHED: The data processing has been successfully completed""")
                .responses(new ApiResponses()
                        .addApiResponse(String.valueOf(HttpStatus.OK.value()), new ApiResponse()
                                .description("Employee files successfully retrieved")
                                .content(new Content()
                                        .addMediaType("application/json", new MediaType()
                                                .schema(new ArraySchema()
                                                        .items(new Schema<>().$ref("#/components/schemas/EmployeeFileDTO"))
                                                )
                                        )
                                )
                        )
                );
    }

    private PathItem generateEmployeeRowsByFileIdPathItem() {
        return new PathItem()
                .get(findAllEmployeeRowsByFileIdOperation());
    }

    private Operation findAllEmployeeRowsByFileIdOperation() {
        return new Operation()
                .operationId("findEmployeeRowsByFileId")
                .summary("Find employee rows by file ID")
                .description("Retrieve employee rows based on uploaded file ID")
                .addParametersItem(new Parameter()
                        .name("id")
                        .description("File ID")
                        .required(true)
                        .in(ParameterIn.PATH.toString()))
                .responses(new ApiResponses()
                        .addApiResponse(String.valueOf(HttpStatus.OK.value()), new ApiResponse()
                                .description("Employee rows retrieved successfully")
                                .content(new Content().addMediaType(
                                        "application/json",
                                        new MediaType().schema(new Schema<>().$ref("#/components/schemas/EmployeeRowDTO"))
                                ))
                        )
                        .addApiResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), new ApiResponse()
                                .description("Invalid input")
                        ));
    }

}
