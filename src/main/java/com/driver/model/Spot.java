package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "spots")
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Enumerated(EnumType.STRING)
    private SpotType spotType;

    private int PricePerHour;

    private boolean occupied;

    @ManyToOne
    @JoinColumn
    ParkingLot parkingLot;

    @OneToMany(mappedBy = "spot", cascade = CascadeType.ALL)
    List<Reservation> reservationList = new ArrayList<>();

    public Spot() {
    }

    public Spot(int id, SpotType spotType, int pricePerHour, boolean occupied, ParkingLot parkingLot, List<Reservation> reservationList) {
        this.id = id;
        this.spotType = spotType;
        PricePerHour = pricePerHour;
        this.occupied = occupied;
        this.parkingLot = parkingLot;
        this.reservationList = reservationList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SpotType getSpotType() {
        return spotType;
    }

    public void setSpotType(SpotType spotType) {
        this.spotType = spotType;
    }

    public int getPricePerHour() {
        return PricePerHour;
    }

    public void setPricePerHour(int pricePerHour) {
        PricePerHour = pricePerHour;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public ParkingLot getParkingLot() {
        return parkingLot;
    }

    public void setParkingLot(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public List<Reservation> getReservationList() {
        return reservationList;
    }

    public void setReservationList(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }
}
