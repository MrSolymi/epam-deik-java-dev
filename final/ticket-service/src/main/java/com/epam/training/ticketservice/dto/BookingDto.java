package com.epam.training.ticketservice.dto;

import com.epam.training.ticketservice.model.Booking;
import com.epam.training.ticketservice.model.Seat;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class BookingDto {
    private final AccountDto accountDto;
    private final ScreeningDto screeningDto;
    private final List<Seat> seats;
    private final int price;

    public BookingDto(Booking booking) {
        accountDto = new AccountDto(booking.getAccount().getUsername(), booking.getAccount().getAccountType());
        screeningDto = new ScreeningDto(booking.getScreening());
        seats = booking.getSeats();
        price = booking.getPrice();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<String> seatList = new ArrayList<>();

        seats.forEach(x -> seatList.add(String.format("(%d,%d)", x.getRowIndex(), x.getColumnIndex())));



        return sb.append("Seats ").append(String.join(", ", seatList))
                .append(" on ").append(screeningDto.getMovieDto().getTitle())
                .append(" in room ").append(screeningDto.getRoomDto().getName())
                .append(" starting at ").append(screeningDto.getDate()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .append(" for ").append(price)
                .append(" HUF")
                .toString();
    }

    public String toStringBooked() {
        List<String> seatList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        seats.forEach(x -> seatList.add(String.format("(%d,%d)", x.getRowIndex(), x.getColumnIndex())));

        return sb.append("Seats booked: ").append(String.join(", ", seatList))
                .append("; the price for this booking is ")
                .append(price)
                .append(" HUF")
                .toString();
    }
}
