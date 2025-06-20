package roomescape.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NormalController {

    @GetMapping
    public String normalHomePage() {
        return "index";
    }

    @GetMapping("/reservation")
    public String normalReservationPage() {
        return "reservation";
    }
}
