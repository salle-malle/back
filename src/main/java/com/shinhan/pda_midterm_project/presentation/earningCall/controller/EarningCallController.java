package com.shinhan.pda_midterm_project.presentation.earningCall.controller;

import com.shinhan.pda_midterm_project.common.annotation.Auth;
import com.shinhan.pda_midterm_project.common.annotation.MemberOnly;
import com.shinhan.pda_midterm_project.common.response.Response;
import com.shinhan.pda_midterm_project.common.response.ResponseMessages;
import com.shinhan.pda_midterm_project.domain.auth.model.Accessor;
import com.shinhan.pda_midterm_project.domain.earning_call.model.EarningCall;
import com.shinhan.pda_midterm_project.domain.earning_call.service.EarningCallService;
import com.shinhan.pda_midterm_project.presentation.earningCall.dto.EarningCallResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/earning-calls")
@RequiredArgsConstructor
public class EarningCallController {

    private final EarningCallService earningCallService;

    /**
     * 모든 어닝콜 데이터 조회
     */
    @GetMapping
    public ResponseEntity<Response<List<EarningCallResponseDto>>> getAllEarningCalls() {
        try {
            List<EarningCall> earningCalls = earningCallService.getAllEarningCalls();
            List<EarningCallResponseDto> responseDtos = earningCalls.stream()
                    .map(EarningCallResponseDto::from)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Response.success(
                    ResponseMessages.EARNING_CALL_GET_ALL_SUCCESS.getCode(),
                    ResponseMessages.EARNING_CALL_GET_ALL_SUCCESS.getMessage(),
                    responseDtos));
        } catch (Exception e) {
            log.error("Error retrieving earning calls", e);
            return ResponseEntity.badRequest()
                    .body(Response.failure(
                            ResponseMessages.EARNING_CALL_GET_FAIL.getCode(),
                            ResponseMessages.EARNING_CALL_GET_FAIL.getMessage() + ": "
                                    + e.getMessage()));
        }
    }

    /**
     * 특정 주식의 어닝콜 데이터 조회
     */
    @GetMapping("/stock/{stockId}")
    public ResponseEntity<Response<List<EarningCallResponseDto>>> getEarningCallsByStockId(
            @PathVariable String stockId) {
        try {
            List<EarningCall> earningCalls = earningCallService.getEarningCallsByStockId(stockId);
            List<EarningCallResponseDto> responseDtos = earningCalls.stream()
                    .map(EarningCallResponseDto::from)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Response.success(
                    ResponseMessages.EARNING_CALL_GET_BY_STOCK_SUCCESS.getCode(),
                    ResponseMessages.EARNING_CALL_GET_BY_STOCK_SUCCESS.getMessage(),
                    responseDtos));
        } catch (Exception e) {
            log.error("Error retrieving earning calls for stockId: {}", stockId, e);
            return ResponseEntity.badRequest()
                    .body(Response.failure(
                            ResponseMessages.EARNING_CALL_GET_FAIL.getCode(),
                            ResponseMessages.EARNING_CALL_GET_FAIL.getMessage() + ": "
                                    + e.getMessage()));
        }
    }

    /**
     * 특정 날짜의 어닝콜 데이터 조회
     */
    @GetMapping("/date/{date}")
    public ResponseEntity<Response<List<EarningCallResponseDto>>> getEarningCallsByDate(@PathVariable String date) {
        try {
            List<EarningCall> earningCalls = earningCallService.getEarningCallsByDate(date);
            List<EarningCallResponseDto> responseDtos = earningCalls.stream()
                    .map(EarningCallResponseDto::from)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Response.success(
                    ResponseMessages.EARNING_CALL_GET_BY_DATE_SUCCESS.getCode(),
                    ResponseMessages.EARNING_CALL_GET_BY_DATE_SUCCESS.getMessage(),
                    responseDtos));
        } catch (Exception e) {
            log.error("Error retrieving earning calls for date: {}", date, e);
            return ResponseEntity.badRequest()
                    .body(Response.failure(
                            ResponseMessages.EARNING_CALL_GET_FAIL.getCode(),
                            ResponseMessages.EARNING_CALL_GET_FAIL.getMessage() + ": "
                                    + e.getMessage()));
        }
    }

    /**
     * 사용자 보유종목의 어닝콜 데이터 조회
     */
    @GetMapping("/member")
    @MemberOnly
    public ResponseEntity<Response<List<EarningCallResponseDto>>> getEarningCallsByMemberId(
            @Auth Accessor accessor) {
        try {
            List<EarningCall> earningCalls = earningCallService
                    .getEarningCallsByMemberId(accessor.memberId());
            List<EarningCallResponseDto> responseDtos = earningCalls.stream()
                    .map(EarningCallResponseDto::from)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Response.success(
                    ResponseMessages.EARNING_CALL_GET_BY_MEMBER_SUCCESS.getCode(),
                    ResponseMessages.EARNING_CALL_GET_BY_MEMBER_SUCCESS.getMessage(),
                    responseDtos));
        } catch (Exception e) {
            log.error("Error retrieving earning calls for memberId: {}", accessor.memberId(), e);
            return ResponseEntity.badRequest()
                    .body(Response.failure(
                            ResponseMessages.EARNING_CALL_GET_FAIL.getCode(),
                            ResponseMessages.EARNING_CALL_GET_FAIL.getMessage() + ": "
                                    + e.getMessage()));
        }
    }

    /**
     * 사용자 보유 종목의 다가올 어닝콜 데이터 조회
     */
    @GetMapping("/member/upcoming")
    @MemberOnly
    public ResponseEntity<Response<List<EarningCallResponseDto>>> getUpcomingEarningCalls(@Auth Accessor accessor) {
        try {
            List<EarningCall> earningCalls = earningCallService.getUpcomingEarningCall(accessor.memberId());
            List<EarningCallResponseDto> responseDtos = earningCalls.stream()
                    .map(EarningCallResponseDto::from)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(Response.success(
                    ResponseMessages.EARNING_CALL_GET_BY_MEMBER_SUCCESS.getCode(),
                    ResponseMessages.EARNING_CALL_GET_BY_MEMBER_SUCCESS.getMessage(),
                    responseDtos));
        } catch (Exception e) {
            log.error("Error retrieving upcoming earning calls for memberId: {}", accessor.memberId(), e);
            return ResponseEntity.badRequest()
                    .body(Response.failure(
                            ResponseMessages.EARNING_CALL_GET_FAIL.getCode(),
                            ResponseMessages.EARNING_CALL_GET_FAIL.getMessage() + ": "
                                    + e.getMessage()));
        }
    }
}
