package HttpClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class ReqList implements Serializable {
    private ArrayList<Request> requests;
    private String listName;

    public ReqList(String listName) {
        requests = new ArrayList<>();
        this.listName = listName;
        saveList();
    }

    /**
     * save list
     */
    public void saveList(){
        try (FileOutputStream fout=new FileOutputStream("./../save/lists/"+listName+".insomnia");
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
        saveList();
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
}
