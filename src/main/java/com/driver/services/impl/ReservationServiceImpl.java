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
//        try {
//            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
//            User user = userRepository3.findById(userId).get();
//            if (parkingLot == null || user == null) {
//                throw new Exception("Cannot make reservation");
//            }
//            List<Spot> spotList = parkingLot.getSpotList();
//            boolean spotAvailiability = false;
//            for(Spot spot : spotList){
//                if(!spot.getOccupied()){
//                    spotAvailiability = true;
//                    break;
//                }
//            }
//            if(spotAvailiability == false){
//                throw new Exception("Cannot make reservation");
//            }
//
//            int price = Integer.MAX_VALUE;
//            SpotType spotType = null;
//            Spot spotBooked = null;
//            boolean flag = false;
//
//            if (numberOfWheels > 0 && numberOfWheels <= 2) {
//                spotType = SpotType.TWO_WHEELER;
//            } else if (numberOfWheels > 2 && numberOfWheels <= 4) {
//                spotType = SpotType.FOUR_WHEELER;
//            } else if (numberOfWheels > 4) {
//                spotType = SpotType.OTHERS;
//            }
//
//            for (Spot spot : spotList) {
//                if (spot.getSpotType() == spotType) {
//                    if (timeInHours * spot.getPricePerHour() < price) {
//                        price = timeInHours * spot.getPricePerHour();
//                        spotBooked = spot;
//                        flag = true;
//                    }
//                }
//            }
//
//            if(flag == false){
//                throw new Exception("Cannot make reservation");
//            }
//
//            Reservation reservation = new Reservation();
//                reservation.setNumberOfHours(timeInHours);
//                reservation.setSpot(spotBooked);
//                reservation.setUser(user);
//                // reservationRepository3.save(reservation);
//
//                spotBooked.setOccupied(true);
//                List<Reservation> reservationList = spotBooked.getReservationList();
//                reservationList.add(reservation);
//                user.getReservationList().add(reservation);
//                userRepository3.save(user);
//                spotRepository3.save(spotBooked);
//            return reservation;
//        } catch (Exception e) {
//            return null;
//        }
//
//    }
//}

        try {

            if (!userRepository3.findById(userId).isPresent() || !parkingLotRepository3.findById(parkingLotId).isPresent()) {
                throw new Exception("Cannot make reservation");
            }

            User user = userRepository3.findById(userId).get();
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();

            List<Spot> spotList = parkingLot.getSpotList();
            boolean checkForSpots = false;
            for (Spot spot : spotList) {
                if (!spot.getOccupied()) {
                    checkForSpots = true;
                    break;
                }
            }

            if (!checkForSpots) {
                throw new Exception("Cannot make reservation");
            }


            SpotType requestSpotType;

            if (numberOfWheels > 4) {
                requestSpotType = SpotType.OTHERS;
            } else if (numberOfWheels > 2) {
                requestSpotType = SpotType.FOUR_WHEELER;
            } else requestSpotType = SpotType.TWO_WHEELER;


            int minimumPrice = Integer.MAX_VALUE;

            checkForSpots = false;

            Spot spotChosen = null;

            for (Spot spot : spotList) {
                if (requestSpotType.equals(SpotType.OTHERS) && spot.getSpotType().equals(SpotType.OTHERS)) {
                    if (spot.getPricePerHour() * timeInHours < minimumPrice && !spot.getOccupied()) {
                        minimumPrice = spot.getPricePerHour() * timeInHours;
                        checkForSpots = true;
                        spotChosen = spot;
                    }
                } else if (requestSpotType.equals(SpotType.FOUR_WHEELER) && spot.getSpotType().equals(SpotType.OTHERS) ||
                        spot.getSpotType().equals(SpotType.FOUR_WHEELER)) {
                    if (spot.getPricePerHour() * timeInHours < minimumPrice && !spot.getOccupied()) {
                        minimumPrice = spot.getPricePerHour() * timeInHours;
                        checkForSpots = true;
                        spotChosen = spot;
                    }
                } else if (requestSpotType.equals(SpotType.TWO_WHEELER) && spot.getSpotType().equals(SpotType.OTHERS) ||
                        spot.getSpotType().equals(SpotType.FOUR_WHEELER) || spot.getSpotType().equals(SpotType.TWO_WHEELER)) {
                    if (spot.getPricePerHour() * timeInHours < minimumPrice && !spot.getOccupied()) {
                        minimumPrice = spot.getPricePerHour() * timeInHours;
                        checkForSpots = true;
                        spotChosen = spot;
                    }
                }

            }

            if (!checkForSpots) {
                throw new Exception("Cannot make reservation");
            }

            assert spotChosen != null;
            spotChosen.setOccupied(true);

            Reservation reservation = new Reservation();
            reservation.setNumberOfHours(timeInHours);
            reservation.setSpot(spotChosen);
            reservation.setUser(user);

            //Bidirectional
            spotChosen.getReservationList().add(reservation);
            user.getReservationList().add(reservation);

            userRepository3.save(user);
            spotRepository3.save(spotChosen);

            return reservation;
        }
        catch (Exception e){
            return null;
        }
    }
}