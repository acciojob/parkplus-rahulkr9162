package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;

    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
        try {
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
            User user = userRepository3.findById(userId).get();
            if (parkingLot == null || user == null) {
                throw new Exception("Cannot make reservation");
            }
            List<Spot> spotList = parkingLot.getSpotList();
            boolean spotAvailiability = false;
            for(Spot spot : spotList){
                if(!spot.getOccupied()){
                    spotAvailiability = true;
                    break;
                }
            }
            if(spotAvailiability == false){
                throw new Exception("Cannot make reservation");
            }

            int price = Integer.MAX_VALUE;
            SpotType spotType = null;
            Spot spotBooked = null;
            boolean flag = false;

            if (numberOfWheels > 0 && numberOfWheels <= 2) {
                spotType = SpotType.TWO_WHEELER;
            } else if (numberOfWheels > 2 && numberOfWheels <= 4) {
                spotType = SpotType.FOUR_WHEELER;
            } else if (numberOfWheels > 4) {
                spotType = SpotType.OTHERS;
            }

            for (Spot spot : spotList) {
                if (spot.getSpotType() == spotType) {
                    if (timeInHours * spot.getPricePerHour() < price) {
                        price = timeInHours * spot.getPricePerHour();
                        spotBooked = spot;
                        flag = true;
                    }
                }
            }

            if(flag == false){
                throw new Exception("Cannot make reservation");
            }

            Reservation reservation = new Reservation();
            if (flag == true) {
                reservation.setNumberOfHours(timeInHours);
                reservation.setSpot(spotBooked);
                reservation.setUser(user);
                // reservationRepository3.save(reservation);

                spotBooked.setOccupied(true);
                List<Reservation> reservationList = spotBooked.getReservationList();
                reservationList.add(reservation);
                spotBooked.setReservationList(reservationList);
                spotRepository3.save(spotBooked);
            }
            return reservation;
        } catch (Exception e) {
            return null;
        }

    }
}