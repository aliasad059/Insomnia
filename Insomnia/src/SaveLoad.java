import GUI.Display;
import GUI.InsomniaFrame;
import GUI.InsomniaMenuBar;
import HttpClient.ReqList;
import HttpClient.Request;

import java.io.*;
import java.util.ArrayList;

public class SaveLoad {
    //TODO:
    //url ,queries , headers url preview ,body for each request
    //
    //
    ArrayList<InsomniaFrame> workSpaces;

    public SaveLoad(ArrayList<InsomniaFrame> workSpaces) {
        this.workSpaces = workSpaces;
    }

    public Display load() {
        File[] workSpaces = new File("./save/workSpaces").listFiles();
        if (workSpaces == null) {
            System.out.println("Not found :./save/workSpaces");
            return null;
        }
        ArrayList<InsomniaFrame>workSpaceFrames = new ArrayList<>();
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
            workSpaceFrames.add(makeInsomniaFrame(file.getName(),reqLists,requests));
        }
        return new Display(workSpaceFrames,workSpaceFrames.get(0));
    }

    public void save() {
        for (InsomniaFrame frame : workSpaces) {
            for (Request request : frame.getRequests()) {
                request.saveRequest("./save/workSpaces/" + frame.getName() + "/requests.txt");
            }
            for (ReqList reqList : frame.getReqLists()) {
                reqList.saveList("./save/workSpaces/" + frame.getName() + "/" + reqList.getListName() + "_list.txt");
            }
        }
    }
    private InsomniaFrame makeInsomniaFrame(String name ,ArrayList<ReqList>reqLists,ArrayList<Request>requests){
        return new InsomniaFrame(name,reqLists,requests);
    }

}
