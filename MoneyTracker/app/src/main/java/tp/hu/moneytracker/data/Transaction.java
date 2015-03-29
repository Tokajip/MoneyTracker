package tp.hu.moneytracker.data;

/**
 * Created by Peti on 2015.03.22..
 */
public class Transaction {
    private String title;
    private long date;
    private int price;



    private enum transactionType {income, outgo;};
    private transactionType type = transactionType.income;
    public Transaction(String title, long date, int price) {
        this.title = title;
        this.date = date;
        this.price = price;
        if (price > 0) {
            type = transactionType.income;
        } else {
            type = transactionType.outgo;
        }
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
        if (price > 0) {
            type = transactionType.income;
        } else {
            type = transactionType.outgo;
        }
    }

    public String getType() {
        return type != null ? type.toString() : "";
    }
}
