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
 * TODO: JavaDoc
 * 
 * @author Alasdair Mercer <mercer.alasdair@gmail.com>
 */
public class SendResponse {

    /**
     * TODO: JavaDoc
     * 
     * @param json
     * @return
     * @throws JSONException
     * @throws NullPointerException
     */
    public static SendResponse fromJSON(JSONObject json) throws JSONException {
        if (json == null) {
            return null;
        }
        return new SendResponse(json.getInt("status"), json.optString("error"));
    }

    private String error;
    private int status;

    /**
     * TODO: JavaDoc
     * 
     * @param status
     */
    public SendResponse(int status) {
        this(status, null);
    }

    /**
     * TODO: JavaDoc
     * 
     * @param status
     * @param error
     */
    public SendResponse(int status, String error) {
        setStatus(status);
        setError(error);
    }

    /**
     * TODO: JavaDoc
     * 
     * @return
     */
    public String getError() {
        return error;
    }

    /**
     * TODO: JavaDoc
     * 
     * @param error
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * TODO: JavaDoc
     * 
     * @return
     */
    public int getStatus() {
        return status;
    }

    /**
     * TODO: JavaDoc
     * 
     * @param status
     */
    public void setStatus(int status) {
        this.status = status;
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