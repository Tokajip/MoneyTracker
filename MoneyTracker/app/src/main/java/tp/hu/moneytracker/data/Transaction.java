package tp.hu.moneytracker.data;

import java.io.Serializable;

/**
 * Created by Peti on 2015.03.22..
 */
public class Transaction implements Serializable {
    private String title;
    private long date;
    private int price;
    private String category;
    private transactionType type = transactionType.income;

    private enum transactionType {income, outgo};

    public Transaction(){};

    public Transaction(String title, long date, int price, String cat) {
        this.title = title;
        this.date = date;
        this.price = price;
        this.category=cat;
        if (price > 0) {
            type = transactionType.income;
        } else {
            type = transactionType.outgo;
        }

    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getType() {
        return type != null ? type.toString() : "";
    }

    public void setType(int i){
        type = i==0 ? transactionType.outgo : transactionType.income;
    }
    @Override
    public String toString() {
        return "title;"+getTitle()+";date;"+getDate()+";price;"+ getDate()+";type;"+getType()+";category;"+getCategory();
    }
}
