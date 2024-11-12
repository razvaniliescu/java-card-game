package Output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class Output {
    ArrayNode output;
    ObjectMapper objectMapper;

    public Output(ArrayNode output, ObjectMapper objectMapper) {
        this.output = output;
        this.objectMapper = objectMapper;
    }
}
