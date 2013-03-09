/* Copyright (C) 2013 Alasdair Mercer, http://neocotic.com
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.appspot.mailmanager;

import java.io.IOException;
import java.util.Properties;

/**
 * TODO: JavaDoc
 * 
 * @author Alasdair Mercer <mercer.alasdair@gmail.com>
 */
public class ApplicationManager {

    private static ApplicationManager instance;

    /**
     * Retrieves the singleton instance of {@link ApplicationManager}.
     * 
     * @return The singleton instance.
     */
    public static ApplicationManager getInstance() {
        if (instance == null) {
            instance = new ApplicationManager();
        }
        return instance;
    }

    private Properties properties = new Properties();

    /**
     * Creates a new instance of {@link ApplicationManager}.
     * <p>
     * The internal {@code Properties} are also loaded at this point, acting as some kind of caching mechanism.
     */
    private ApplicationManager() {
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("WEB-INF/application.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Indicates whether the specified {@code apiKey} is valid.
     * 
     * @param apiKey
     *            the API key to be checked
     * @return {@code true} if the API key exists; otherwise {@code false}.
     */
    public boolean exists(String apiKey) {
        return properties.containsKey(apiKey);
    }

    /**
     * Indicates whether the specified {@code apiKey} is enabled.
     * 
     * @param apiKey
     *            the API key to be checked
     * @return {@code true} if the API key is enabled; otherwise {@code false}.
     */
    public boolean isEnabled(String apiKey) {
        return Boolean.TRUE.toString().equalsIgnoreCase(properties.getProperty(apiKey));
    }
}