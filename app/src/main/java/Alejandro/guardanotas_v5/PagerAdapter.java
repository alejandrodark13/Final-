package Alejandro.guardanotas_v5;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import Alejandro.guardanotas_v5.Datos.POJO_Nota;



public class PagerAdapter extends FragmentStatePagerAdapter {

    private int numTabs;
    private POJO_Nota[] dummyModels0;
    private POJO_Nota[] dummyModels1;


    public PagerAdapter(FragmentManager fm, int numTabs, POJO_Nota[] dummyModels0, POJO_Nota[] dummyModels1){

        super(fm);
        this.numTabs = numTabs;
        this.dummyModels0 = dummyModels0;
        this.dummyModels1 = dummyModels1;

    }
    //muestra la posicion que nos indica las interfaces de tarea o nota
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ItemFragment tab1 = ItemFragment.newInstance(dummyModels0);
                return tab1;
            case 1:
                ItemFragment tab2 = ItemFragment.newInstance(dummyModels1);
                return tab2;
            default:
                throw new RuntimeException("Tab position invalid " + position);
        }

    }
    //muestra el total de pantallas que muestra la primer pantalla
    @Override
    public int getCount() {
        return numTabs;
    }

}
