package engineer.hyper.agentic.props;

public interface Prompt {
    String DEFAULT_ORCHESTRATOR_PROMPT = """
            Analyze this task and break it down into 2-3 distinct approaches:
            
            Task: {task}
            
            Return your response in this JSON format:
            \\{
            "analysis": "Explain your understanding of the task and which variations would be valuable.
                         Focus on how each approach serves different aspects of the task.",
            "tasks": [
            	\\{
            	"type": "formal",
            	"description": "Write a precise, technical version that emphasizes specifications"
            	\\},
            	\\{
            	"type": "conversational",
            	"description": "Write an engaging, friendly version that connects with readers"
            	\\}
            ]
            \\}
            """;

    String DEFAULT_WORKER_PROMPT = """
            Generate content based on:
            Task: {original_task}
            Style: {task_type}
            Guidelines: {task_description}
            """;
}
