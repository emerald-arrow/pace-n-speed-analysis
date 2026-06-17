package io.pacenspeedanalysis.util.io;

import org.apache.commons.io.input.BOMInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class BOMInputStreamReader implements CustomInputStreamReader {

    @Override
    public InputStreamReader newReader(final InputStream stream) throws IOException {
        return new InputStreamReader(BOMInputStream.builder()
                                                    .setInputStream(stream)
                                                    .get(), StandardCharsets.UTF_8);
    }
}
