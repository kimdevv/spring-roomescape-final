package roomescape.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import roomescape.auth.annotation.NormalLogin;

@Controller
public class NormalController {

    @GetMapping
    public String normalHomePage() {
        return "index";
    }

    @NormalLogin
    @GetMapping("/reservation")
    public String normalReservationPage() {
        return "reservation";
    }

    @GetMapping("/login")
    public String normalLoginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String normalSignupPage() {
        return "signup";
    }

    @NormalLogin
    @GetMapping("/reservation/mine")
    public String normalMyReservationPage() {
        return "reservation-mine";
    }
}
