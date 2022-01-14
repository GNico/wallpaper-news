package com.gnico.main;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.MalformedURLException;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ApplicationTest {

    @ParameterizedTest
    @ValueSource(strings = {"", "malformed url"})
    @NullSource
    void testIncorrectFeedURL(String input)  {
        Application app = new Application("test", "test");
        assertThrows(MalformedURLException.class, () -> app.updateHeadlines(input));
    }

}
