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

import java.io.UnsupportedEncodingException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * TODO: JavaDoc
 * 
 * @author Alasdair Mercer <mercer.alasdair@gmail.com>
 */
public class Contact {

    /**
     * Creates a new instance of {@link Contact} based on the values derived from the specified {@code json}.
     * 
     * @param json
     *            the {@code JSONObject} from which the details are to be derived
     * @return The {@code Contact} derived from {@code json}.
     * @throws IllegalArgumentException
     *             If the derived email address is empty.
     * @throws JSONException
     *             If {@code json} is malformed.
     * @throws NullPointerException
     *             If {@code json} is {@code null}.
     */
    public static Contact fromJSON(JSONObject json) throws JSONException {
        return new Contact(json.getString("email"), json.optString("name"));
    }

    private String email;
    private String name;

    /**
     * Creates a new instance of {@link Contact} with no name and the {@code email} address provided.
     * 
     * @param email
     *            the email address to be used
     * @throws IllegalArgumentException
     *             If {@code email} is {@code null} or empty.
     */
    public Contact(String email) {
        this(email, null);
    }

    /**
     * Creates a new instance of {@link Contact} with the {@code email} address and {@code name} provided.
     * 
     * @param email
     *            the email address to be used
     * @param name
     *            the name to be used
     * @throws IllegalArgumentException
     *             If {@code email} is {@code null} or empty.
     */
    public Contact(String email, String name) {
        setEmail(email);
        setName(name);
    }

    /**
     * Returns the email address of this {@link Contact}.
     * 
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of this {@link Contact} to {@code email}.
     * 
     * @param email
     *            the email address to be set
     * @throws IllegalArgumentException
     *             If {@code email} is either {@code null} or empty.
     */
    public void setEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Invalid contact email: " + email);
        }
        this.email = email;
    }

    /**
     * Return the name of this {@link Contact}.
     * 
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this {@link Contact} to {@code name}.
     * 
     * @param name
     *            the name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Creates an {@code InternetAddress} using the details provided by this {@link Contact}.
     * 
     * @return The derived {@code InternetAddress}.
     * @throws AddressException
     *             If this contact's email address is invalid.
     * @throws UnsupportedEncodingException
     *             If this contact's name is invalid.
     */
    public InternetAddress toInternetAddress() throws AddressException, UnsupportedEncodingException {
        InternetAddress address = new InternetAddress(email);
        if (name != null) {
            address.setPersonal(name);
        }
        return address;
    }

    /**
     * TODO: JavaDoc
     * 
     * @return
     */
    public JSONObject toJSON() {
        return new JSONObject(this);
    }

    /*
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /*
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Contact other = (Contact) obj;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
}