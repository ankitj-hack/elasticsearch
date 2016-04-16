package dev.playground.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CsvParser<T> {

    public List<T> parse(File file) throws FileNotFoundException {
        try (BufferedReader bufferedReader = getBufferedReader(file)) {
            return parse(bufferedReader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private BufferedReader getBufferedReader(File file) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        return new BufferedReader(inputStreamReader);
    }

    private List<T> parse(BufferedReader br) {
        return br.lines()
                 .skip(1)
                 .map(this::split)
                 .map(this::mapToProduct)
                 .collect(Collectors.toList());
    }

    private List<String> split(String line) {
        String separator = getSeparator();
        String[] split = line.split(separator);
        return new ArrayList<>(Arrays.asList(split));
    }

    protected String getSeparator() {
        return ",";
    }

    protected abstract T mapToProduct(List<String> items);

}
