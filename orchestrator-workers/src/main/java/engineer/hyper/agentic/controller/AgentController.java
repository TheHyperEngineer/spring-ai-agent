package engineer.hyper.agentic.controller;


import engineer.hyper.agentic.dto.FinalResponse;
import engineer.hyper.agentic.service.OrchestratorWorkers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/agent")
public class AgentController {

    @Autowired
    private OrchestratorWorkers orchestratorWorkers;

    @GetMapping
    public FinalResponse execute(@RequestParam(value = "task", defaultValue = "Write a product description for a new eco-friendly water bottle") String task) {
        return orchestratorWorkers.process(task);
    }
}
