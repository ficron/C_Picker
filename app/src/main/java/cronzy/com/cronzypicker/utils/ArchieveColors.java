package cronzy.com.cronzypicker.utils;

import java.util.ArrayList;

import cronzy.com.cronzypicker.constants.Constants;

public class ArchieveColors implements Constants{

    ArrayList<Integer> archiveList;

    public ArchieveColors(){
        archiveList = new ArrayList<>();
        completeList();
    }

    public void addColorToArchieve(int color){
        if(archiveList.get(archiveList.size()-1)!=color){
            archiveList.add(color);
        }
    }

    public ArrayList<Integer> getListOfColors(){
        if(archiveList.size()<6) {
            completeList();
        }
        return archiveList;
    }

    public int[] getMassOfColors(){
        if(archiveList.size()<6) {
            completeList();
        }

        int[] output = new int[archiveList.size()];
        for (int i = 0; i != archiveList.size(); i++) {
            output[i] = archiveList.get(i);
        }
        return output;
    }

    private void completeList(){
        for(int b=0; archiveList.size()<6;b++){
            archiveList.add(Constants.colors[b]);
        }
    }

}
