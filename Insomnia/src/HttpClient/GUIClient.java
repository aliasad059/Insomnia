package HttpClient;

import GUI.InsomniaMenuBar;
import GUI.ResponsePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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

    private static class SendRequest extends SwingWorker<HttpResponse<byte[]>, String> {
        Request requestToRun;
        double elapsedTime;

        public SendRequest(Request requestToRun) {
            this.requestToRun = requestToRun;
            requestToRun.getResponsePanel().resetPanel();
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
        protected HttpResponse<byte[]> doInBackground()   {
            System.out.println("START DOINBACGROUND");
            HttpRequest httpRequest = requestToRun.makeRequest();
            System.out.println("START DOINBACGROUND");

            if (httpRequest == null) {
                System.out.println("NULL");
                return null;
            } else {
                System.out.println("START DOINBACGROUND");

                HttpClient client;
                HttpClient.Builder builder = HttpClient.newBuilder();
                System.out.println("START DOINBACGROUND");

//                if (InsomniaMenuBar.isFollowRedirect()) {
//                    requestToRun.setFollowRedirect(true);
//                    builder.followRedirects(HttpClient.Redirect.ALWAYS);
//                } else {
//                    requestToRun.setFollowRedirect(false);
//                    builder.followRedirects(HttpClient.Redirect.NEVER);
//                }
//                builder.version(HttpClient.Version.HTTP_1_1);
                client = builder.build();
                System.out.println("START DOINBACGROUND");

                double startTime = System.nanoTime();
                System.out.println("SENDING");
                HttpResponse<byte[]> response = null;
                try {
                    response = client.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
                } catch (IOException | InterruptedException e) {
                    System.out.println("EXIOIOIOIO");
                    e.printStackTrace();
                }
                System.out.println("SENT");
                if (response == null) {
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
            HttpResponse<byte[]> response = null;
            try {
                System.out.println("Entering GET");
                response = get();
                System.out.println("GET DONE");
                if (response == null) {
                    System.out.println("GET RETURNS NULL");
                }
            } catch (InterruptedException | ExecutionException e) {
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
            responsePanel.setTimeLabel(elapsedTime + timeSize);
            responsePanel.setSizeLabel(response.body().length / 1024 + "kB");

            int statusCode = response.statusCode();
            if (statusCode>=500){
                JLabel label = new JLabel(statusCode+" Error");
                label.setOpaque(true);
                label.setBackground(Color.RED);
                label.setForeground(Color.BLACK);
                label.setToolTipText("Server Error");
                responsePanel.setStatusLabel(label);
            }else if (statusCode>=400){
                JLabel label = new JLabel(statusCode+" Error");
                label.setOpaque(true);
                label.setBackground(Color.RED);
                label.setForeground(Color.BLACK);
                label.setToolTipText("Client Error");
                responsePanel.setStatusLabel(label);
            }else if (statusCode>=300){
                JLabel label = new JLabel(statusCode+" Moved");
                label.setOpaque(true);
                label.setBackground(Color.ORANGE);
                label.setForeground(Color.BLACK);
                label.setToolTipText("Redirection");
                responsePanel.setStatusLabel(label);
            }else if (statusCode>=200){
                JLabel label = new JLabel(statusCode+" OK");
                label.setOpaque(true);
                label.setBackground(Color.GREEN);
                label.setForeground(Color.BLACK);
                label.setToolTipText("Successful");
                responsePanel.setStatusLabel(label);
            }

            if (response.headers().map().get("content-type").get(0).contains("image")) {
                System.out.println("IMAGE");
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(response.body());
                try {
                    Image image = ImageIO.read(byteArrayInputStream);
                    responsePanel.setPreviewContent(image);
                } catch (IOException ignored) {
                }
            }
            if (response.headers().map().get("content-type").get(0).contains("application/json")) {
                responsePanel.setJSONBodyContent(new String(response.body()));
            }
            responsePanel.setRowBodyContent(new String(response.body()));
            responsePanel.setHeaders(response.headers().map());
            responsePanel.revalidate();
        }
    }
}
