package HttpClient;

import GUI.Display;
import GUI.InsomniaFrame;
import GUI.InsomniaMenuBar;
import GUI.ResponsePanel;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GUIClient {
    public GUIClient() {
    }

    public static Display load() {
        System.out.println("Enter of load");
        File[] workSpaces = new File("./save/workSpaces").listFiles();
        if (workSpaces == null) {
            System.out.println("Not found :./save/workSpaces");
            return null;
        }
        ArrayList<InsomniaFrame> workSpaceFrames = new ArrayList<>();
        for (File file : workSpaces) {
            ArrayList<Request> requests = new ArrayList<>();
            ArrayList<ReqList> reqLists = new ArrayList<>();
            if (file.isDirectory()) {
                File[] workSpaceContent = file.listFiles();
                if (workSpaceContent != null) {
                    for (File workSpaceFile : workSpaceContent) {
                        if (workSpaceFile.getName().contains("list")) {
                            try (FileInputStream finLists = new FileInputStream(workSpaceFile);
                                 ObjectInputStream listsReader = new ObjectInputStream(finLists)
                            ) {
                                ReqList reqList = (ReqList) listsReader.readObject();
                                reqLists.add(reqList);
                                for (Request request : reqList.getRequests()) {
                                    request.initResponsePanel();
                                    request.initMiddlePanel();
                                }
                            } catch (ClassNotFoundException | IOException ignored) {
                            }
                        } else {
                            try (FileInputStream finRequests = new FileInputStream(workSpaceFile);
                                 ObjectInputStream requestsReader = new ObjectInputStream(finRequests);
                            ) {
                                while (true) {
                                    Request request = (Request) requestsReader.readObject();
                                    System.out.println(request.getRequestName());
                                    request.initMiddlePanel();
                                    request.initResponsePanel();
                                    requests.add(request);
                                }
                            } catch (ClassNotFoundException | IOException ignored) {
                            }
                        }
                    }
                }
            }
            if(reqLists.size()!=0 || requests.size()!=0) {
                workSpaceFrames.add(makeInsomniaFrame(file.getName(), reqLists, requests));
            }
        }
        if (workSpaceFrames.size()!= 0) {
            System.out.println("load done");

            return new Display(workSpaceFrames, workSpaceFrames.get(0));
        }
        System.out.println("load failed");
        return null;
    }

    public static void save() {
        ArrayList<InsomniaFrame>workSpaces = Display.getWorkSpaces();
        for (InsomniaFrame frame : workSpaces) {
            for (Request request : frame.getRequests()) {
                System.out.println("yes");
                request.saveRequest("./save/workSpaces/" + frame.getTitle() + "/requests.txt");
            }
            for (ReqList reqList : frame.getReqLists()) {
                reqList.saveList("./save/workSpaces/" + frame.getTitle() + "/" + reqList.getListName() + "_list.txt");
            }
        }
        System.out.println("SAVE DONE");
    }
    private static InsomniaFrame makeInsomniaFrame(String name ,ArrayList<ReqList>reqLists,ArrayList<Request>requests){
        return new InsomniaFrame(name,reqLists,requests);
    }

    public static void addRequest(Request requestToAdd) {
    }

    public static void addFolder() {
    }

    public static void addRequestTo() {
    }

    public static void runRequest(Request requestToRun) {
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
        protected HttpResponse<byte[]> doInBackground() {
            HttpRequest httpRequest = requestToRun.makeRequest();
            if (httpRequest == null) {
                System.out.println("NULL");
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
                HttpResponse<byte[]> response = null;
                try {
                    response = client.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                elapsedTime = System.nanoTime() - startTime;
                requestToRun.setLastResponse(response);
                return response;
            }
        }

        @Override
        protected void done() {
            HttpResponse<byte[]> httpResponse = null;
            try {
                httpResponse = get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            Response response = new Response(httpResponse);
            response.setResponseTime(elapsedTime);
            requestToRun.setResponse(response);
            ResponsePanel responsePanel = requestToRun.getResponsePanel();
            responsePanel.updatePanel(response);
        }
    }
}
