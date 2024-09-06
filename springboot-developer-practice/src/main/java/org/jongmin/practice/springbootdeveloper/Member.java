package org.jongmin.practice.springbootdeveloper;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
// 해당 클래스를 엔티티로 지정
@Entity
public class Member{
    // 해당 엔티티의 칼럼을 기본키로 지정하는 어노테이션
    @Id
    // @ 해당 엔티티의 키가 자동으로 생성되도록 설정하는 어노테이션으로
    // 사용자가 해당 Member 엔티티를 생성할 때 (null, "A") 이런 형식으로 Id와 이름값을 넣었다면
    // id에는 null이 아니라 자동으로 1이 삽입됨 여기서 자동키 생성방식의 IDENTITY 속성은 기본키 생성을 DB에 위임함을 의미
    // 즉 자동으로 생성하라는 뜻임. 다른 옵션으로는 SEQUENCE(DB에 생성된 시퀀스를 이용)등이 있음
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // updatable 옵션은 사용자가 임의로 해당 id를 업데이트 하지 못하게 하는 메서드
    @Column(name = "id", updatable = false)
    private Long id;

    // nullable 옵션은 사용자가 임의로 해당 칼럼에 null을 삽입하지 못하게 함 (""는 가능)
    @Column(name = "name", nullable = false)
    private String name;

    // 수정의 경우 delete, insert와는 다르게
    // JPA는 엔티티가 수정될 때 변경된 필드를 자동으로 감지하여
    // 트랜잭션이 커밋될 때 한꺼번에 UPDATE 쿼리를 실행함
    // 따라서, 엔티티 필드의 값을 변경하기 위해서는
    // 트랜잭션 내에서 변경 메서드를 호출해야 함
    public void changeName(String name){
        this.name = name;
    }
}

