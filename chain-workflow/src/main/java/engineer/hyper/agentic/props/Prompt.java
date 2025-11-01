package engineer.hyper.agentic.props;

public interface Prompt {
    /**
     * Array of system prompts that define the transformation steps in the chain.
     * Each prompt acts as a gate that validates and transforms the output before
     * proceeding to the next step.
     */
    String[] DEFAULT_SYSTEM_PROMPTS = {

            // Step 1
            """
					Extract only the numerical values and their associated metrics from the text.
					Format each as'value: metric' on a new line.
					Example format:
					92: customer satisfaction
					45%: revenue growth""",
            // Step 2
            """
					Convert all numerical values to percentages where possible.
					If not a percentage or points, convert to decimal (e.g., 92 points -> 92%).
					Keep one number per line.
					Example format:
					92%: customer satisfaction
					45%: revenue growth""",
            // Step 3
            """
					Sort all lines in descending order by numerical value.
					Keep the format 'value: metric' on each line.
					Example:
					92%: customer satisfaction
					87%: employee satisfaction""",
            // Step 4
            """
					Format the sorted data as a markdown table with columns:
					| Metric | Value |
					|:--|--:|
					| Customer Satisfaction | 92% | """
    };
}
