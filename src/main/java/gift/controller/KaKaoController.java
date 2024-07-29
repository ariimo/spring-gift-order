package gift.controller;

import gift.classes.RequestState.RequestStatus;
import gift.classes.RequestState.SecureRequestStateDTO;
import gift.services.KaKaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("kakao")
@Tag(name = "KaKaoController", description = "KaKao API")
public class KaKaoController {

    private final KaKaoService kaKaoService;

    public KaKaoController(KaKaoService kaKaoService) {
        this.kaKaoService = kaKaoService;
    }

    @GetMapping("/login")
    @Operation(summary = "카카오 로그인", description = "카카오로 로그인 할 때 사용하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 토큰을 받음")
    })
    public String login(Model model) {
        model.addAttribute("kakaoUrl", kaKaoService.getKaKaoLogin());

        return "kakaologin";
    }

    @GetMapping("/callback")
    @Operation(summary = "카카오 로그인 콜백", description = "카카오 로그인 후 콜백을 처리하는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공적으로 콜백을 처리함")
    })
    public ResponseEntity<?> callback(HttpServletRequest request) throws Exception {
        String token = kaKaoService.getKaKaoToken(request.getParameter("code"));

        return ResponseEntity.ok().body(new SecureRequestStateDTO(
            RequestStatus.success,
            null,
            token
        ));
    }

}
