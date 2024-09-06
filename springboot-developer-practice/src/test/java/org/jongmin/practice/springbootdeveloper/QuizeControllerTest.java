package org.jongmin.practice.springbootdeveloper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class QuizeControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    // ObjectMapper는 Jackson 라이브러리에서 제공하는 클래스이며, Java 객체와 JSON 간의 변환을 처리하는 데 사용됨
    // 쉽게 말해 자바 객체를 JSON 형태로 직렬화 해주거나 그 반대로 리컨버트 해주는 클래스임.
    private ObjectMapper objectMapper;


    @DisplayName("Quiz() : GET / quiz?code=1 이면 응답 코드는 201")
    @Test
    public void Quize1() throws Exception {
    final String url = "/quiz";

    // mockMvc를 이용해 url을 지정하여 get 방식 으로 요청을 하는데, 이때 param(파라미터)는 code와 1
    // 즉 /quiz?code=1 형식으로 컨트롤러에 요청함을 의미
    // 또한 응답 받은 상태 코드가 Created인지를 확인하는 status 메서드와
    final ResultActions result = mockMvc.perform(get(url).param("code", "1"));
    result.andExpect(status().isCreated());
    }

    @DisplayName("Quiz() : GET / quiz?code=2 이면 응답 코드는 404, 응답 본문은 Bad Request!")
    @Test
    public void Quize2() throws Exception {
        final String url = "/quiz";
    final ResultActions result = mockMvc.perform(get(url).param("code", "2"));

    // 여기서는 status 뿐만 아니라 응답 본문이 Created!와 일치하는지를 확인하는 content 메서드를 사용함
    result.andExpect(status().isBadRequest())
          .andExpect(content().string("Bad Request!"));
    }

    @DisplayName("quiz() : POST / quiz?code=1 이면 응답 코드는 403, 응답 본문은 Forbidden!")
    @Test
    public void Quize3() throws Exception{
        final String url = "/quiz";

        // 'post(url)'는 'url'로 POST 요청을 보내는 동작을 수행함
        final ResultActions result = mockMvc.perform(post(url)
                // '.contentType(MediaType.APPLICATION_JSON)'는 요청의 콘텐츠 타입을 JSON으로 설정함
                .contentType(MediaType.APPLICATION_JSON)
                // 요청 본문에 'Code(1)' 객체를 JSON 형식으로 변환하여 설정
                .content(objectMapper.writer().writeValueAsString(new Code(1))));


        result.andExpect(status().isForbidden())
              .andExpect(content().string("Forbidden!"));
    }

    @DisplayName("quiz() : POST / quiz?code=13 이면 응답 코드는 200, 응답 본문은 OK!")
    @Test
    public void Quize4() throws Exception{
        final String url = "/quiz";

        final ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writer().writeValueAsString(new Code(13))));

        result.andExpect(status().isOk())
              .andExpect(content().string("Ok!"));
    }
}