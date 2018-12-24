package com.buildworld.engine.utils;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import com.shawnclake.morgencore.core.component.filesystem.FileRead;

public class Utils {

    public static String loadResource(String fileName) throws Exception {
        String result;
//        try (InputStream in = Class.forName(Utils.class.getName()).getResourceAsStream(fileName);
//             Scanner scanner = new Scanner(in)) {
//            result = scanner.useDelimiter("\\A").next();
//        }
        result = new Scanner(new File(fileName)).useDelimiter("\\A").next();



        return result;
    }

    public static List<String> readAllLines(String fileName)
    {
        return new FileRead(fileName).getEntireFile();
    }

    public static float[] listToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }
}