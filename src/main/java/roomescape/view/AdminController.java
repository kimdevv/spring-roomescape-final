package roomescape.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String adminHomePage() {
        return "admin/index";
    }

    @GetMapping("/reservation")
    public String adminReservationPage() {
        return "admin/reservation-legacy";
    }
}

