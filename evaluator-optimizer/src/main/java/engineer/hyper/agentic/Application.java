package engineer.hyper.agentic;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Application {

    @Autowired
    private ChatClient chatClient;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @GetMapping("/evaluate")
    public String evaluate(@RequestParam String userInput) {
        EvaluatorOptimizer.RefinedResponse refinedResponse = new EvaluatorOptimizer(chatClient).loop("""
                <user input>
                Implement a Stack in Java with:
                1. push(x)
                2. pop()
                3. getMin()
                All operations should be O(1).
                All inner fields should be private and when used should be prefixed with 'this.'.
                </user input>
                """);
        return "FINAL OUTPUT:\n : " + refinedResponse;
    }
}