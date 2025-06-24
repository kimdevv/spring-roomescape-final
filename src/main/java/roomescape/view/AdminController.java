package roomescape.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import roomescape.auth.annotation.AdminLogin;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @AdminLogin
    @GetMapping
    public String adminHomePage() {
        return "admin/index";
    }

    @AdminLogin
    @GetMapping("/reservation")
    public String adminReservationPage() {
        return "admin/reservation-new";
    }

    @AdminLogin
    @GetMapping("/reservation/time")
    public String adminReservationTimePage() {
        return "admin/time";
    }

    @AdminLogin
    @GetMapping("/reservation/theme")
    public String adminThemePage() {
        return "admin/theme";
    }

    @AdminLogin
    @GetMapping("/reservation/waiting")
    public String adminWaitingReservationPage() {
        return "admin/waiting";
    }
}

