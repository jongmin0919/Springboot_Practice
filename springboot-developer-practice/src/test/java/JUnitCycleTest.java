import org.junit.jupiter.api.*;

public class JUnitCycleTest {
    // 테스트 전체를 시작하기 전에 1회 실행되도록 메서드를 설정하는 어노테이션. 그래서 static 키워드를 붙임
    // 그 이유는 해당 인스턴스를 생성할 떄마다 부여되는고유의 메서드가 아니기 때문에 클래스 메서드로 지정해야 하기 때문임
    @BeforeAll
    static void beforeAll() {
        System.out.println("@BeforeAll");
    }

    // 테스트 마다 시작하기 전에 1회 실행되도록 메서드를 설정하는 어노테이션
    // 테스트 메서드에서 사용하는 객체를 초기화하거나 필요한 값을 미리 넣을 때 사용
    @BeforeEach
    public void beforeEach() {
        System.out.println("@BeforeEach");
    }

    @Test
    public void test1() {
        System.out.println("test1");
    }

    @Test
    public void test2() {
        System.out.println("test2");
    }

    @Test
    public void test3() {
        System.out.println("test3");
    }

    // 테스트 전체가 종료되기 직전에 1회 실행되도록 메서드를 설정하는 어노테이션. 이 메서드 또한 static 키워드를 붙임
    // 설명은 beforeAll과 동일
    @AfterAll
    static void afterAll() {
        System.out.println("@afterAll");
    }

    // 테스트 마다 종료되기 직전에 1회 실행되도록 메서드를 설정하는 어노테이션
    // 이하 설명은 beforeEach와 동일
    @AfterEach
    public void afterEach() {
        System.out.println("@afterEach");
    }
}
