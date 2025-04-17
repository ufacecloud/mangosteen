package com.metsakuur.mangosteen.mangosteendemo.service;

import com.metsakuur.mangosteen.mangosteendemo.model.TotpData;
import com.metsakuur.uface.mangosteen.UFaceMangosteen;
import com.metsakuur.uface.mangosteen.exception.FRException;
import com.metsakuur.uface.mangosteen.model.results.FaceCompareResult;
import com.metsakuur.uface.mangosteen.model.results.FaceCropData;
import com.metsakuur.uface.mangosteen.model.results.FaceValidationResult;
import com.metsakuur.uface.mangosteen.model.results.FeatureTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class UFaceService {

    private final UFaceMangosteen uFaceMangosteen;

    public UFaceService(@Autowired  UFaceMangosteen uFaceMangosteen) {
        this.uFaceMangosteen = uFaceMangosteen;
    }

    public FaceValidationResult validateFaces(byte[] image, int faceDetectionSizeThreshold,
                                             boolean checkMultiface, boolean checkLiveness) throws FRException {
        return uFaceMangosteen.invalidateFaces(image, faceDetectionSizeThreshold, checkMultiface, checkLiveness);
    }

    public FaceCropData cropFace(byte[] image) throws FRException {
        return uFaceMangosteen.getFaceCropData(image , 100);
    }

    public FaceCompareResult compareFaceToFace(float threshold, byte[] source, byte[] target) throws FRException {
        return uFaceMangosteen.compareFaceToFace(threshold, source, target);
    }

    public FeatureTemplate compareTemplateToTemplate(byte[] image) throws FRException {
        return uFaceMangosteen.getFeatureTemplate(image) ;
    }

    public byte [] decryptTotp(TotpData data) throws FRException {
        byte [] source = Base64.getDecoder().decode(data.getData());
        return uFaceMangosteen.MKDecryptedDataOTP( 6 , System.currentTimeMillis() / 1000 , -120 ,  data.getUuid() , data.getRand(), source , source.length);
    }

    public byte[] encryptTotp(TotpData data) throws FRException {
        byte[] source = data.getData().getBytes();
        return uFaceMangosteen.MKEncryptedDataOTP(data.getUuid(), data.getRand(), source, source.length);
    }

    public byte [] encryptData(byte [] data) {
        return uFaceMangosteen.encryptData(data) ;
    }

    public byte [] decryptData(byte [] data) {
        return uFaceMangosteen.decryptData(data);
    }

}
