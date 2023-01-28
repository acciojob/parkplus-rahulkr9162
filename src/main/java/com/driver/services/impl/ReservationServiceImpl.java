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
        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
        User user = userRepository3.findById(userId).get();
        List<Spot> spotList = parkingLot.getSpotList();
        int price = Integer.MAX_VALUE;
        SpotType spotType;
        Spot spotBooked = new Spot();
        boolean flag = false;

        if(numberOfWheels >0 && numberOfWheels <=2){
            spotType = SpotType.TWO_WHEELER;
        }
        else if(numberOfWheels >2 && numberOfWheels <=4){
            spotType = SpotType.FOUR_WHEELER;
        }
        else{
            spotType = SpotType.OTHERS;
        }

        for(Spot spot : spotList){
            if(spot.getSpotType() == spotType){
                if(timeInHours* spot.getPricePerHour() < price){
                    price = timeInHours* spot.getPricePerHour();
                    spotBooked = spot;
                    flag = true;
                }
            }
        }

        Reservation reservation = new Reservation();
        if(flag == true){
            reservation.setNumberOfHours(timeInHours);
            reservation.setSpot(spotBooked);
            reservation.setUser(user);
            reservationRepository3.save(reservation);

            spotBooked.setOccupied(true);
            spotBooked.setPricePerHour(timeInHours*spotBooked.getPricePerHour());

        }
        else throw  new Exception("Cannot make reservation");

        return  reservation;
    }
}
