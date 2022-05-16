package com.jskim.preuorder.service;

import com.jskim.preuorder.model.response.CommonResult;
import com.jskim.preuorder.model.response.ListResult;
import com.jskim.preuorder.model.response.SingleResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 해당 클래스가 Service임을 명시한다.
public class ResponseService {
    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    // enum으로 API 요청 결과에 대한 code, message를 정의한다.
    public enum CommonResponse {
        SUCCESS(0, "성공하였습니다."),
        FAIL(-1, "실패하였습니다.");

        private int code;
        private String msg;
    }

    // 단일건 결과를 처리하는 메소드
    public <T> SingleResult<T> getSingleResult(T data) {
        SingleResult<T> result = new SingleResult<>();
        result.setData(data);
        setSuccessResult(result);
        return result;
    }

    // 다중건 결과를 처리하는 메소드
    public <T> ListResult<T> getListResult(List<T> list) {
        ListResult<T> result = new ListResult<>();
        result.setList(list);
        setSuccessResult(result);
        return result;
    }

    //성공 결과만 처리하는 메소드
    public CommonResult getSuccessResult() { // CommonResult 응답 결과를 알려주는 클래스
        CommonResult result = new CommonResult();
        setSuccessResult(result);
        return result;
    }

    public CommonResult getFailResult(int code, String msg) {
        CommonResult result = new CommonResult();
        result.setSuccess(false);
//        result.setCode(CommonResponse.FAIL.getCode());
//        result.setMsg(CommonResponse.FAIL.getMsg());
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    private void setSuccessResult(CommonResult result) {
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMsg(CommonResponse.SUCCESS.getMsg());
    }
}
