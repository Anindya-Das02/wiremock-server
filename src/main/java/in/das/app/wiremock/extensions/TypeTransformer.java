package in.das.app.wiremock.extensions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseTransformerV2;
import com.github.tomakehurst.wiremock.http.Response;
import com.github.tomakehurst.wiremock.stubbing.ServeEvent;
import in.das.app.wiremock.utils.TransformerUtils;
import in.das.app.wiremock.utils.TypedPlaceHolder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

@Slf4j
public class TypeTransformer implements ResponseTransformerV2 {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Response transform(Response response, ServeEvent serveEvent) {
        Map<String,TypedPlaceHolder> placeHolderMap = new HashMap<>();
        JsonNode responseJson;
        try {
            responseJson = mapper.readValue(response.getBody(), JsonNode.class);
        } catch (IOException e) {
            log.error("[ERROR] error occurred while converting response into JsonNode.");
            throw new RuntimeException(e);
        }
        List<String> arr = TransformerUtils.findNodesToReplace(responseJson);
        System.out.println(arr);
        arr.forEach(node -> {
            if(node.contains("|")){
                String[] args = node.split("\\|");
                // ${status|int} => type = int
                String param = args[0].replace("${","").trim();
                String pType = args[1].replace("}","").trim();
                placeHolderMap.put(param, new TypedPlaceHolder(param,pType,node));
            }
            else{
                // ${status} => default type = string
                String param = node.replace("${","").replace("}","").trim();
                placeHolderMap.put(param, new TypedPlaceHolder(param,"string",node));
            }
        });

        placeHolderMap.forEach((k,v) -> {
            System.out.println(k + " : " + v);
        });

        String jsonStr = "";
        try {
            jsonStr = mapper.writeValueAsString(responseJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String transformedResponse = transformResponse(jsonStr, placeHolderMap, serveEvent.getTransformerParameters());

        return Response.Builder.like(response)
                .body(transformedResponse)
                .status(response.getStatus())
                .headers(response.getHeaders())
                .build();
    }

    @Override
    public boolean applyGlobally() {
        return false;
    }

    @Override
    public String getName() {
        return "response-type-transformer";
    }

    private String transformResponse(String jsonResponse, Map<String, TypedPlaceHolder> placeHolderMap, Parameters parameters){
        for (Map.Entry<String, TypedPlaceHolder> entry : placeHolderMap.entrySet()) {
            if ("string".equalsIgnoreCase(entry.getValue().getType())) {
                jsonResponse = jsonResponse.replaceAll(Pattern.quote(entry.getValue().getCompletePlaceHolder()), parameters.getString(entry.getKey()));
            }
            else if ("int".equalsIgnoreCase(entry.getValue().getType())) {
                jsonResponse = jsonResponse.replaceAll(Pattern.quote("\"" + entry.getValue().getCompletePlaceHolder() + "\""), String.valueOf(parameters.getInt(entry.getKey())));
            }
            else if ("bool".equalsIgnoreCase(entry.getValue().getType()) || "boolean".equalsIgnoreCase(entry.getValue().getType())) {
                jsonResponse = jsonResponse.replaceAll(Pattern.quote("\"" + entry.getValue().getCompletePlaceHolder() + "\""), String.valueOf(parameters.getBoolean(entry.getKey())));
            }
        }
        return jsonResponse;
    }
}
