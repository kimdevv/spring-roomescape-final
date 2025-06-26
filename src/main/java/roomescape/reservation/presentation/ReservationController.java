package roomescape.reservation.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.annotation.AdminLogin;
import roomescape.auth.annotation.NormalLogin;
import roomescape.auth.model.LoginInfo;
import roomescape.common.config.SwaggerConfig;
import roomescape.common.exception.handler.ErrorResponse;
import roomescape.reservation.business.ReservationService;
import roomescape.reservation.business.dto.request.ReservationCreateRequest;
import roomescape.reservation.model.Reservation;
import roomescape.reservation.model.ReservationStatus;
import roomescape.reservation.presentation.dto.request.ReservationCreateByAdminWebRequest;
import roomescape.reservation.presentation.dto.request.ReservationCreateWebRequest;
import roomescape.reservation.presentation.dto.response.ReservationGetResponse;
import roomescape.reservation.presentation.dto.response.ReservationMineGetWebResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@Tag(name = "예약", description = "예약과 관련된 API")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Operation(summary = "예약 생성", description = "예약을 생성합니다.", security = @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "예약 생성 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터가 잘못된 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @NormalLogin
    @PostMapping
    public ResponseEntity<ReservationGetResponse> createReservation(@RequestBody ReservationCreateWebRequest requestBody, @Parameter(hidden = true) LoginInfo loginInfo) {
        Reservation reservation = reservationService.createReservation(new ReservationCreateRequest(loginInfo.email(), requestBody.date(), requestBody.timeId(), requestBody.themeId(), requestBody.status(),
                requestBody.paymentKey(), requestBody.orderId(), requestBody.amount(), requestBody.paymentType()));
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReservationGetResponse(reservation.getId(), reservation.getMember().getName(), reservation.getDate(), reservation.getTime().getStartAt(), reservation.getTheme().getName(), reservation.getStatus().name()));
    }

    @Operation(summary = "예약 생성 관리자", description = "관리자가 예약을 생성합니다.", security = @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "예약 생성 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터가 잘못된 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @AdminLogin
    @PostMapping("/admin")
    public ResponseEntity<ReservationGetResponse> createReservationByAdmin(@RequestBody ReservationCreateByAdminWebRequest requestBody) {
        Reservation reservation = reservationService.createReservation(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReservationGetResponse(reservation.getId(), reservation.getMember().getName(), reservation.getDate(), reservation.getTime().getStartAt(), reservation.getTheme().getName(), reservation.getStatus().name()));
    }

    @Operation(summary = "예약 조회", description = "예약을 조회합니다.", security = @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예약 조회 성공")
    })
    @AdminLogin
    @GetMapping
    public List<ReservationGetResponse> findReservations(@RequestParam(required = false) @Parameter(description = "조회하고 싶은 특정 예약의 상태") ReservationStatus status) {
        List<Reservation> reservations = reservationService.findReservations(status);
        return reservations.stream()
                .map(reservation -> new ReservationGetResponse(reservation.getId(), reservation.getMember().getName(), reservation.getDate(), reservation.getTime().getStartAt(), reservation.getTheme().getName(), reservation.getStatus().name()))
                .toList();
    }

    @Operation(summary = "내 예약 조회", description = "현재 로그인되어 있는 멤버가 생성한 예약을 조회합니다.", security = @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예약 조회 성공")
    })
    @NormalLogin
    @GetMapping("/mine")
    public List<ReservationMineGetWebResponse> findMyReservations(@Parameter(hidden = true) LoginInfo loginInfo) {
        return reservationService.findMyReservations(loginInfo.email());
    }

    @Operation(summary = "예약 조건부 조회", description = "조건을 걸어 예약을 조회합니다.", security = @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예약 조회 성공")
    })
    @AdminLogin
    @GetMapping("/filter")
    public List<ReservationGetResponse> findFilteredReservations(@RequestParam(required = false) @Parameter(description = "예약한 멤버의 id") Long memberId,
                                                      @RequestParam(required = false) @Parameter(description = "예약한 테마의 id") Long themeId,
                                                      @RequestParam(required = false) @Parameter(description = "조회할 예약의 시작 날짜") LocalDate startDate,
                                                      @RequestParam(required = false) @Parameter(description = "조회할 예약의 마지막 날짜") LocalDate endDate) {
        List<Reservation> reservations = reservationService.findFilteredReservations(memberId, themeId, startDate, endDate);
        return reservations.stream()
                .map(reservation -> new ReservationGetResponse(reservation.getId(), reservation.getMember().getName(), reservation.getDate(), reservation.getTime().getStartAt(), reservation.getTheme().getName(), reservation.getStatus().name()))
                .toList();
    }

    @Operation(summary = "예약 대기 승인", description = "대기 상태의 예약을 예약 상태로 변경합니다.", security = @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "예약 대기 승인 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터가 잘못된 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @AdminLogin
    @PatchMapping("/apply/{id}")
    public ResponseEntity<ReservationGetResponse> applyWaitingReservation(@PathVariable @Schema(description = "예약 상태로 변경할 대기 상태의 예약 id") Long id) {
        Reservation reservation = reservationService.applyWaitingReservation(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReservationGetResponse(reservation.getId(), reservation.getMember().getName(), reservation.getDate(), reservation.getTime().getStartAt(), reservation.getTheme().getName(), reservation.getStatus().name()));
    }

    @Operation(summary = "예약 삭제", description = "저장되어 있는 예약을 삭제합니다.", security = @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "예약 삭제 성공")
    })
    @NormalLogin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable @Schema(description = "삭제할 예약의 id") Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
