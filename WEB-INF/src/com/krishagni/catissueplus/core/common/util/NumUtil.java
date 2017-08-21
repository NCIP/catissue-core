package com.krishagni.catissueplus.core.common.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class NumUtil {

    public static Boolean lessThanZero(BigDecimal d) {
        return d != null && d.compareTo(BigDecimal.ZERO) < 0;
    }

    public static Boolean lessThanEqualsZero(BigDecimal d) {
        return d != null && d.compareTo(BigDecimal.ZERO) <= 0;
    }

    public static Boolean greaterThanZero(BigDecimal d) {
        return d != null && d.compareTo(BigDecimal.ZERO) > 0;
    }

    public static Boolean greaterThanEqualsZero(BigDecimal d) {
        return d != null && d.compareTo(BigDecimal.ZERO) >= 0;
    }

    public static Boolean isZero(BigDecimal d) {
        return d != null && d.equals(BigDecimal.ZERO);
    }

    public static Boolean lessThan(BigDecimal subject, BigDecimal measure) {
        return subject != null && measure != null && subject.compareTo(measure) < 0;
    }

    public static Boolean lessThanEquals(BigDecimal subject, BigDecimal measure) {
        return subject != null && measure != null && subject.compareTo(measure) <= 0;
    }

    public static Boolean greaterThan(BigDecimal subject, BigDecimal measure) {
        return subject != null && measure != null && subject.compareTo(measure) > 0;
    }

    public static Boolean greaterThanEquals(BigDecimal subject, BigDecimal measure) {
        return subject != null && measure != null && subject.compareTo(measure) >= 0;
    }

    public static BigDecimal multiply(BigDecimal op1, int op2) {
        return op1.multiply(new BigDecimal(op2));
    }

    public static BigDecimal divide(BigDecimal op1, int op2, int precision) {
        MathContext roundVal = new MathContext(precision, RoundingMode.HALF_UP);
        return op1.divide(new BigDecimal(op2)).round(roundVal);
    }

    public static BigDecimal numberToBigDecimal(Object number) {
        if (number == null) {
            return null;
        }

        if (!(number instanceof Number)) {
            throw new IllegalArgumentException("Input object is not a number");
        }

        return new BigDecimal(((Number)number).toString());
    }
}
