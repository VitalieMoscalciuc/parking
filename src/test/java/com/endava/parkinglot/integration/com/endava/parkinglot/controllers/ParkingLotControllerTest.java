package com.endava.parkinglot.integration.com.endava.parkinglot.controllers;

import com.endava.parkinglot.enums.WorkingDays;
import com.endava.parkinglot.integration.com.endava.parkinglot.util.Prerequisites;
import com.endava.parkinglot.model.ParkingLevelEntity;
import com.endava.parkinglot.model.ParkingLotEntity;
import com.endava.parkinglot.model.ParkingSpaceEntity;
import com.endava.parkinglot.model.UserEntity;
import com.endava.parkinglot.model.WorkingDaysEntity;
import com.endava.parkinglot.model.repository.ParkingLevelRepository;
import com.endava.parkinglot.model.repository.ParkingLotRepository;
import com.endava.parkinglot.model.repository.ParkingSpaceRepository;
import com.endava.parkinglot.model.repository.WorkingDaysRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ParkingLotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Prerequisites prerequisites;

    @Autowired
    private ParkingLotRepository parkingRepository;

    @Autowired
    private WorkingDaysRepository workingDaysRepository;

    @Autowired
    private ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    private ParkingLevelRepository parkingLevelRepository;

    private String adminToken;

    private HttpHeaders headers;

    @BeforeEach
    void init() {
        prerequisites.clear();
        UserEntity userAdmin = prerequisites.createUserAdmin();
        adminToken = prerequisites.generateAccessToken(userAdmin.getEmail());
        headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + adminToken);
    }

    @Test
    void parkingLotRequest_withValidData_shouldReturnCreated() throws Exception {
        this.mockMvc
                .perform(post("/api/parkingLot/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .content("""
                                  {
                                  "name" : "NewParking444",
                                  "address" : "asdfghjkl",
                                  "workingHours" : "00:00-00:00",
                                  "workingDays" : ["MONDAY", "WEDNESDAY", "FRIDAY"],
                                  "operatesNonStop" : false,
                                  "levels": [
                                    {
                                      "floor": "A",
                                      "numberOfSpaces": 5
                                    },
                                    {
                                      "floor": "B",
                                      "numberOfSpaces": 5
                                    }
                                  ]
                                }
                                """)
                )
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.name").value("NewParking444"))
                .andExpect(jsonPath("$.address").value("asdfghjkl"));

        Optional<ParkingLotEntity> result = parkingRepository.findByName("NewParking444");

        Long lotId = result.get().getId();
        List<WorkingDaysEntity> days = workingDaysRepository.getWorkingsDaysByLotName(lotId);
        List<ParkingLevelEntity> levels = parkingLevelRepository.getAllByParkingLotId(lotId);

        List<ParkingSpaceEntity> spaces = new ArrayList<>();
        for (ParkingLevelEntity parkingLevel : levels) {
            spaces.addAll(parkingSpaceRepository.getAllByParkingLevelName(lotId, parkingLevel.getId()));
        }

        String name = "NewParking444";
        String address = "asdfghjkl";
        LocalTime beginWorkingHour = LocalTime.of(0, 0, 0);
        LocalTime endWorkingHour = (LocalTime.of(23, 59, 59));

        assertNotNull(result.get());
        assertAll(
                () -> assertTrue(days.stream().anyMatch(day -> day.getNameOfDay().equals(WorkingDays.FRIDAY))),
                () -> assertTrue(days.stream().anyMatch(day -> day.getNameOfDay().equals(WorkingDays.WEDNESDAY))),
                () -> assertTrue(days.stream().anyMatch(day -> day.getNameOfDay().equals(WorkingDays.MONDAY)))
        );
        assertAll(
                () -> assertEquals(address, result.get().getAddress()),
                () -> assertEquals(beginWorkingHour, result.get().getBeginWorkingHour()),
                () -> assertEquals(endWorkingHour, result.get().getEndWorkingHour())
        );
        assertEquals("A", levels.get(0).getFloor());
        assertEquals("B", levels.get(1).getFloor());
        assertEquals(5, levels.get(0).getNumberOfSpaces());
        assertEquals(5, levels.get(1).getNumberOfSpaces());

        int expectedNumberOfSpaces = 10;
        assertEquals(expectedNumberOfSpaces, spaces.size());
    }

    @Test
    void parkingLotRequest_withInvalidData_shouldReturnBadRequest() throws Exception {
        this.mockMvc
                .perform(post("/api/parkingLot/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .headers(headers)
                        .content("""
                                  {
                                  "name" : "NewParking444",
                                  "address" : "asdfghjkl",
                                  "workingHours" : "00:00-00:00",
                                  "workingDays" : ["MONDAY", "WEDNESDAY", "FRIDAY"],
                                  "operatesNonStop" : false,
                                  "levels": [
                                    {
                                      "floor": "A",
                                      "numberOfSpaces": 5
                                    },
                                    {
                                      "floor": "A",
                                      "numberOfSpaces": 5
                                    }
                                  ]
                                }
                                """)
                )
                .andExpect(status().is(400));
    }
}