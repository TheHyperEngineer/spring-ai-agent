package engineer.hyper.agentic.controller;

import engineer.hyper.agentic.agent.ReflectionAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {

    @Autowired
    private ReflectionAgent reflectionAgent;

    @GetMapping("/ping")
    public String ping(@RequestParam(defaultValue = "Generate a Java implementation of the Merge Sort algorithm") String question) {
        return reflectionAgent.run(question, 2);
    }

}
