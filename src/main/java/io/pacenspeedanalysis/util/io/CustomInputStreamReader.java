package io.pacenspeedanalysis.util.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public interface CustomInputStreamReader {

    InputStreamReader newReader(final InputStream stream) throws IOException;
}
