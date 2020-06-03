package HttpClient;

import GUI.InsomniaMenuBar;
import GUI.ResponsePanel;

import javax.swing.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;

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
            sendRequest.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class SendRequest extends SwingWorker<HttpResponse<String>, String> {
        Request requestToRun;
        double elapsedTime;

        public SendRequest(Request requestToRun) {
            this.requestToRun = requestToRun;
            elapsedTime = 0;
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
                    System.out.println("YESSSSSSSSSSSSSSSSSSSsss");
                    requestToRun.setFollowRedirect(false);
                    builder.followRedirects(HttpClient.Redirect.NEVER);
                }
                builder.version(HttpClient.Version.HTTP_1_1);
                client = builder.build();

                double startTime = System.nanoTime();
                System.out.println("SENDING");
                HttpResponse<String> response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                System.out.println("SENT");
                if (response==null) {
                    System.out.println("respone is null");
                }
                elapsedTime = System.nanoTime() - startTime;
//                requestToRun.setLastResponse(response);
                System.out.println("YES");
                return response;
            }
        }

        @Override
        protected void done() {
            HttpResponse<String> response= null;
            try {
                response = get();
                System.out.println("GET DONE");
                if (response == null){
                    System.out.println("GET RETURNS NULL");
                }
            } catch (InterruptedException  | ExecutionException e) {
                System.out.println("EXEPTIONEXEPTIONEXEPTIONEXEPTIONEXEPTIONEXEPTION");
                e.printStackTrace();
            }
            ResponsePanel responsePanel = requestToRun.getResponsePanel();
            String timeSize = "nS";
            if (elapsedTime > 1000000000) {
                timeSize = " S";
                elapsedTime /= 1000000000;
            } else if (elapsedTime > 1000000) {
                timeSize = " mS";
                elapsedTime /= 1000000;
            } else if (elapsedTime > 1000) {
                timeSize = " μS";
                elapsedTime /= 1000;
            }
            if (responsePanel == null) System.out.println("response panel is null");
            if (response == null) System.out.println("response is null");
            responsePanel.setTimeLabel(elapsedTime + timeSize);
//            responsePanel.setSizeLabel(response.headers().allValues("Content-Length").get(0));
            responsePanel.setJSONBodyText(response.body());
            responsePanel.setRowBodyText(response.body());
            responsePanel.setHeaders(response.headers().map());
//        }
        }
    }
}
