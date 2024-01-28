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
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

@Slf4j
public class ResponseFillUsingParamTransformer implements ResponseTransformerV2 {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Response transform(Response response, ServeEvent serveEvent) {
        Map<String, Object> map = new HashMap<>();
        serveEvent.getTransformerParameters().forEach((param,obj) -> {
            map.put("${" + param + "}", obj);
        });
        JsonNode responseJson;
        try {
            responseJson = mapper.readValue(response.getBody(), JsonNode.class);
        } catch (IOException e) {
            log.error("[ERROR] error occurred while converting response into JsonNode.");
            throw new RuntimeException(e);
        }
        String transformedResponse = transformResponse(responseJson, map);
        return Response.Builder.like(response)
                .body(transformedResponse)
                .status(response.getStatus())
                .headers(response.getHeaders())
                .build();
    }

    @Override
    public String getName() {
        return "response-fill-using-param-transformer";
    }

    @Override
    public boolean applyGlobally() {
        return false;
    }

    private String transformResponse(JsonNode resJson, Map<String,Object> paramMap){
        List<String> nodesToReplace = TransformerUtils.findNodesToReplace(resJson);
        String responseJson = "";
        try {
            responseJson = mapper.writeValueAsString(resJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        for(String node : nodesToReplace){
            if(paramMap.get(node) instanceof String) {
                responseJson = responseJson.replaceAll("\\$\\{" + node.substring(2, node.length()-1) + "}", (String) paramMap.get(node));
            } else {
                log.error("complex (not string) value found for parameter: \"{}\", substitution skipped", node.substring(2, node.length()-1));
            }
        }
        return responseJson;
    }
}
