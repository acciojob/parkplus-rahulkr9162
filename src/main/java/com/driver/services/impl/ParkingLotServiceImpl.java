package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {
        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        parkingLotRepository1.save(parkingLot);
        return parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        Spot spot = new Spot();
        spot.setParkingLot(parkingLot);
        spot.setPricePerHour(pricePerHour);

        if(numberOfWheels > 0 && numberOfWheels <=2) {
            spot.setSpotType(SpotType.TWO_WHEELER);
        }
        else if(numberOfWheels >2 && numberOfWheels <=4){
            spot.setSpotType(SpotType.FOUR_WHEELER);
        }
        else if(numberOfWheels >4){
            spot.setSpotType(SpotType.OTHERS);
        }

        List<Spot> spotList = parkingLot.getSpotList();
        spotList.add(spot);
        parkingLot.setSpotList(spotList);

        parkingLotRepository1.save(parkingLot);

        return spot;

    }

    @Override
    public void deleteSpot(int spotId) {
//        if (spotRepository1.findById(spotId).get() != null) {
//            Spot spot = spotRepository1.findById(spotId).get();
//            ParkingLot parkingLot = spot.getParkingLot();
//            List<Spot> spotList = parkingLot.getSpotList();
//            for (Spot spot1 : spotList) {
//                if (spot1.getId() == spot.getId()) {
//                    spotList.remove(spot1);
//                    break;
//                }
//            }
//            parkingLot.setSpotList(spotList);
//
//            spotRepository1.deleteById(spotId);
//
//            parkingLotRepository1.save(parkingLot);
//        }
        spotRepository1.deleteById(spotId);
    }



    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {
        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
        List<Spot> spotList = parkingLot.getSpotList();
        Spot newSpot = new Spot();
        for(Spot spot : spotList){
            if(spot.getId() == spotId){
                spot.setPricePerHour(pricePerHour);
                spotRepository1.save(spot);
                newSpot = spot;
                break;
            }
        }
        parkingLot.setSpotList(spotList);

        parkingLotRepository1.save(parkingLot);

        return newSpot;



    }

    @Override
    public void deleteParkingLot(int parkingLotId) {
       // List<Spot> spotList = parkingLotRepository1.findById(parkingLotId).get().getSpotList();
        parkingLotRepository1.deleteById(parkingLotId);

    }
}
