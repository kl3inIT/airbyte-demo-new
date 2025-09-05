package com.company.airbyte.utils;

import com.airbyte.api.models.errors.SDKError;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class AirbyteUtils {
    public static String decodeBody(SDKError e) {
        byte[] bytes = e.body();
        if (bytes == null || bytes.length == 0) return "<empty>";
        Charset cs = charsetFromContentType(e.rawResponse()
                .headers()
                .firstValue("Content-Type")
                .orElse(null));
        String text = new String(bytes, cs);
        // Cắt log nếu body quá lớn
        int max = 4000;
        return text.length() > max ? text.substring(0, max) + "…(truncated)" : text;
    }

    public static Charset charsetFromContentType(String ct) {
        if (ct == null) return StandardCharsets.UTF_8;
        String lower = ct.toLowerCase(Locale.ROOT);
        int i = lower.indexOf("charset=");
        if (i >= 0) {
            String enc = lower.substring(i + 8).trim();
            int semi = enc.indexOf(';');
            if (semi >= 0) enc = enc.substring(0, semi).trim();
            try {
                return Charset.forName(enc);
            } catch (Exception ignore) {
            }
        }
        return StandardCharsets.UTF_8;
    }
}
