package org.com.kakaobank.common.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.com.kakaobank.common.exception.DTOConversionException;

public class ObjectConvertUtil {

    private static final ObjectMapper objectMapper;

    // ObjectMapper 초기 설정
    static {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private ObjectConvertUtil() {
        // 유틸리티 클래스의 인스턴스화를 방지
    }

    /**
     * DTO 변환 메서드
     *
     * @param fromValue 변환할 원본 객체
     * @param toValueType 변환 대상 클래스 타입
     * @param <T> 변환 대상 타입
     * @return 변환된 객체
     */
    public static <T> T copyVO(Object fromValue, Class<T> toValueType) {
        try {
            return objectMapper.convertValue(fromValue, toValueType);
        } catch (Exception e) {
            throw new DTOConversionException("DTO 변환 실패: " + e.getMessage(), e);
        }
    }

    /**
     * JSON 문자열을 특정 클래스 객체로 변환
     *
     * @param json JSON 문자열
     * @param valueType 변환 대상 클래스 타입
     * @param <T> 변환 대상 타입
     * @return 변환된 객체
     */
    public static <T> T fromJson(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (Exception e) {
            throw new DTOConversionException("JSON to Object conversion failed: " + e.getMessage(), e);
        }
    }

    /**
     * 객체를 JSON 문자열로 변환
     *
     * @param object 변환할 객체
     * @return JSON 문자열
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new DTOConversionException("Object to JSON conversion failed: " + e.getMessage(), e);
        }
    }
}
