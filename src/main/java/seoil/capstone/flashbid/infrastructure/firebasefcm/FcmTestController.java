package seoil.capstone.flashbid.infrastructure.firebasefcm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("fcm")
public class FcmTestController {
    private final FcmService fcmService;

    @PostMapping("/token")
    public String sendToToken(@RequestParam String token,
                              @RequestParam String title,
                              @RequestParam String body,
                              @RequestParam(required = false) String link) throws Exception {
        return fcmService.sendToToken(token, title, body, link);
    }

    @PostMapping("/topic")
    public String sendToTopic(@RequestParam String topic,
                              @RequestParam String title,
                              @RequestParam String body,
                              @RequestParam(required = false) String link) throws Exception {
        return fcmService.sendToTopic(topic, title, body,link);
    }

    @GetMapping("/subscribe")
    public String subscribeToTopic(@RequestParam String topic,
                                   @RequestParam String token) {
        return fcmService.subscribeToTopic(topic, token);
    }
}
