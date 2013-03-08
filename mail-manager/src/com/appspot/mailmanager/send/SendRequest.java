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

import java.util.LinkedHashSet;
import java.util.Set;

import com.appspot.mailmanager.Contact;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * TODO: JavaDoc
 * 
 * @author Alasdair Mercer <mercer.alasdair@gmail.com>
 */
public class SendRequest {

    private String apiKey;
    private String html;
    private Set<Contact> recipients = new LinkedHashSet<>();
    private Contact sender;
    private String subject;
    private String text;

    /**
     * Creates a new instance of {@link SendRequest} based on the values derived from the specified {@code json}.
     * 
     * @param json
     *            the {@code JSONObject} from which the details are to be derived
     * @throws IllegalArgumentException
     * @throws JSONException
     * @throws NullPointerException
     *             If {@code json} is {@code null}.
     */
    public SendRequest(JSONObject json) throws JSONException {
        // TODO: Dervice values from json
    }

    /**
     * TODO: JavaDoc
     * 
     * @param recipient
     * @throws IllegalArgumentException
     */
    public void addRecipient(Contact recipient) {
        if (recipient == null) {
            throw new IllegalArgumentException("Invalid recipient");
        }
        recipients.add(recipient);
    }

    /**
     * TODO: JavaDoc
     * 
     * @param email
     * @throws IllegalArgumentException
     */
    public void addRecipient(String email) {
        addRecipient(email, null);
    }

    /**
     * TODO: JavaDoc
     * 
     * @param email
     * @param name
     * @throws IllegalArgumentException
     */
    public void addRecipient(String email, String name) {
        addRecipient(new Contact(email, name));
    }

    /**
     * TODO: JavaDoc
     * 
     * @return
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * TODO: JavaDoc
     * 
     * @param apiKey
     * @throws IllegalArgumentException
     */
    public void setApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("Invalid API key: " + apiKey);
        }
        this.apiKey = apiKey;
    }

    /**
     * TODO: JavaDoc
     * 
     * @return
     */
    public String getHtml() {
        return html;
    }

    /**
     * TODO: JavaDoc
     * 
     * @param html
     */
    public void setHtml(String html) {
        this.html = html;
    }

    /**
     * TODO: JavaDoc
     * 
     * @return
     */
    public Set<Contact> getRecipients() {
        return recipients;
    }

    /**
     * TODO: JavaDoc
     * 
     * @return
     */
    public Contact getSender() {
        return sender;
    }

    /**
     * TODO: JavaDoc
     * 
     * @param sender
     * @throws IllegalArgumentException
     */
    public void setSender(Contact sender) {
        if (sender == null) {
            throw new IllegalArgumentException("Invalid sender");
        }
        this.sender = sender;
    }

    /**
     * TODO: JavaDoc
     * 
     * @param email
     * @throws IllegalArgumentException
     */
    public void setSender(String email) {
        setSender(email, null);
    }

    /**
     * TODO: JavaDoc
     * 
     * @param email
     * @param name
     * @throws IllegalArgumentException
     */
    public void setSender(String email, String name) {
        setSender(new Contact(email, name));
    }

    /**
     * TODO: JavaDoc
     * 
     * @return
     */
    public String getSubject() {
        return subject;
    }

    /**
     * TODO: JavaDoc
     * 
     * @param subject
     * @throws IllegalArgumentException
     */
    public void setSubject(String subject) {
        if (subject == null || subject.isEmpty()) {
            throw new IllegalArgumentException("Invalid subject: " + subject);
        }
        this.subject = subject;
    }

    /**
     * TODO: JavaDoc
     * 
     * @return
     */
    public String getText() {
        return text;
    }

    /**
     * TODO: JavaDoc
     * 
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /*
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((apiKey == null) ? 0 : apiKey.hashCode());
        result = prime * result + ((html == null) ? 0 : html.hashCode());
        result = prime * result + ((recipients == null) ? 0 : recipients.hashCode());
        result = prime * result + ((sender == null) ? 0 : sender.hashCode());
        result = prime * result + ((subject == null) ? 0 : subject.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
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
        SendRequest other = (SendRequest) obj;
        if (apiKey == null) {
            if (other.apiKey != null)
                return false;
        } else if (!apiKey.equals(other.apiKey))
            return false;
        if (html == null) {
            if (other.html != null)
                return false;
        } else if (!html.equals(other.html))
            return false;
        if (recipients == null) {
            if (other.recipients != null)
                return false;
        } else if (!recipients.equals(other.recipients))
            return false;
        if (sender == null) {
            if (other.sender != null)
                return false;
        } else if (!sender.equals(other.sender))
            return false;
        if (subject == null) {
            if (other.subject != null)
                return false;
        } else if (!subject.equals(other.subject))
            return false;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
    }
}