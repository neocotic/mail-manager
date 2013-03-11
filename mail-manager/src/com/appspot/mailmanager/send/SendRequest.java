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
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

/**
 * Contains information which is to be used to construct and send an email message.
 * 
 * @author Alasdair Mercer <mercer.alasdair@gmail.com>
 */
public class SendRequest {

    /**
     * Creates a new instance of {@link SendRequest} based on the values derived from the specified {@code json}.
     * 
     * @param json
     *            the {@code JSONObject} from which the details are to be derived
     * @return The {@link SendRequest} derived from {@code json}.
     * @throws IllegalArgumentException
     *             If any of the required derived values are invalid.
     * @throws JSONException
     *             If {@code json} is malformed.
     * @throws NullPointerException
     *             If {@code json} is {@code null}.
     */
    public static SendRequest fromJSON(JSONObject json) throws JSONException {
        SendRequest request = new SendRequest();
        request.setApiKey(json.getString("apiKey"));
        request.setHtml(json.optString("html", null));
        request.setSender(Contact.fromJSON(json.getJSONObject("sender")));
        request.setSubject(json.getString("subject"));
        request.setText(json.optString("text", null));

        JSONArray recipients = json.getJSONArray("recipients");
        for (int i = 0; i < recipients.length(); i++) {
            request.addRecipient(Contact.fromJSON(recipients.getJSONObject(i)));
        }

        return request;
    }

    private String apiKey;
    private String html;
    private Set<Contact> recipients = new LinkedHashSet<>();
    private Contact sender;
    private String subject;
    private String text;

    /**
     * Creates a new instance of {@link SendRequest}.
     */
    private SendRequest() {
    }

    /**
     * Adds the specified {@link Contact} to the list of recipients for the message.
     * 
     * @param recipient
     *            the {@code Contact} to receive the message
     * @throws IllegalArgumentException
     *             If {@code recipient} is {@code null}.
     */
    public void addRecipient(Contact recipient) {
        if (recipient == null) {
            throw new IllegalArgumentException("Invalid recipient");
        }
        recipients.add(recipient);
    }

    /**
     * Adds a {@link Contact} with the specified {@code email} address to the list of recipients for the message.
     * 
     * @param email
     *            the email address of the {@code Contact} to receive the message
     * @throws IllegalArgumentException
     *             If {@code email} is {@code null} or empty.
     */
    public void addRecipient(String email) {
        addRecipient(email, null);
    }

    /**
     * Adds a {@link Contact} with the specified {@code email} address and {@code name} to the list of recipients for the message.
     * 
     * @param email
     *            the email address of the {@code Contact} to receive the message
     * @param name
     *            the name of the {@code Contact} to receive the message
     * @throws IllegalArgumentException
     *             If {@code email} is {@code null} or empty.
     */
    public void addRecipient(String email, String name) {
        addRecipient(new Contact(email, name));
    }

    /**
     * Returns the API key of the application making this request.
     * 
     * @return The API key.
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Sets the API key of the application making this request to {@code apiKey}.
     * 
     * @param apiKey
     *            the API key to be set
     * @throws IllegalArgumentException
     *             If {@code apiKey} is {@code null} or empty.
     */
    public void setApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalArgumentException("Invalid API key: " + apiKey);
        }
        this.apiKey = apiKey;
    }

    /**
     * Returns the HTML content for the message.
     * <p>
     * If this returns {@code null}, it might be worth checking the text content to see if the request is to send plain text in the message.
     * 
     * @return The HTML content.
     * @see #getText()
     */
    public String getHtml() {
        return html;
    }

    /**
     * Sets the HTML content for the message to {@code html}.
     * 
     * @param html
     *            the HTML to be set
     */
    public void setHtml(String html) {
        this.html = html;
    }

    /**
     * Returns the {@link Contact Contacts} that are to receive the message.
     * 
     * @return The {@code Set} of recipients.
     */
    public Set<Contact> getRecipients() {
        return recipients;
    }

    /**
     * Returns the {@link Contact} that is sending the message.
     * 
     * @return The sender.
     */
    public Contact getSender() {
        return sender;
    }

    /**
     * Sets the {@link Contact} that is sending the message to {@code sender}.
     * 
     * @param sender
     *            the {@code Contact} to be set
     * @throws IllegalArgumentException
     *             If {@code Contact} is {@code null}.
     */
    public void setSender(Contact sender) {
        if (sender == null) {
            throw new IllegalArgumentException("Invalid sender");
        }
        this.sender = sender;
    }

    /**
     * Sets the {@link Contact} that is sending the message to one with the {@code email} address provided.
     * 
     * @param email
     *            the email address of the {@code Contact} to be set
     * @throws IllegalArgumentException
     *             If {@code email} is {@code null} or empty.
     */
    public void setSender(String email) {
        setSender(email, null);
    }

    /**
     * Sets the {@link Contact} that is sending the message to one with the {@code email} address and {@code name} provided.
     * 
     * @param email
     *            the email address of the {@code Contact} to be set
     * @param name
     *            the name of the {@code Contact} to be set
     * @throws IllegalArgumentException
     *             If {@code email} is {@code null} or empty.
     */
    public void setSender(String email, String name) {
        setSender(new Contact(email, name));
    }

    /**
     * Returns the subject for the message.
     * 
     * @return The subject.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the subject for the message to {@code subject}.
     * 
     * @param subject
     *            the subject to be set
     * @throws IllegalArgumentException
     *             If {@code subject} is {@code null} or empty.
     */
    public void setSubject(String subject) {
        if (subject == null || subject.isEmpty()) {
            throw new IllegalArgumentException("Invalid subject: " + subject);
        }
        this.subject = subject;
    }

    /**
     * Returns the text content for the message.
     * <p>
     * If this returns {@code null}, it might be worth checking the HTML content to see if the request is to send HTML in the message.
     * 
     * @return The text content.
     * @see #getHtml()
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text content for the message to {@code text}.
     * 
     * @param text
     *            the text to be set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Creates a {@code JSONObject} based on this {@link SendRequest}.
     * 
     * @return The derived {@code JSONObject}.
     * @throws JSONException
     *             If this {@link SendRequest} is malformed.
     */
    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("apiKey", apiKey);
        json.putOpt("html", html);
        json.put("sender", sender.toJSON());
        json.put("subject", subject);
        json.putOpt("text", text);

        JSONArray array = new JSONArray();
        for (Contact recipient : recipients) {
            array.put(recipient.toJSON());
        }
        json.put("recipients", array);

        return json;
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