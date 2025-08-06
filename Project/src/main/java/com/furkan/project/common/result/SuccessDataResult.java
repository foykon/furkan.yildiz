package com.furkan.project.common.result;


public class SuccessDataResult<T> extends DataResult<T>{
    public SuccessDataResult(T data) {
        super(data, true);
    }
    public SuccessDataResult(T data, String message){
        super(data, true, message);
    }
}
