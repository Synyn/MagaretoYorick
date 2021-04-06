package com.magareto.yorick.service;

import java.io.IOException;
import java.io.InputStream;

public interface WaifuService {
    String getNsfw(String tag);
    String getSfw(String tag) throws IOException;
}
