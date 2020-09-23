package demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GraphqlWineryTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper om;

    @Test
    @Order(0)
    void listFoods() throws Exception {

        var expectedResponse = Map.of("data", Map.of("wines", List.of(
                Map.of("id", 1, "name", "Wine 1", "rating", 10),
                Map.of("id", 2, "name", "Wine 2", "rating", 2),
                Map.of("id", 3, "name", "Wine 3", "rating", 7)
        )));

        mockMvc.perform(post("/graphql")
                .with(jwt())
                .content("{\"query\":\"{ wines { id name rating } }\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(om.writeValueAsString(expectedResponse)))
                .andReturn();
    }

    @Test
    @Order(1)
    void addAndRemoveFood() throws Exception {

        var expectedBefore = Map.of("data", Map.of("wines", List.of(
                Map.of("id", 1, "name", "Wine 1", "rating", 10),
                Map.of("id", 2, "name", "Wine 2", "rating", 2),
                Map.of("id", 3, "name", "Wine 3", "rating", 7)
        )));

        var expectedAfter = Map.of("data", Map.of("wines", List.of(
                Map.of("id", 1, "name", "Wine 1", "rating", 10),
                Map.of("id", 2, "name", "Wine 2", "rating", 2),
                Map.of("id", 3, "name", "Wine 3", "rating", 7),
                Map.of("id", 4, "name", "Wine 4", "rating", 0)
        )));

        String expectedJsonBefore = om.writeValueAsString(expectedBefore);
        String expectedJsonAfter = om.writeValueAsString(expectedAfter);

        // List wines
        mockMvc.perform(post("/graphql")
                .with(jwt())
                .content("{\"query\":\"{ wines { id name rating } }\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJsonBefore))
                .andReturn();

        // Add 'New Wine'
        mockMvc.perform(post("/graphql")
                .with(jwt())
                .content("{\"query\":\"mutation { saveWine(wine: { name: \\\"Wine 4\\\" }) { id name rating } }\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"data\":{\"saveWine\":{\"id\":4,\"name\":\"Wine 4\"}}}"))
                .andReturn();

        // List wines, expect 'New Wine' to be there
        mockMvc.perform(post("/graphql")
                .with(jwt())
                .content("{\"query\":\"{ wines { id name rating} }\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJsonAfter))
                .andReturn();

        // Remove 'New Wine'
        mockMvc.perform(post("/graphql")
                .with(jwt())
                .content("{\"query\":\"mutation { deleteWine(id: 4) }\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // List wines, expect 'New Wine' to not be there
        mockMvc.perform(post("/graphql")
                .with(jwt())
                .content("{\"query\":\"{ wines { id name rating} }\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJsonBefore))
                .andReturn();
    }
}