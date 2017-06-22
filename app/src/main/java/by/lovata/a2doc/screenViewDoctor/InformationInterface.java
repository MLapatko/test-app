package by.lovata.a2doc.screenViewDoctor;


import java.util.Map;

public interface InformationInterface {
    public DoctorInfo[] getDoctors();
    public Map<Integer, String> getSevices();
    Map<Integer, OrganizationInfo> getOrganizations();
}
