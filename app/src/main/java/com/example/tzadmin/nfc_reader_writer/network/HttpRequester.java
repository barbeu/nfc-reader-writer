package com.example.tzadmin.nfc_reader_writer.network;

import android.os.AsyncTask;

import com.github.kevinsawicki.http.HttpRequest;

/**
 * Created by velor on 6/27/17.
 */

@Deprecated
public class HttpRequester extends AsyncTask<RequestNode, ResponceNode, Integer> {

    private RequestDelegate delegate;
    private Integer stage;

    public HttpRequester (RequestDelegate delegate, Integer stage) {
        this.delegate = delegate;
        this.stage = stage;
    }

    @Override
    protected void onProgressUpdate(ResponceNode... values) {
        if (delegate != null) {
            for (ResponceNode item : values)
                delegate.RequestDone(item.url, item.responce, item.passData, stage);
        }

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Integer success) {
        if (delegate != null)
            delegate.TaskDone(success);

        super.onPostExecute(success);
    }

    @Override
    protected Integer doInBackground(RequestNode... urls) {
        try {
            for (RequestNode item : urls) {
                if (item.method == RequestMethod.GET) {
                    String body = HttpRequest.get(item.url, item.params, true).body();
                    publishProgress( new ResponceNode(item.url, body, item.backParam) );
                } else if (item.method == RequestMethod.POST) {
                    String body = HttpRequest.post(item.url).form(item.params).body();
                    publishProgress( new ResponceNode(item.url, body, item.backParam) );
                }
            }
        } catch (HttpRequest.HttpRequestException exception) {
            return stage;
        }

        return stage;
    }
}