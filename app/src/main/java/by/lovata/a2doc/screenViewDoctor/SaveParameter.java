package by.lovata.a2doc.screenViewDoctor;


import java.util.Map;

public class SaveParameter {

    DoctorInfo[] doctorsInfo;
    Map<Integer, String> sevices;
    Map<Integer, OrganizationInfo> organizations;

    SaveParameter(DoctorInfo[] doctorsInfo, Map<Integer, String> sevices,
                    Map<Integer, OrganizationInfo> organizations) {
        this.doctorsInfo = doctorsInfo;
        this.sevices = sevices;
        this.organizations = organizations;
    }

}
