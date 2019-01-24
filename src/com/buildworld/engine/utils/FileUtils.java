package com.buildworld.engine.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import com.buildworld.game.Game;
import com.shawnclake.morgencore.core.component.filesystem.FileRead;
import com.shawnclake.morgencore.core.component.filesystem.Files;
import org.lwjgl.BufferUtils;
import static org.lwjgl.BufferUtils.*;

public class FileUtils {

    public static String loadResource(String fileName) throws Exception {
        String result;
//        try (InputStream in = Class.forName(FileUtils.class.getName()).getResourceAsStream(fileName);
//             Scanner scanner = new Scanner(in)) {
//            result = scanner.useDelimiter("\\A").next();
//        }
        result = new Scanner(new File(Game.path + fileName)).useDelimiter("\\A").next();



        return result;
    }

    public static List<String> readAllLines(String fileName)
    {
        return new FileRead(Game.path + fileName).getEntireFile();
    }

    public static int[] listIntToArray(List<Integer> list) {
        int[] result = list.stream().mapToInt((Integer v) -> v).toArray();
        return result;
    }

    public static float[] listToArray(List<Float> list) {
        int size = list != null ? list.size() : 0;
        float[] floatArr = new float[size];
        for (int i = 0; i < size; i++) {
            floatArr[i] = list.get(i);
        }
        return floatArr;
    }

    public static boolean existsResourceFile(String fileName) {
        Files files = new Files();
        return files.isFile(Game.path + fileName);
    }

    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        Path path = Paths.get(Game.path + resource);
        if (java.nio.file.Files.isReadable(path)) {
            try (SeekableByteChannel fc = java.nio.file.Files.newByteChannel(path)) {
                buffer = BufferUtils.createByteBuffer((int) fc.size() + 1);
                while (fc.read(buffer) != -1) ;
            }
        } else {
            try (
                    InputStream source = new FileInputStream(Game.path + resource);
                    ReadableByteChannel rbc = Channels.newChannel(source)) {
                buffer = createByteBuffer(bufferSize);

                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    if (buffer.remaining() == 0) {
                        buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                    }
                }
            }
        }

        buffer.flip();
        return buffer;
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

}