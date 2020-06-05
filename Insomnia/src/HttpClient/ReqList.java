package HttpClient;

import java.io.*;
import java.util.ArrayList;

public class ReqList implements Serializable {
    private ArrayList<Request> requests;
    private String listName;

    public ReqList(String listName) {
        requests = new ArrayList<>();
        this.listName = listName;
        saveList("./../save/lists/list_"+listName+".txt");
    }

    /**
     * save list
     */
    public void saveList(String path){
        try (FileOutputStream fout=new FileOutputStream(path);
             ObjectOutputStream objWriter=new ObjectOutputStream(fout)){
            objWriter.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get list name
     * @return list name
     */
    public String getListName(){
        return listName;
    }

    /**
     * adds a new request to list and update list
     * @param requestToAdd request to add
     */
    public void addReq(Request requestToAdd){
        requests.add(requestToAdd);
        saveList("./../save/lists/list_"+listName+".txt");
    }

    /**
     * print list requests
     */
    public void printList(){
        for (int i = 0; i < requests.size(); i++) {
            System.out.print((i+1)+". ");
            requests.get(i).printRequest();
        }
    }

    /**
     * get index-th request
     * @param index request index
     * @return request
     */
    public Request getRequest(int index) {
        return requests.get(index);
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }
}
