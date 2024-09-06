package org.jongmin.practice.springbootdeveloper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

// @DataJpaTest: JPA 관련된 테스트를 할 때 사용하는 어노테이션임.
// 이 어노테이션을 사용하면, JPA 관련된 빈(bean)만 로드하고 데이터베이스를 메모리 DB(H2)로 설정해서 테스트함.
@DataJpaTest
class MemberRepositoryTest {

    // @Autowired: 스프링의 의존성 주입(Dependency Injection) 어노테이션임.
    // MemberRepository라는 인터페이스를 스프링 컨테이너에서 찾아 자동으로 주입해줌.
    @Autowired
    MemberRepository memberRepository;

    // @Sql("/insert-member.sql"): 테스트를 실행하기 전에 insert-member.sql 파일에 있는 SQL 스크립트를 실행함.
    // 예를 들어, 테스트하기 전에 데이터베이스에 특정 데이터를 삽입하는 데 사용될 수 있음.
    @Sql("/insert-member.sql")
    @Test
    void getAllmembers() {
        // memberRepository를 사용해 모든 멤버(Member) 데이터를 데이터베이스에서 조회함.
        // 특히 findById와의 반환 타입에서 다른 점은 findAll은 List<Member> 형태로 데이터를 반환한다는거임
        List<Member> members = memberRepository.findAll();
        // memberRepository를 이용해 id 값으로 조회해옴. 이때 2L은 long타입을 의미하며
        // findById는 옵셔널(값이 있으면 있는대로 감싸고, 없으면 null을 감싸 null exception을 방지하는 객체) 객체를
        // get메서드는 실제 entity로 반환 받는 객체를 의미함
        Member member2 = memberRepository.findById(2L).get();
        // member1에 사용된 findByName은 MemberRepository 인터페이스에서 작성항 쿼리 메서드로
        // 이름 조회의 경우 기본적으로 JpaRepository에서 제공하지 않기 때문에
        // 해당 Jpa를 상속 받는 인터페이스에서 작성한 쿼리 메서드임. 이때 작성된 쿼리 메서드를 Jpa에서 자동으로 유추하고 쿼리를 생성.
        Member member1 = memberRepository.findByName("C").get();
        // assertThat(members.size()).isEqualTo(3): 조회된 멤버의 수가 3명인지 확인하는 검증(assertion) 코드임.
        //특히 assertThat 메서드는 값의 일치 여부뿐만 아니라, 다양한 조건을 쉽게 검증할 수 있도록
        // 다양한 매처(Matcher)를 함께 제공함, 즉 데이터 검증 조건을 좀 더 세밀히 조작할 수 있음.
        // 그래서 아래의 테스트가 성공하려면 members 리스트의 크기가 3이어야 함.
        assertThat(members.size()).isEqualTo(3);
        assertThat(member2.getName()).isEqualTo("B");
        assertThat(member1.getName()).isEqualTo("C");
    }

    @Test
    void saveMember(){
        Member member = new Member(1L, "A");

        // sql에서 inset문은 Jpa에서 save 메서드로 사용 가능
        // insert into member (id, name) values (1, 'A')을 의미
        memberRepository.save(member);

        assertThat(memberRepository.findById(1L).get().getName()).isEqualTo("A");
    }

    @Test
    void saveMembers(){
        // List.of 메서드는 List의 메서드중 하나로, 여러 개의 요소를 콤마로 구분하여 인자로 전달할 수 있음
        // 만약 List.of를 안쓰면 Arrays.asList 메서드를 쓸 수도 있고, 단순히 members.add로 추가할 수 있음
        List<Member> members = List.of(new Member(2L,"B"), new Member(3L,"C"));

        memberRepository.saveAll(members);

        assertThat(memberRepository.findById(1L).isEmpty()).isTrue();
    }

    // 각 테스트는 영속적이기 때문에 이 시점에서의 DB삭제는 무의미 함
    // 그 이유는 아래의 delete메서드를 수행하더라도 memberRepository에 저장된 값 자체가 없기 때문임.
    // 그래도 오류는 안나기 때문에 isEmpty문이 성립하므로 테스트가 넘어감
    @Test
    void deleteMemberById(){

        // db의 특정 행을 삭제하는 메서드
        memberRepository.deleteById(1L);

        assertThat(memberRepository.findById(1L).isEmpty()).isTrue();
    }

    // deleteAll을 사용하는 메서드의 경우 테스트 간의 격리를 보장하기 위해서
    // AfterEach 어노테이션을 붙여 사용함
    @AfterEach
    public void deleteAllMembers(){

        // db의 모든 데이터를 삭제하는 메서드
        memberRepository.deleteAll();

        assertThat(memberRepository.findAll().size()).isZero();
    }

    // 각 테스트는 테스트 간의 db를 공유하지 않으므로 memberRepository가 전역 변수라고 할지라도
    // 각각의 test에 사용되는 트랜잭션은 새로운 트랜잭션이기 때문에 이 경우 update문의 수행을 위해 Sql문 어노테이션을 수행해줌
    @Sql("/insert-member.sql")
    @Test
    void updateMember(){

        Member member = memberRepository.findById(2L).get();
        // 트랜잭션 까지는 실제 db가 반영된 상태가 아니기 때문에 해당 트랜잭션 반영을 위해 
        // memberRepository의 save메서드로 member를 덮어씌워줌
        member.changeName("Re_Go");
        memberRepository.save(member);
        // 업데이트의 경우 앞서 말한대로 insert, delete와 다르게 트랜잭션 내에 수정 작업을 마무리 하고
        // 한꺼번에 DB에 적용하므로 그 전까지는 JPA 트랜잭션 내의 엔티티를 변경하는 메서드를 수행함
        assertThat(memberRepository.findById(2L).get().getName()).isEqualTo("Re_Go");
    }
}
