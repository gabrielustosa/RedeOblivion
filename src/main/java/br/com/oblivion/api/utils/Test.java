package br.com.oblivion.api.utils;

import java.time.Instant;

public class Test {

    public static void main(String[] args) {
        System.out.println(Instant.now().plusSeconds(60 * 60).toEpochMilli());
    }
}
