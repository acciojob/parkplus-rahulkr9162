package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.model.Spot;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation = reservationRepository2.findById(reservationId).get();
        Spot spot = reservation.getSpot();
        Payment payment =reservation.getPayment();
        PaymentMode paymentMode;
        mode = mode.toUpperCase();

        if(spot.getPricePerHour()*reservation.getNumberOfHours() <= amountSent){
            payment.setPaymentCompleted(true);
        }
        else{
            throw new Exception("Insufficient Amount");
        }


        if(mode == "CASH"){
            payment.setPaymentMode(PaymentMode.CASH);
        }
        else if(mode == "CARD"){
            payment.setPaymentMode(PaymentMode.CARD);
        }
        else if(mode == "UPI"){
            payment.setPaymentMode(PaymentMode.UPI);
        }
        else{
            payment.setPaymentCompleted(false);
            throw new Exception("Payment mode not detected");
        }

        payment.setReservation(reservation);
        reservation.setPayment(payment);
        reservationRepository2.save(reservation);

        return payment;
    }
}
