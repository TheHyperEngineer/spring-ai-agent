package engineer.hyper.agentic.service;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.model.ModelOptionsUtils;
import org.springframework.stereotype.Component;

@Component
public class MyLogAdvisor implements BaseAdvisor {

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        print("REQUEST", chatClientRequest.prompt().getInstructions());
        return chatClientRequest;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        print("RESPONSE", chatClientResponse.chatResponse().getResults());
        return chatClientResponse;
    }

    private void print(String label, Object object) {
        System.out.println(label + ":" + ModelOptionsUtils.toJsonString(object) + "\n");
    }

}