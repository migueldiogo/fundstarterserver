package fundstarterserver;


/**
 * Created by Miguel Prata Leal on 14/10/15.
 */
public class Menu {
    private OptionList optionsList;
    private String answerPrompt;

    public Menu() {
        this.optionsList = new OptionList();
        this.answerPrompt = "";
    }

    public void addOption(String option) {
        optionsList.add(option);
    }



    public void setAnswerPrompt(String answerPrompt) {
        this.answerPrompt = answerPrompt;
    }

    public OptionList getOptionsList() {
        return optionsList;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < optionsList.getSize(); i++) {
            result.append(i + " - ");
            result.append(optionsList.get(i));
            result.append("\n");
        }
        result.append(answerPrompt);

        return result.toString();
    }
}
