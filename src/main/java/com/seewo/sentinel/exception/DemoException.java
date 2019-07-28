package com.seewo.sentinel.exception;

import com.seewo.honeycomb.web.exception.BizException;

/**
 *  1、使用非受检异常，是为了api使用起来方便，使用方可以在他们的统一异常处理处统一处理异常，而不需要在调用的地方就要处理
 *  2、BizException是预定义的用于http场景的业务异常基类，因此对应有code和errorMsg，请问你的业务异常定义唯一的code和清晰的errorMsg。
 *     如果项目没有http场景，可以不定义code和errorMsg或者自定义你的异常基类代替BizException
 */
public class DemoException extends BizException {

    public DemoException(String message) {
        super(message);
    }

    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public String getErrorMsg() {
        return null;
    }
}