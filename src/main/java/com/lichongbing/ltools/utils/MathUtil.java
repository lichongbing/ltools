package com.lichongbing.ltools.utils;

/**
 * @author lichongbing
 * @version 1.0.0
 * @createdate 2021/10/13 3:11 下午
 * @description: TODO
 */
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.text.DecimalFormat;

@Slf4j
@Configuration
public class MathUtil {

    public static float scale(Float value){
        DecimalFormat format = new DecimalFormat("#.00");
        String scaled = format.format(value);

        return Float.parseFloat(scaled);
    }


    public static double scale(Double value){
        if (value == null){
            value = 0.0;
        }
        DecimalFormat format = new DecimalFormat("#.00");
        String scaled = format.format(value);

        return Double.parseDouble(scaled);
    }
}

