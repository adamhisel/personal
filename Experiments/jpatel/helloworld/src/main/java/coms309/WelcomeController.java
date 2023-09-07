package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309";
    }

    @GetMapping("/{name}/{number}")
    public String welcome(@PathVariable String name, @PathVariable String number) {
        return ( "Your name is:  " + name + "Your age is: "+ number);
    }
}
