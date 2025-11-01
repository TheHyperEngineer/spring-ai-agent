package engineer.hyper.agentic.service;

import engineer.hyper.agentic.dto.FinalResponse;
import engineer.hyper.agentic.dto.OrchestratorResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

import static engineer.hyper.agentic.props.Prompt.DEFAULT_ORCHESTRATOR_PROMPT;
import static engineer.hyper.agentic.props.Prompt.DEFAULT_WORKER_PROMPT;

/**
 * Pattern: <b>Orchestrator-workers</b>
 * <p/>
 * In this pattern, a central LLM (the orchestrator) dynamically breaks down
 * complex tasks into subtasks,
 * delegates them to worker LLMs, and uses a synthesizer to combine their
 * results. The orchestrator analyzes
 * the input to determine what subtasks are needed and how they should be
 * executed, while the workers focus
 * on their specific assigned tasks. Finally, the synthesizer integrates the
 * workers' outputs into a cohesive result.
 * <p/>
 * Key components:
 * <ul>
 * <li>Orchestrator: Central LLM that analyzes tasks and determines required
 * subtasks</li>
 * <li>Workers: Specialized LLMs that execute specific subtasks</li>
 * <li>Synthesizer: Component that combines worker outputs into final
 * result</li>
 * </ul>
 * <p/>
 * When to use: This pattern is well-suited for complex tasks where you can't
 * predict the subtasks needed upfront.
 * For example:
 * <ul>
 * <li>Coding tasks where the number of files to change and nature of changes
 * depend on the specific request</li>
 * <li>Search tasks that involve gathering and analyzing information from
 * multiple sources</li>
 * </ul>
 * <p/>
 * While topographically similar to parallelization, the key difference is its
 * flexibility—subtasks aren't
 * pre-defined, but dynamically determined by the orchestrator based on the
 * specific input. This makes it
 * particularly effective for tasks that require adaptive problem-solving and
 * coordination between multiple
 * specialized components.
 *
 * @author Christian Tzolov
 * @see <a href=
 * "https://www.anthropic.com/research/building-effective-agents">Building
 * effective agents</a>
 */
@Service
public class OrchestratorWorkers {

    private final ChatClient chatClient;
    private final String orchestratorPrompt = DEFAULT_ORCHESTRATOR_PROMPT;
    private final String workerPrompt = DEFAULT_WORKER_PROMPT;

    public OrchestratorWorkers(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * Processes a task using the orchestrator-workers pattern.
     * First, the orchestrator analyzes the task and breaks it down into subtasks.
     * Then, workers execute each subtask in parallel.
     * Finally, the results are combined into a single response.
     *
     * @param taskDescription Description of the task to be processed
     * @return WorkerResponse containing the orchestrator's analysis and combined
     * worker outputs
     * @throws IllegalArgumentException if taskDescription is null or empty
     */
    @SuppressWarnings("null")
    public FinalResponse process(String taskDescription) {
        Assert.hasText(taskDescription, "Task description must not be empty");

        // Step 1: Get orchestrator response
        OrchestratorResponse orchestratorResponse
                = this.chatClient
                .prompt()
                .user(u -> u.text(this.orchestratorPrompt).param("task", taskDescription))
                .call()
                .entity(OrchestratorResponse.class);

        System.out.println(
                String.format(
                        "\n=== ORCHESTRATOR OUTPUT ===\nANALYSIS: %s\n\nTASKS: %s\n",
                        orchestratorResponse.analysis(),
                        orchestratorResponse.tasks()
                )
        );

        // Step 2: Process each task
        List<String> workerResponses
                = orchestratorResponse
                .tasks()
                .stream()
                .map(
                        task ->
                                this.chatClient
                                        .prompt()
                                        .user(
                                                u -> u.text(this.workerPrompt)
                                                        .param("original_task", taskDescription)
                                                        .param("task_type", task.type())
                                                        .param("task_description", task.description()))
                                        .call()
                                        .content())
                .toList();

        System.out.println("\n=== WORKER OUTPUT ===\n" + workerResponses);

        return new FinalResponse(orchestratorResponse.analysis(), workerResponses);
    }

}