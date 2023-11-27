package com.example.SimplestCRUDExample;

import com.example.SimplestCRUDExample.model.Reservation;
import com.example.SimplestCRUDExample.repo.ReservationRepository;
import com.example.SimplestCRUDExample.repo.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest
public class RoomAndReservationTests {

     @Autowired
    private MockMvc mockMvc;

     @Autowired
     private ReservationRepository reservationRepository;

    String exampleRoomJson = "{\"name\":\"berthas room\",\"description\":\"B room\",\"beds\":12,\"price\":200,\"availableFrom\":\"2024-02-01\",\"availableTo\":\"2024-02-14\"}";
    String exampleReservationJson = "{\"roomId\":1,\"checkIn\":\"2024-01-02\",\"checkOut\":\"2024-01-09\",\"guestCount\":2}";


    @Test
    void shouldReturnCREATEDafterAddRoom() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/addRoom")
                .accept(MediaType.APPLICATION_JSON).content(exampleRoomJson)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    void shouldReturnNoContent() throws Exception {
        // even though room was created, the number of beds searched is more than capacity
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("http://localhost:9090/api/getAllRooms")
                .queryParam("beds", "13")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    void shouldReturnExampleRoom() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("http://localhost:9090/api/getAllRooms")
                .queryParam("beds", "1")
                .queryParam("dateFrom", "2024-02-02")
                .queryParam("dateTo", "2024-02-04")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        String expected = "[{\"id\":2,\"name\":\"berthas room\",\"description\":\"B room\",\"beds\":12,\"price\":200,\"availableFrom\":\"2024-02-01T00:00:00.000+00:00\",\"availableTo\":\"2024-02-14T00:00:00.000+00:00\"}]";

        assertEquals(expected, response.getContentAsString());
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    @Test
    void shouldReturnReservationNumberAfterAddReservation() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/addReservation")
                .accept(MediaType.APPLICATION_JSON).content(exampleReservationJson)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(6, response.getContentAsString().length());
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
    }

    @Test
    void shouldReturnEmptyReservationList() throws Exception {
        // return empty because te query to did not match the existing reservation
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("http://localhost:9090/api/getAllReservations")
                .queryParam("checkInFrom", "2024-09-09")
                .queryParam("checkInTo", "2024-09-13")
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    void shouldCancelReservation() throws Exception {
        // create the reservation
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/addReservation")
                .accept(MediaType.APPLICATION_JSON).content(exampleReservationJson)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        String resNumber = response.getContentAsString();

        // cancel reservation
        requestBuilder = MockMvcRequestBuilders
                .post("http://localhost:9090/api/cancelReservation")
                .accept(MediaType.APPLICATION_JSON).content(resNumber)
                .contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(requestBuilder).andReturn();
        response = result.getResponse();

        assertEquals(resNumber + " successfully cancelled", response.getContentAsString());
        assertEquals(HttpStatus.OK.value(), response.getStatus());


    }

}
