package engineer.hyper.agentic.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    @Autowired
    private ChatClient chatClient;

    @GetMapping("/ping")
    public String ping(@RequestParam(defaultValue = "What is current weather in Paris?") String name) {
        return chatClient
                .prompt(name)
                .call()
                .content();
    }

}
