/*
 *  Copyright 2010 Felix Schulze
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package de.felixschulze.androidacrademoapp;

import static org.acra.ACRA.LOG_TAG;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.acra.ACRA;
import org.acra.CrashReportData;
import org.acra.ReportField;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import org.acra.util.HttpRequest;

import android.net.Uri;
import android.util.Log;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;
import org.json.JSONException;

/**
 * An JSON ReportSender for ACRA 4 (http://code.google.com/p/acra/)
 * @author Felix Schulze
 *
 */
public class JsonSender implements ReportSender {
    private Uri mFormUri = null;
    private Map<ReportField, String> mMapping = null;
    private static final String CONTENT_TYPE;

    static {
        CONTENT_TYPE = "application/json";
    }

    /**
     * <p>
     * Create a new HttpPostSender instance.
     * </p>
     *
     * @param formUri The URL of your server-side crash report collection script.
     * @param mapping If null, POST parameters will be named with
     *                {@link org.acra.ReportField} values converted to String with
     *                .toString(). If not null, POST parameters will be named with
     *                the result of mapping.get(ReportField.SOME_FIELD);
     */
    public JsonSender(String formUri, Map<ReportField, String> mapping) {
        mFormUri = Uri.parse(formUri);
        mMapping = mapping;
    }

    public void send(CrashReportData report) throws ReportSenderException {

        try {
            URL reportUrl;
            reportUrl = new URL(mFormUri.toString());
            Log.d(LOG_TAG, "Connect to " + reportUrl.toString());

            JSONObject json = createJSON(report);

            doPost(json.toString(), reportUrl, ACRA.getConfig().formUriBasicAuthLogin(), ACRA.getConfig()
                    .formUriBasicAuthPassword());

        } catch (Exception e) {
            throw new ReportSenderException("Error while sending report to Http Post Form.", e);
        }

    }

    public static void doPost(String data, URL url, String login,
                              String password) throws ClientProtocolException, IOException {

        HttpRequest req = new HttpRequest(isNull(login) ? null : login,
                isNull(password) ? null : password);
        req.sendPost(url.toString(), data, CONTENT_TYPE);
    }

    private static boolean isNull(String aString) {
        return aString == null || aString == ACRA.NULL_VALUE;
    }

    private JSONObject createJSON(Map<ReportField, String> report) {
        JSONObject json= new JSONObject();

        ReportField[] fields = ACRA.getConfig().customReportContent();
        if (fields.length == 0) {
            fields = ACRA.DEFAULT_REPORT_FIELDS;
        }
        for (ReportField field : fields) {
            try {
                if (mMapping == null || mMapping.get(field) == null) {
                    json.put(field.toString(), report.get(field));
                }
                else {
                    json.put(mMapping.get(field), report.get(field));
                }
            } catch (JSONException e) {
                Log.e("JSONException", "There was an error creating JSON", e);
            }
        }

        return json;
    }
}