package tp.hu.moneytracker.data;

import tp.hu.moneytracker.R;

/**
 * Created by Peti on 2015.03.31..
 */
public class TileDatas {
    private int[] titles={
        R.string.tile_income,R.string.tile_outgo,R.string.tile_date,R.string.rule};
    private int[] icons={R.drawable.tile_income,R.drawable.tile_outgo,R.drawable.calendar_icon,R.drawable.book1_144x144};

    public int[] getTitles() {
        return titles;
    }

    public int[] getIcons() {
        return icons;
    }
}
