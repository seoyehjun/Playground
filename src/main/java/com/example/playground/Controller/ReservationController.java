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


    //테스트용 템플릿
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

    public String get_color(int roomId)
    {
        String roomColor;
        switch (roomId)
        {
            case 1:
                roomColor = "#FF5733"; // 방 1의 색상
                break;
            case 2:
                roomColor = "#00AABB"; // 방 2의 색상
                break;
            case 3:
                roomColor = "#FFD700"; // 방 3의 색상
                break;
            default:
                roomColor = "#FFFFFF"; // 기본 색상 (방 Id에 해당하는 색상이 없을 경우)
                break;
        }
        return roomColor;
    }

    // 1. reservation1에서 방과 날을 선택하면 making_reservation으로 넘어간다
    @GetMapping("/reservationform1")
    public String reserve_form(Authentication authentication, Model model)
    {
        model.addAttribute("room_list", reservationService.get_rooms());
        return "thymeleaf/reservationform1";
    }

    // 2. 여기서 DB에 저장해준뒤 reservation_status 링크로 넘어가서 예약 현황을 보여준다.
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

    // 3. 예약 현황을 보여주는 페이지로 이동
    @GetMapping("/reservation_status")
    public String reservation_status(Model model,@RequestParam(name="issuccess") String issuccess)
    {
        System.out.println("status 컨트롤러 이동 완료");
        List<EventDto> events = new ArrayList<>();
        String[] temp1 = new String[3];
        String[] temp2 = new String[3];
        String temp1_1 = null;
        String temp2_1 = null;
        String color;
        for (Reservation reservation : reservationService.get_reservations())
        {
            temp1_1 = reservation.getStartDay()+"";
            temp2_1 = reservation.getEndDay()+"";
            temp1 = temp1_1.split(" ")[0].split("-");
            temp2 = temp2_1.split(" ")[0].split("-");//시간부분은 필요없으니 분리

            events.add(new EventDto("reservation"+reservation.getRoom().getRoomId(),
                    LocalDate.of(Integer.parseInt(temp1[0]),Integer.parseInt(temp1[1]),Integer.parseInt(temp1[2])),
                    LocalDate.of(Integer.parseInt(temp2[0]),Integer.parseInt(temp2[1]),Integer.parseInt(temp2[2])),
                    get_color(  (int)reservation.getRoom().getRoomId()  ) ) )         ;

            System.out.println("EventDto : "+ new EventDto("reservation"+reservation.getReservationId(),
                    LocalDate.of(Integer.parseInt(temp1[0]),Integer.parseInt(temp1[1]),Integer.parseInt(temp1[2])),
                    LocalDate.of(Integer.parseInt(temp2[0]),Integer.parseInt(temp2[1]),Integer.parseInt(temp2[2])),
                    get_color(  (int)reservation.getRoom().getRoomId()  ) ));

        }
        model.addAttribute("issuccess", issuccess);//예약 성공 여부
        model.addAttribute("events", events); // 템플릿으로 전달

        return "thymeleaf/reservation_status";
    }
    /*@PostMapping("/making_reservation")
    public String handleReservation(@RequestParam("startday") String startDay,
                                    @RequestParam("endday") String endDay,
                                    @RequestParam("roomId") Long roomId) {
        System.out.println("POST 이동 성공!!!!!!!!!!!!!");
        return "redirect:/reservation-success"; // 예약 성공 페이지로 리다이렉트
    }*/






}
