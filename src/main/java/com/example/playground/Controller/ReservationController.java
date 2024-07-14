package com.example.playground.Controller;

import com.example.playground.Config.Auth.PrincipalDetail;
import com.example.playground.Model.dto.EventDto;
import com.example.playground.Model.reserveEntity.Reservation;
import com.example.playground.Model.reserveEntity.Room;
import com.example.playground.Service.ReservationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Controller
@RequestMapping("/reserve")
public class ReservationController
{
    @Autowired
    ReservationService reservationService;

    @GetMapping("/reservationform3")
    public String reserve_form3(Model model)
    {
        return "thymeleaf/reservationform3";
    }

    @GetMapping("/reservationform1")
    public String reserve_form(Authentication authentication, Model model)
    {
        model.addAttribute("room_list", reservationService.get_rooms());
        return "thymeleaf/reservationform1";
    }
/*
    @GetMapping("/reservation_status")
    public String reservation_status(Model model,@RequestParam(name="issuccess") String issuccess)
    {
        System.out.println("status 컨트롤러 이동 완료");
        List<EventDto> events = new ArrayList<>();
        for (Reservation reservation : reservationService.get_reservations())
        {
            LocalDate localDate = reservation.toInstant()  // Date -> Instant
                    .atZone(ZoneId.systemDefault())  // Instant -> ZonedDateTime
                    .toLocalDate();  // ZonedDateTime -> LocalDate

            // 3. Date -> LocalDateTime
            LocalDateTime localDateTime = date.toInstant()  // Date -> Instant
                    .atZone(ZoneId.systemDefault())  // Instant -> ZonedDateTime
                    .toLocalDateTime();  // ZonedDateTime -> LocalDateTime

        }
        model.addAttribute("issuccess", issuccess);//예약 성공 여부
        model.addAttribute("events", events); // 템플릿으로 전달

        return "thymeleaf/reservation_status";
    }
*/

    @PostMapping("/making_reservation")
    public String getRoom(@RequestParam(name= "startday") String startday,
                          @RequestParam(name= "endday") String endday,
                          @RequestParam(name= "roomId" ) Long roomId,
                          @AuthenticationPrincipal PrincipalDetail principaldetail,
                          RedirectAttributes ra
                          )
    {
        System.out.println("startday : "+startday);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date temp_date1 = null;
        Date temp_date2 = null;
        try
        {
            temp_date1 = dateFormat.parse(startday);
            temp_date2 = dateFormat.parse(endday);
        }
        catch (ParseException e)
        {
            System.err.println("Error parsing date: " + e.getMessage());
        }

        Room temp_room = reservationService.get_room(roomId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 방입니다."));
            // 값이 존재할 때 처리 (Optional클래스는 null값을 받을 수 있다)
            //https://xxeol.tistory.com/20(람다식 관련 글)


        String result = reservationService.making_reservation(temp_date1, temp_date2, temp_room, principaldetail.getMember());
        ra.addAttribute("issuccess", result);
        System.out.println("리다이렉트 전");
        return "redirect:/reserve/reservation_status";//예약 현황을 보는 페이지
    }

    /*@PostMapping("/making_reservation")
    public String handleReservation(@RequestParam("startday") String startDay,
                                    @RequestParam("endday") String endDay,
                                    @RequestParam("roomId") Long roomId) {
        System.out.println("POST 이동 성공!!!!!!!!!!!!!");
        return "redirect:/reservation-success"; // 예약 성공 페이지로 리다이렉트
    }*/


    @GetMapping("/calendar")
    public String showCalendar(Model model)
    {
        List<EventDto> events = new ArrayList<>();
        events.add(new EventDto("Event 1", LocalDate.of(2024, 7, 15), LocalDate.of(2024, 7, 16), "#FF5733"));
        events.add(new EventDto("Event 2", LocalDate.of(2024, 7, 20), LocalDate.of(2024, 7, 22), "#007BFF"));
        events.add(new EventDto("Event 3", LocalDate.of(2024, 7, 25), LocalDate.of(2024, 7, 27), "#28A745"));

        model.addAttribute("events", events);
        return "thymeleaf/reservationform3"; // Thymeleaf 템플릿 이름
    }



}
