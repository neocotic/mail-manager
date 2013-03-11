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
package com.appspot.mailmanager.send;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * Contains information used in response to a {@link SendRequest}.
 * 
 * @author Alasdair Mercer <mercer.alasdair@gmail.com>
 */
public class SendResponse {

    /**
     * Creates a new instance of {@link SendResponse} based on the values derived from the specified {@code json}.
     * 
     * @param json
     *            the {@code JSONObject} from which the details are to be derived
     * @return The {@link SendResponse} derived from {@code json}.
     * @throws JSONException
     *             If {@code json} is malformed.
     * @throws NullPointerException
     *             If {@code json} is {@code null}.
     */
    public static SendResponse fromJSON(JSONObject json) throws JSONException {
        return new SendResponse(json.getInt("status"), json.optString("error"));
    }

    private String error;
    private int status;

    /**
     * Creates a new instance of {@link SendResponse} with the specified {@code status} code but no error message.
     * 
     * @param status
     *            the status code to be used
     */
    public SendResponse(int status) {
        this(status, null);
    }

    /**
     * Creates a new instance of {@link SendResponse} with the {@code status} code and {@code error} message provided.
     * 
     * @param status
     *            the status code to be used
     * @param error
     *            the error message to be used
     */
    public SendResponse(int status, String error) {
        setStatus(status);
        setError(error);
    }

    /**
     * Returns the error message for this response.
     * 
     * @return The error message.
     */
    public String getError() {
        return error;
    }

    /**
     * Sets the error message for this response to {@code error}.
     * 
     * @param error
     *            the error message to be set
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Returns the status code for this response.
     * 
     * @return The status code.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the status code for this response to {@code status}.
     * 
     * @param status
     *            the status code to be set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Creates a {@code JSONObject} based on this {@link SendResponse}.
     * 
     * @return The derived {@code JSONObject}.
     * @throws JSONException
     *             If this {@link SendResponse} is malformed.
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.putOpt("error", error);
        json.putOpt("status", status);

        return json;
    }

    /*
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((error == null) ? 0 : error.hashCode());
        result = prime * result + status;
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
        SendResponse other = (SendResponse) obj;
        if (error == null) {
            if (other.error != null)
                return false;
        } else if (!error.equals(other.error))
            return false;
        if (status != other.status)
            return false;
        return true;
    }
}