package com.magareto.yorick.service;

import com.magareto.yorick.models.manhwa.Manhwa;
import com.magareto.yorick.models.manhwa.ManhwaFilter;

import java.util.List;

public interface ManhwaService {
    void test();

    List<Manhwa> recommendManhwas(ManhwaFilter manhwaFilter);
}
