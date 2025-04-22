package io.eddie.backend.member.api;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @GetMapping("/user/test")
    public String userTest() {
        return "User Test!";
    }

    @GetMapping("/admin/test")
    public String adminTest() {
        return "Admin Test!";
    }

}
