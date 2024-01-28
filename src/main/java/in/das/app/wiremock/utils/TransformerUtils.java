package in.das.app.wiremock.utils;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;

public interface TransformerUtils {

    static List<String> findNodesToReplace(JsonNode node){
        List<String> arr = new ArrayList<>();

        if(node == null){
            return arr;
        }

        if(node.isObject()){
            node.fields().forEachRemaining(entry -> {
                if(entry.getValue().isTextual() && entry.getValue().textValue().startsWith("${") && entry.getValue().textValue().endsWith("}")){
                    arr.add(entry.getValue().textValue());
                }
                else if(entry.getValue().isObject() || entry.getValue().isArray()){
                    arr.addAll(findNodesToReplace(entry.getValue()));
                }
            });
        }

        else if (node.isArray()){
            for (JsonNode arrayNode : node) {
                arr.addAll(findNodesToReplace(arrayNode));
            }
        }

        return arr;
    }
}
