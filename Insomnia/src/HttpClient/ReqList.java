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
    public void saveList(){
        try (FileOutputStream fout=new FileOutputStream("./save/lists.txt",true);
             ObjectOutputStream objWriter=new ObjectOutputStream(fout)){
            objWriter.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public String getListName(){
        return listName;
    }
    public void addReq(Request requestToAdd){
        requests.add(requestToAdd);
    }
    public void printList(){
        for (int i = 0; i < requests.size(); i++) {
            System.out.print((i+1)+". ");
            requests.get(i).printRequest();
        }
    }
}
