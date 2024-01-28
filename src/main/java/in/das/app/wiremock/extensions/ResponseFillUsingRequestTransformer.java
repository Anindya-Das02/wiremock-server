package in.das.app.wiremock.extensions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.extension.ResponseTransformerV2;
import com.github.tomakehurst.wiremock.http.Response;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import in.das.app.wiremock.utils.TransformerUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class ResponseFillUsingRequestTransformer implements ResponseTransformerV2 {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Response transform(Response response, ServeEvent serveEvent) {

        JsonNode requestJson;
        JsonNode responseJson;
        try {
            requestJson = mapper.readValue(serveEvent.getRequest().getBody(), JsonNode.class);
            responseJson = mapper.readValue(response.getBody(), JsonNode.class);
        } catch (IOException e) {
            log.error("[ERROR] error occurred while converting request/response into JsonNode.");
            throw new RuntimeException(e);
        }

        String transformedResponse = transformResponse(requestJson, responseJson);

        return Response.Builder.like(response)
                .body(transformedResponse)
                .status(response.getStatus())
                .headers(response.getHeaders())
                .build();
    }

    /*
        Below returned string will be used in "transformers".
        ex: "transformers" : [ "response-fill-using-request-transformer" ]
     */
    @Override
    public String getName() {
        return "response-fill-using-request-transformer";
    }

    /*
        If below method is not implemented (true by default), then this Transformer will be used globally.
        i.e., Transformer will be invoked even when not mentioned in "transformers" : [...]
     */
    @Override
    public boolean applyGlobally() {
        return false;
    }

    private String transformResponse(JsonNode requestNode, JsonNode responseNode) {
        List<String> nodesToReplace = TransformerUtils.findNodesToReplace(responseNode);
        String responseJson = "";
        try {
            responseJson = mapper.writeValueAsString(responseNode);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        for(String node : nodesToReplace){
            node = node.substring(2, node.length()-1);
            if(requestNode.has(node)){
                String nodeValue = requestNode.get(node).textValue();
                responseJson = responseJson.replaceAll("\\$\\{" + node + "}", nodeValue);
            } else {
                log.error("No substitution found for \"${{}}\" in request", node);
            }
        }
        return responseJson;
    }
}
