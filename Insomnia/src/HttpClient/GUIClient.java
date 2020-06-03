package HttpClient;

import GUI.InsomniaMenuBar;
import GUI.ResponsePanel;

import javax.swing.*;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class GUIClient {
    public GUIClient() {
    }

    public static void addRequest(Request requestToAdd) {
    }

    public static void addFolder() {
    }

    public static void addRequestTo() {
    }

    public static void runRequest(Request requestToRun) {
        //TODO: run the request in background and then set the response panel when it is done
        SendRequest sendRequest = new SendRequest(requestToRun);
        try {
            sendRequest.doInBackground();
            sendRequest.done();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static class SendRequest extends SwingWorker<HttpResponse<String>, String> {
        Request requestToRun;

        public SendRequest(Request requestToRun) {
            this.requestToRun = requestToRun;
        }

        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * <p>
         * Note that this method is executed only once.
         *
         * <p>
         * Note: this method is executed in a background thread.
         *
         * @return the computed result
         * @throws Exception if unable to compute a result
         */
        @Override
        protected HttpResponse<String> doInBackground() throws Exception {
            HttpRequest httpRequest = requestToRun.makeRequest();
            if (httpRequest == null) {
                return null;
            } else {
                HttpClient client;
                HttpClient.Builder builder = HttpClient.newBuilder();
                if (InsomniaMenuBar.isFollowRedirect()) {
                    requestToRun.setFollowRedirect(true);
                    builder.followRedirects(HttpClient.Redirect.ALWAYS);
                } else {
                    requestToRun.setFollowRedirect(false);
                    builder.followRedirects(HttpClient.Redirect.NEVER);
                }
                builder.version(HttpClient.Version.HTTP_1_1);
                client = builder.build();

                double startTime = System.nanoTime();
                HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                double elapsedTime = System.nanoTime() - startTime;
                requestToRun.setLastResponse(response);
                return response;
            }
        }

        @Override
        protected void done() {
            ResponsePanel responsePanel = requestToRun.getResponsePanel();

//            String timeSize = "nS";
//            if (elapsedTime > 1000000000) {
//                timeSize = " S";
//                elapsedTime /= 1000000000;
//            } else if (elapsedTime > 1000000) {
//                timeSize = " mS";
//                elapsedTime /= 1000000;
//            } else if (elapsedTime > 1000) {
//                timeSize = " μS";
//                elapsedTime /= 1000;
//            }
//            System.out.println("URI: " + response.uri());
//            System.out.println("Response Time: " + elapsedTime + timeSize);
//            System.out.println("Content Length: " + response.headers().allValues("Content-Length"));
//            System.out.println("Version: " + response.version().toString() + "  |  Status code: " + response.statusCode());
//            System.out.println(response.body());
//            if (request.GetShowResponseHeaders()) {
//                HttpHeaders headers = response.headers();
//                headers.map().forEach((k, v) -> System.out.println(k + ":" + v));
//            }
        }
    }
}
