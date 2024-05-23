package com.example.shopapp.responses;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.Data;

import java.io.IOException;

@Data
@Builder
public class ResponseUnauthorized {
    public static void writeJsonResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(new ResponseBody(status, message));
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }

    private static class ResponseBody {
        public int status;
        public String message;

        public ResponseBody(int status, String message) {
            this.status = status;
            this.message = message;
        }
    }
}
