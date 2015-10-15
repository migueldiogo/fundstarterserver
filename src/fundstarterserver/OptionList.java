package fundstarterserver;

import java.util.ArrayList;

/**
 * Created by Miguel Prata Leal on 14/10/15.
 */
public class OptionList extends ArrayList<String>{
    public OptionList() {}


    public void addOption(String option) {
        this.add(option);
    }

    public int getSize() {
        return this.size();
    }


}
