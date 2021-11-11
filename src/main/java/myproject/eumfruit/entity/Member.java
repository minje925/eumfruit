package myproject.eumfruit.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import myproject.eumfruit.constant.Role;
import myproject.eumfruit.dto.MemberForm;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name="member")
@Getter @Setter
@ToString
public class Member {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberForm memberForm, PasswordEncoder passwordEncoder) {
        // 회원 엔티티를 생성하는 메소드를 엔티티에서 만들어서 관리하면 좋다.
        Member member = new Member();
        member.setName(memberForm.getName());
        member.setEmail(memberForm.getEmail());
        member.setAddress(memberForm.getAddress());
        String password = passwordEncoder.encode(memberForm.getPassword());
        member.setPassword(password);
        member.setRole(Role.ADMIN);
        return member;
    }
}
