package org.jongmin.practice.springbootdeveloper;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// @RestController: 이 클래스가 RESTful 웹 서비스의 컨트롤러임을 나타냄
// @RestController는 @Controller와 @ResponseBody의 조합으로, 모든 메서드가 JSON 또는 XML 형식으로 응답을 반환하도록 함
@RestController
public class QuizeController {

    // @GetMapping("/quiz"): HTTP GET 요청을 처리하기 위한 메서드를 정의
    // "/quiz" 경로로 들어오는 GET 요청을 이 메서드가 처리함
    @GetMapping("/quiz")
    public ResponseEntity<String> quiz1(@RequestParam("code") int code) {
        // @RequestParam("code"): 요청의 쿼리 파라미터 "code" 값을 int 타입의 변수로 받음
        // 예: /quiz?code=1 일 때, code 파라미터 값이 1로 설정됨

        // switch 문을 사용하여 code 값에 따라 다른 응답을 반환
        switch (code) {
            case 1:
                // code 값이 1일 때, HTTP 상태 코드 201 Created와 "Created!" 메시지를 응답으로 반환
                return ResponseEntity.created(null).body("Created!");
            case 2:
                // code 값이 2일 때, HTTP 상태 코드 400 Bad Request와 "Bad Request!" 메시지를 응답으로 반환
                return ResponseEntity.badRequest().body("Bad Request!");
            default:
                // 그 외의 경우, HTTP 상태 코드 200 OK와 "Ok!" 메시지를 응답으로 반환
                return ResponseEntity.ok().body("Ok!");
        }
    }

    // @PostMapping("/quiz"): HTTP POST 요청을 처리하기 위한 메서드를 정의
    // "/quiz" 경로로 들어오는 POST 요청을 이 메서드가 처리함
    @PostMapping("/quiz")
    public ResponseEntity<String> quiz2(@RequestBody Code code) {
        // @RequestBody Code code: 요청의 본문(body)을 Code 객체로 매핑하여 받음
        // 클라이언트가 POST 요청을 보낼 때 JSON 형태의 데이터가 Code 객체로 변환됨

        // switch 문을 사용하여 Code 객체의 value 값에 따라 다른 응답을 반환
        switch (code.value()) {
            case 1:
                // value 값이 1일 때, HTTP 상태 코드 403 Forbidden과 "Forbidden!" 메시지를 응답으로 반환
                return ResponseEntity.status(403).body("Forbidden!");
            default:
                // 그 외의 경우, HTTP 상태 코드 200 OK와 "Ok!" 메시지를 응답으로 반환
                return ResponseEntity.ok().body("Ok!");
        }
    }
}

// record Code(int value): Java 14 이상에서 사용 가능한 record 타입을 사용하여 불변 객체를 정의
// Code는 단순히 'value' 필드를 가지는 데이터 클래스 역할을 함
// 'record' 키워드는 불변 데이터 객체를 간단하게 정의할 수 있는 새로운 방식임
record Code(int value) {}
