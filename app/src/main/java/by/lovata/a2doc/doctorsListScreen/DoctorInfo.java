package by.lovata.a2doc.doctorsListScreen;


public class DoctorInfo {

    String url_img;
    String full_name;
    String speciality;
    String price_of_consultation;
    String service;
    String gps;

    DoctorInfo(String url_img, String full_name, String speciality,
                String price_of_consultation, String service, String gps) {
        this.url_img = url_img;
        this.full_name = full_name;
        this.speciality = speciality;
        this.price_of_consultation = price_of_consultation;
        this.service = service;
        this.gps = gps;
    }

}
