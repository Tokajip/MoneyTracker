package tp.hu.moneytracker.data;

/**
 * Created by Peti on 2015.03.22..
 */
public class Transaction {
    private String title;
    private String date;
    private int price;

    public Transaction(String title, String date, int price){
        this.title=title;
        this.date=date;
        this.price=price;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
