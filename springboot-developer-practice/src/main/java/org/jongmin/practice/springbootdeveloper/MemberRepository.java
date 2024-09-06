package org.jongmin.practice.springbootdeveloper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // JPA는 기본적으로 sql 파일 내에 name을 찾는 코드 옵션을 지원하지 않아서
    // 사용자가 정의한 JpaRepository를 상속 받고있는 인터페이스 내에
    // 메서드 이름 기반의 쿼리를 생성하는 작업이나, @Query 방식으로 해당 메서드를 작성하는 방법이 존재하는데요.
    // 특히 아래의 방식은 JPA가 제공하는 기능으로, 메서드 이름을 통해 자동으로 데이터베이스 쿼리를 유추하고 생성하는 방식입니다.
    Optional<Member> findByName(String name);
}

