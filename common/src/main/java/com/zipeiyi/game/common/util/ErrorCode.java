package com.zipeiyi.game.common.util;

public class ErrorCode {

    public static enum CommonResult {
        FAIL(0),
        SUCCESS(1);

        int code;

        CommonResult(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

    }

    public static enum AckResult {
        FAIL(0),
        SUCCESS(1);

        int result;

        private AckResult(int result) {
            this.result = result;
        }

        public Integer getValue() {
            return this.result;
        }
    }
}
