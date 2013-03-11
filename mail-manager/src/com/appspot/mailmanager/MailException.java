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

/**
 * The general {@code Exception} that is used to represent problems recognized by this application.
 * 
 * @author Alasdair Mercer <mercer.alasdair@gmail.com>
 */
@SuppressWarnings("serial")
public class MailException extends Exception {

    /**
     * Creates a new {@link MailException} with {@code null} as its detail message and no initialized cause.
     */
    public MailException() {
        super();
    }

    /**
     * Creates a new {@link MailException} with the specified detail {@code message} but no initialized caused.
     * 
     * @param message
     *            the detail message to be used
     */
    public MailException(String message) {
        super(message);
    }

    /**
     * Creates a new {@link MailException} with the specified detail {@code message} and {@code cause}.
     * 
     * @param message
     *            the detail message to be used
     * @param cause
     *            the cause to be used
     */
    public MailException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new {@link MailException} with the specified {@code cause} but not detail message.
     * 
     * @param cause
     *            the cause to be used
     */
    public MailException(Throwable cause) {
        super(cause);
    }
}