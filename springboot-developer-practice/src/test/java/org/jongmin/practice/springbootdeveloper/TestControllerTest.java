package org.jongmin.practice.springbootdeveloper;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.aspectj.lang.annotation.Before;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//  스프링 부트 애플리케이션(프로젝트) 전체를 테스트할 때 사용하는 어노테이션으로
// 스프링 부트의 애플리케이션 컨텍스트를 로드하고, 통합 테스트 환경을 제공
@SpringBootTest
// MockMvc 객체를 자동으로 설정하고 구성해주는 어노테이션으로
// 테스트에서 MockMvc를 사용할 때 별도의 수동 설정 없이 자동으로 주입되고 사용할 수 있도록 함
@AutoConfigureMockMvc
class TestControllerTest {
    // Autowired는 미리 스프링 부트에서 생성하고 관리하고 있는 해당 객체를 주입할 때 사용하며
    // MockMvc는 스프링에서 제공하는 객체로, 웹 애플리케이션의 컨트롤러를 실제 서버를 띄우지 않고도 테스트할 수 있게 해줌
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    //애플리케이션의 데이터 접근 계층(Data Access Layer)을 구성하는 요소로,
    // 데이터베이스와의 CRUD(Create, Read, Update, Delete) 작업을 담당하는 인터페이스로
    // 간략하게 말하자면 JPA의 기능을 간단하게 사용할 수 있게 해주는 인터페이스.
    private MemberRepository memberRepository;

    @BeforeEach
    public void mockMvcSetUp(){
        // MockMvcBuilders : MockMvc 생성을 위한 여러 빌더들을 제공
        // webAppContextSetup : WebApplicationContext를 활용하여 MockMvc를 구성
        // 즉 아래의 전문은 MockMvc 빌더로 웹 애플리케이션 컨텍스트를 사용해 테스트 환경을 설정하는 작업을 의미.
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @AfterEach
    public void cleanUp(){
        memberRepository.deleteAll();
    }

    @DisplayName("getAllMembers: 아티클 조회 성공")
    @Test
    public void getAllMembers() throws Exception {

        // given : 테스트의 초기 상태 또는 전제 조건을 설정하는 단계로
        // 테스트를 실행하기 위한 필요한 데이터를 준비하는 단계임
        // `/test`라는 URL을 설정하고, 데이터베이스에 새로운 멤버(Member)를 저장하여 초기 상태를 만듬
        final String url = "/test"; // 테스트할 엔드포인트 URL을 설정함
        Member saveMember = memberRepository.save(new Member(1L, "홍길동"));
        // MemberRepository를 사용하여 새로운 Member 객체를 데이터베이스에 저장하여 초기 데이터를 준비함

        // when : 테스트의 실행 단계로
        // 준비된 상태에서 실제로 테스트하고자 하는 동작(또는 메서드 호출)을 수행하는 단계임
        // 참고로 ResultActions 클래스는 MockMvc에서 사용되는 객체로, HTTP 요청의 결과를 처리하고 검증할 수 있는 API를 제공
        // MockMvc를 사용하여 설정된 URL에 대해 GET 요청을 보냄
        final ResultActions result = mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));
        // MockMvc를 사용하여 `/test` 엔드포인트에 대한 GET 요청을 모의함
        // `accept(MediaType.APPLICATION_JSON)`는 서버가 JSON 형식으로 응답할 것을 기대함을 나타냄

        // then : 테스트의 검증 단계로
        // when 단계에서 수행된 동작의 결과가 예상한 대로인지 검증하는 단계임
        // 응답 상태가 200 OK인지 확인하고, 응답 JSON의 첫 번째 객체의 id와 name이 예상한 값과 일치하는지 확인함
        // 참고로 andExpect의 각 수행 단계는 동기적 단계로, fetch, then과는 달리 수행 성공 반환값을
        // 다음 함수에 반환하지 않음 (각 수행 단계의 결과를 받아서 사용하지 않음을 의미)
        result.andExpect(status().isOk())
                // 응답 상태 코드가 200(OK)인지 확인함
                .andExpect(jsonPath("$[0].id").value(saveMember.getId()))
                // jsonPath는 JSON 문서의 특정 경로를 지정해서 해당 경로에 위치한 데이터를 추출하는 메서드를 의미.
                // 참고로 $[0].id에서 $는 JSON 문서의 루트, 즉 시작점을 의미하고 [0].id는 0번째 요소의 id를 의미함
                // 그리고 value는 검증값을 설정하는 함수로 해당 테스트 클래스에서 가장 먼저 선언했던 saveMember를 값으로 설정함.
                // 결론만 말하면 응답 JSON에서 첫 번째 객체의 `id` 필드가 데이터베이스에 저장된 `saveMember` 객체의 `id`와 일치하는지 검증함
                .andExpect(jsonPath("$[0].name").value(saveMember.getName()));
                // 응답 JSON에서 첫 번째 객체의 `name` 필드가 데이터베이스에 저장된 `saveMember` 객체의 `name`과 일치하는지 검증함
    }
}