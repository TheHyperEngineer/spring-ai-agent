package engineer.hyper.agentic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChainWorkFlowController {

    @Autowired
    private ChainWorkflow chainWorkflow;

    String report = """
            Q3 Performance Summary:
            Our customer satisfaction score rose to 92 points this quarter.
            Revenue grew by 45% compared to last year.
            Market share is now at 23% in our primary market.
            Customer churn decreased to 5% from 8%.
            New user acquisition cost is $43 per user.
            Product adoption rate increased to 78%.
            Employee satisfaction is at 87 points.
            Operating margin improved to 34%.
            """;

    @GetMapping("/chain-workflow")
    public String chainWorkflow() {
        return chainWorkflow.chain(report);
    }
}
