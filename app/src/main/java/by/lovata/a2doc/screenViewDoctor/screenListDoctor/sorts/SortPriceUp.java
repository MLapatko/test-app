package by.lovata.a2doc.screenViewDoctor.screenListDoctor.sorts;


import java.util.Comparator;

import by.lovata.a2doc.screenViewDoctor.DoctorInfo;

public class SortPriceUp implements Comparator<DoctorInfo> {

    private int id_filter;

    public SortPriceUp(int id_filter) {
        this.id_filter = id_filter;
    }

    @Override
    public int compare(DoctorInfo o1, DoctorInfo o2) {
        return o1.getService_list().get(id_filter) - o2.getService_list().get(id_filter);
    }

}