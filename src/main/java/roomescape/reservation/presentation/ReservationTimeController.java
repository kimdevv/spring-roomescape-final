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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.annotation.AdminLogin;
import roomescape.auth.annotation.NormalLogin;
import roomescape.common.config.SwaggerConfig;
import roomescape.common.exception.handler.ErrorResponse;
import roomescape.reservation.business.ReservationTimeService;
import roomescape.reservation.business.dto.request.ReservationTimeGetWithAvailabilityRequest;
import roomescape.reservation.model.ReservationTime;
import roomescape.reservation.presentation.dto.request.ReservationTimeCreateWebRequest;
import roomescape.reservation.presentation.dto.response.ReservationTimeGetResponse;
import roomescape.reservation.presentation.dto.response.ReservationTimeGetWithAvailabilityWebResponse;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservations/times")
@Tag(name = "예약시간", description = "예약시간과 관련된 API")
public class ReservationTimeController {

    private final ReservationTimeService reservationTimeService;

    public ReservationTimeController(ReservationTimeService reservationTimeService) {
        this.reservationTimeService = reservationTimeService;
    }

    @Operation(summary = "예약시간 생성", description = "예약할 수 있는 예약시간을 생성합니다.", security = @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "예약시간 생성 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터가 잘못된 경우", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @AdminLogin
    @PostMapping
    public ResponseEntity<ReservationTimeGetResponse> createReservationTime(@RequestBody ReservationTimeCreateWebRequest requestBody) {
        ReservationTime reservationTime = reservationTimeService.createReservationTime(requestBody);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReservationTimeGetResponse(reservationTime.getId(), reservationTime.getStartAt()));
    }

    @Operation(summary = "모든 예약시간 조회", description = "저장되어 있는 모든 예약시간을 조회합니다.", security = @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예약시간 조회 성공")
    })
    @AdminLogin
    @GetMapping
    public List<ReservationTimeGetResponse> findAllReservationTimes() {
        List<ReservationTime> reservationTimes = reservationTimeService.findAllReservationTimes();
        return reservationTimes.stream()
                .map(reservationTime -> new ReservationTimeGetResponse(reservationTime.getId(), reservationTime.getStartAt()))
                .toList();
    }

    @Operation(summary = "모든 예약시간 조회 with 예약 가능 여부", description = "저장되어 있는 모든 예약시간을 예약 가능 여부와 함께 조회합니다.", security = @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "예약시간 조회 성공")
    })
    @NormalLogin
    @GetMapping("/withAvailability")
    public List<ReservationTimeGetWithAvailabilityWebResponse> findAllReservationTimesWithAvailability(@RequestParam @Parameter(description = "예약시간을 조회할 테마", required = true) Long themeId,
                                                                                                       @RequestParam @Parameter(description = "예약시간을 조회할 날짜", required = true) LocalDate date) {
        return reservationTimeService.findAllReservationTimesWithAvailability(new ReservationTimeGetWithAvailabilityRequest(themeId, date));
    }

    @Operation(summary = "예약시간 삭제", description = "저장되어 있는 예약시간을 삭제합니다.", security = @SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME))
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "예약시간 삭제 성공")
    })
    @AdminLogin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservationTime(@PathVariable @Schema(description = "삭제할 예약시간의 id") Long id) {
        reservationTimeService.deleteReservationTimeById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
