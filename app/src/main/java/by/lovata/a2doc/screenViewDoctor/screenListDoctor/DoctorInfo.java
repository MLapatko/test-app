package by.lovata.a2doc.screenViewDoctor.screenListDoctor;


public class DoctorInfo {

    int url_img;
    String full_name;
    String speciality;
    String price_of_consultation;
    String service;
    String review;
    String gps;

    DoctorInfo(int url_img, String full_name, String speciality,
                String price_of_consultation, String service, String gps, String review) {
        this.url_img = url_img;
        this.full_name = full_name;
        this.speciality = speciality;
        this.price_of_consultation = price_of_consultation;
        this.service = service;
        this.review = review;
        this.gps = gps;
    }

}
