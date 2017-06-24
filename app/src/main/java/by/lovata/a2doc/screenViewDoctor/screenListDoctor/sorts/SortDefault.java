package by.lovata.a2doc.screenViewDoctor.screenListDoctor.sorts;


import java.util.Comparator;

import by.lovata.a2doc.screenViewDoctor.DoctorInfo;

public class SortDefault implements Comparator<DoctorInfo>{

    @Override
    public int compare(DoctorInfo o1, DoctorInfo o2) {
        return o1.full_name.compareTo(o2.full_name);
    }

}
