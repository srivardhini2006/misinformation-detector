package com.misinfo.misinformationdetector;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AnalysisController {

    @GetMapping("/hello")
    public String hello() {
        return "Misinformation Detector is running!";
    }

    @PostMapping("/analyze")
    public String analyze(@RequestBody String text) {
        return "Received text: " + text;
    }
}
 