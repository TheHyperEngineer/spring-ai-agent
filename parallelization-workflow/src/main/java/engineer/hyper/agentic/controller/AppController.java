package engineer.hyper.agentic.controller;

import engineer.hyper.agentic.service.ParallelizationWorkflow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AppController {

    @Autowired
    private ParallelizationWorkflow parallelizationWorkflow;

    @GetMapping("/run-workflow")
    public List<String> runWorkflow() {
        return parallelizationWorkflow.parallel("""
                        Analyze how market changes will impact this stakeholder group.
                        Provide specific impacts and recommended actions.
                        Format with clear sections and priorities.
                        """,
                List.of(
                        """
                                Customers:
                                - Price sensitive
                                - Want better tech
                                - Environmental concerns
                                """,

                        """
                                Employees:
                                - Job security worries
                                - Need new skills
                                - Want clear direction
                                """,

                        """
                                Investors:
                                - Expect growth
                                - Want cost control
                                - Risk concerns
                                """,

                        """
                                Suppliers:
                                - Capacity constraints
                                - Price pressures
                                - Tech transitions
                                """),
                4);
    }
}
