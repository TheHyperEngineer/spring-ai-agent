package engineer.hyper.agentic;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// ------------------------------------------------------------
// EVALUATOR-OPTIMIZER
// ------------------------------------------------------------

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
        /*OllamaApi ollamaApi
                = OllamaApi.builder()
                .baseUrl("http://localhost:11434")
                .build();

        ModelManagementOptions modelManagementOptions
                = ModelManagementOptions.builder()
                .build();

        ChatModel chatModel
                = OllamaChatModel
                .builder()
                .ollamaApi(ollamaApi)
                .modelManagementOptions(modelManagementOptions)
                .build();*/

        /*ChatClient.Builder builder = ChatClient.builder(chatModel);
        ChatClient chatClient = builder.build();*/
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

	/*@Bean
	public CommandLineRunner commandLineRunner(ChatClient.Builder chatClientBuilder) {
		var chatClient = chatClientBuilder.build();
		return args -> {
			RefinedResponse refinedResponse = new EvaluatorOptimizer(chatClient).loop("""
					<user input>
					Implement a Stack in Java with:
					1. push(x)
					2. pop()
					3. getMin()
					All operations should be O(1).
					All inner fields should be private and when used should be prefixed with 'this.'.
					</user input>
					""");

			System.out.println("FINAL OUTPUT:\n : " + refinedResponse);
		};
	}*/
}