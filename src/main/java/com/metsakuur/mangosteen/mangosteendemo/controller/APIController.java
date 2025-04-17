package com.metsakuur.mangosteen.mangosteendemo.controller;

import com.metsakuur.mangosteen.mangosteendemo.model.ResponseModel;
import com.metsakuur.mangosteen.mangosteendemo.model.TotpData;
import com.metsakuur.mangosteen.mangosteendemo.model.request.ValidationRequest;
import com.metsakuur.mangosteen.mangosteendemo.service.UFaceService;
import com.metsakuur.mangosteen.mangosteendemo.util.StringUtil;
import com.metsakuur.uface.mangosteen.Constants;
import com.metsakuur.uface.mangosteen.exception.FRException;
import com.metsakuur.uface.mangosteen.model.results.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@RestController
@Slf4j
public class APIController
{

    @Autowired
    private UFaceService uFaceService;

    @PostMapping("/api/encode")
    public @ResponseBody ResponseModel<String> encryptData(@RequestParam String data) {
        ResponseModel<String> response = new ResponseModel<String>();
        response.setMessage(Constants.OK+ "");
        try {
            byte [] encrypted = uFaceService.encryptData(data.getBytes());
            String encryptedString = Base64.getEncoder().encodeToString(encrypted);
            response.setData(encryptedString);
        } catch (Exception e) {
            response.setCode("" + Constants.ERROR);
            response.setMessage(e.getMessage());
            response.setData("");
        }
        return response;
    }

    @PostMapping("/api/decode")
    public @ResponseBody ResponseModel<String> decryptData(@RequestParam String data) {
        ResponseModel<String> response = new ResponseModel<String>();
        response.setMessage(Constants.OK+ "");
        try {
            byte [] decoded = Base64.getDecoder().decode(data);
            byte [] decrypted = uFaceService.decryptData(decoded);
            response.setData(new String(decrypted));
        } catch (Exception e) {
            response.setCode("" + Constants.ERROR);
            response.setMessage(e.getMessage());
            response.setData("");
        }
        return response;
    }


    @PostMapping("/api/totp/decrypt")
    public @ResponseBody ResponseModel<String> checkTOTP(@RequestBody TotpData data) {
        ResponseModel<String> response = new ResponseModel<String>();
        response.setMessage("OK");
        try {
            response.setCode(""+Constants.OK);
            byte [] decrypted = uFaceService.decryptTotp(data);
            response.setData(new String(decrypted));
        } catch (FRException e) {
            response.setCode("" + e.getReason());
            response.setMessage(e.getMessage());
            response.setData("");
        }
        return response;
    }

    @PostMapping("/api/totp/encrypt")
    public @ResponseBody ResponseModel<TotpData> encryptTOTP(@RequestParam(name="data") String message , @RequestParam(name="uuid") String uuid) {
        ResponseModel<TotpData> response = new ResponseModel<TotpData>();
        response.setMessage("OK");
        try {
            TotpData totpData = new TotpData();
            totpData.setRand(StringUtil.generateRandomString(5));
            totpData.setUuid(uuid);
            response.setCode(""+Constants.OK);
            totpData.setData(message);
            byte [] encrypted = uFaceService.encryptTotp(totpData);
            String encryptedString = Base64.getEncoder().encodeToString(encrypted);
            totpData.setData(new String(encryptedString));
            response.setData(totpData);
        } catch (FRException e) {
            response.setCode("" + e.getReason());
            response.setMessage(e.getMessage());
            response.setData(null);
        }
        return response;
    }

    @PostMapping("/api/validate")
    public @ResponseBody FaceValidationResult validateFace(@RequestBody ValidationRequest request) throws Exception {

        byte [] face = Base64.getDecoder().decode(request.getFace());

        return uFaceService.validateFaces(face ,
                  Integer.parseInt( request.getMinsize() ) ,
                  "Y".equals(request.getMultiface())
                  ,  "Y".equals(request.getLiveness()) );

    }

    @PostMapping("/api/crop")
    public @ResponseBody FaceCropData cropFace(@RequestParam MultipartFile image) throws FRException, IOException {
        byte [] face = image.getBytes() ;
        return uFaceService.cropFace(face);
    }

    @PostMapping("/api/compare")
    public @ResponseBody FaceCompareResult compareFace(@RequestParam String threshold ,
                                                       @RequestParam MultipartFile source ,
                                                       @RequestParam MultipartFile target) throws FRException, IOException {
        float thresholdValue = Float.parseFloat(threshold);
        log.info("thresholdValue: " + thresholdValue);
        byte [] sourceImage = source.getBytes() ;
        byte [] targetImage = target.getBytes() ;
        log.info("sourceImage: " + source.getOriginalFilename()) ;
        log.info("targetImage: " + target.getOriginalFilename()) ;
        return uFaceService.compareFaceToFace(thresholdValue , sourceImage , targetImage);
    }


    @PostMapping("/api/template")
    public @ResponseBody FeatureTemplate getTemplate(@RequestParam MultipartFile image) throws FRException, IOException {
        byte [] face = image.getBytes() ;
        return uFaceService.compareTemplateToTemplate(face);
    }

    @ExceptionHandler(FRException.class)
    public @ResponseBody ResponseModel<String> handleException(HttpServletResponse resp , FRException e) {
        ResponseModel<String> response = new ResponseModel<String>();
        response.setCode("" + e.getReason());
        response.setMessage(e.getMessage());
        response.setData("");
        return response;
    }
}
