import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JUnitFailedTest {
    //테스트 이름
    @DisplayName("1+2는 3이다.")
    //테스트 메서드
    @Test
    public void junitTest(){
        int a = 1;
        int b = 3;
        int sum = 3;
        // 테스트를 위해 제공되는 객체.
        // 실패의 경우 하나만 실패해도 전체가 실패한 것으로 간주
        Assertions.assertEquals(sum, a+b);
    }
}
