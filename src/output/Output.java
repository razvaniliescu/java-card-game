package output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Parent class for the output subclasses
 */
public class Output {
    protected ArrayNode output;
    protected ObjectMapper objectMapper;
    protected ObjectNode objectNode;

    public Output(final ArrayNode output, final ObjectMapper objectMapper, final ObjectNode objectNode) {
        this.output = output;
        this.objectMapper = objectMapper;
        this.objectNode = objectNode;
    }
}
