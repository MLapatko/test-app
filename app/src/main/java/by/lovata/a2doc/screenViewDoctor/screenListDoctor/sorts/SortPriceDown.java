package by.lovata.a2doc.screenViewDoctor.screenListDoctor.sorts;


import java.util.Comparator;

import by.lovata.a2doc.screenViewDoctor.DoctorInfo;

public class SortPriceDown implements Comparator<DoctorInfo> {

    private int id_filter;

    public SortPriceDown(int id_filter) {
        this.id_filter = id_filter;
    }

    @Override
    public int compare(DoctorInfo o1, DoctorInfo o2) {
        return o2.getService_list().get(id_filter) - o1.getService_list().get(id_filter);
    }

}