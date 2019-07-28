package com.seewo.sentinel.common.util;

import com.alibaba.csp.sentinel.slots.block.BlockException;

public final class ExceptionUtil {

    public static void handleException(BlockException ex) {
        System.out.println("Oops: " + ex.getClass().getCanonicalName());
    }
}
