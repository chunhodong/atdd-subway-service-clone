package chunhodong.subway.auth.application;


import chunhodong.subway.auth.domain.LoginMember;
import chunhodong.subway.auth.dto.TokenRequest;
import chunhodong.subway.auth.dto.TokenResponse;
import chunhodong.subway.auth.infrastructure.JwtTokenProvider;
import chunhodong.subway.member.domain.Member;
import chunhodong.subway.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final String INVALID_TOKEN = "유효하지 않은 토큰입니다";
    private MemberRepository memberRepository;
    private JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse login(TokenRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(AuthorizationException::new);
        member.checkPassword(request.getPassword());

        String token = jwtTokenProvider.createToken(request.getEmail());
        return new TokenResponse(token);
    }

    public LoginMember findMemberByToken(String credentials) {
        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthorizationException(INVALID_TOKEN);
        }

        String email = jwtTokenProvider.getPayload(credentials);
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return new LoginMember(member.getId(), member.getEmail(), member.getAge());
    }
}
