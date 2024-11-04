package ru.gozhan.pronotesapi.test.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.test.web.servlet.ResultHandler;

import java.util.Collections;

public class MockMvcUtil {

    /**
     * Formats and prints the request and response JSON in a structured format.
     *
     * @return ResultHandler that formats and prints request and response JSON.
     */
    public static ResultHandler prettyPrintRequestAndResponse() {
        return result -> {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            System.out.println("---- Request ----");
            System.out.println("Request URI: " + result.getRequest().getRequestURI());
            System.out.println("HTTP Method: " + result.getRequest().getMethod());
            System.out.println("Request Headers: ");
            Collections
                    .list(result.getRequest().getHeaderNames())
                    .forEach(header -> System.out.println(
                            "    " + header + ": " + result.getRequest().getHeader(header)
                    ));

            String requestBody = result.getRequest().getContentAsString();
            if (!requestBody.isEmpty()) {
                String prettyRequestBody = mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(mapper.readTree(requestBody));
                System.out.println("Formatted Request Body:\n" + prettyRequestBody);
            } else {
                System.out.println("Request Body: (empty)");
            }

            System.out.println("---- Response ----");
            System.out.println("Response Status: " + result.getResponse().getStatus());
            System.out.println("Response Headers: ");
            result.getResponse()
                    .getHeaderNames()
                    .forEach(header -> System.out.println(
                            "    " + header + ": " + result.getResponse().getHeader(header)
                    ));

            String responseBody = result.getResponse().getContentAsString();
            if (!responseBody.isEmpty()) {
                String prettyResponseBody = mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(mapper.readTree(responseBody));
                System.out.println("Formatted Response Body:\n" + prettyResponseBody);
            } else {
                System.out.println("Response Body: (empty)");
            }
        };
    }

}
