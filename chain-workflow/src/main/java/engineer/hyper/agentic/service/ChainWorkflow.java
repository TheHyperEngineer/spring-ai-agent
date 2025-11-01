package engineer.hyper.agentic.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static engineer.hyper.agentic.props.Prompt.DEFAULT_SYSTEM_PROMPTS;

/**
 * Implements the Prompt Chaining workflow pattern for decomposing complex tasks
 * into a sequence
 * of LLM calls where each step processes the output of the previous one.
 *
 * <p>
 * This implementation demonstrates a four-step workflow for processing
 * numerical data in text:
 * <ol>
 * <li>Extract numerical values and metrics</li>
 * <li>Standardize to percentage format</li>
 * <li>Sort in descending order</li>
 * <li>Format as markdown table</li>
 * </ol>
 * <p>
 * <p/>
 * When to use this workflow: This workflow is ideal for situations where the
 * task can be easily and cleanly decomposed into fixed subtasks. The main goal
 * is to trade off latency for higher accuracy, by making each LLM call an
 * easier task.
 *
 * @author Christian Tzolov
 * @see org.springframework.ai.chat.client.ChatClient
 * @see <a href="https://docs.spring.io/spring-ai/reference/1.0/api/chatclient.html">Spring AI ChatClient</a>
 * @see <a href=
 * "https://www.anthropic.com/research/building-effective-agents">Building
 * Effective Agents</a>
 */
@Service
public class ChainWorkflow {

    @Autowired
    private ChatClient chatClient;

    private final String[] systemPrompts = DEFAULT_SYSTEM_PROMPTS;

    /**
     * Constructs a new instance of the Prompt Chaining workflow with the specified
     * chat client and default system prompts.
     *
     * @param chatClient the Spring AI chat client used to make LLM calls
     */
/*    public ChainWorkflow() {
        OllamaApi ollamaApi
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
                .build();
        ChatClient.Builder builder = ChatClient.builder(chatModel);

        this(builder.build(), DEFAULT_SYSTEM_PROMPTS);
    }*/

    /**
     * Executes the prompt chaining workflow by processing the input text through
     * a series of LLM calls, where each call's output becomes the input for the
     * next step.
     *
     * <p>
     * The method prints the intermediate results after each step to show the
     * progression of transformations through the chain.
     *
     * @param chatClient the Spring AI chat client used to make LLM calls
     * @param userInput  the input text containing numerical data to be processed
     * @return the final output after all steps have been executed
     */
    public String chain(String userInput) {

        int step = 0;
        String response = userInput;
        System.out.println(String.format("\nSTEP %s:\n %s", step++, response));

        for (String prompt : systemPrompts) {

            // 1. Compose the input using the response from the previous step.
            String input = String.format("{%s}\n {%s}", prompt, response);

            // 2. Call the chat client with the new input and get the new response.
            response = chatClient.prompt(input).call().content();

            System.out.println(String.format("\nSTEP %s:\n %s", step++, response));
        }

        return response;
    }

    public String question(String question) {

        SystemMessage systemMessage = SystemMessage.builder().text("""
                You are a science teacher for teenagers. Your name is Dexter.
                You explain science concepts in a simple, concise and direct way.
                """).build();
        UserMessage userMessage = UserMessage.builder().text(question).build();
        Prompt prompt = Prompt.builder().messages(systemMessage, userMessage).build();
        return chatClient.prompt(prompt).call().content();
    }
}
