package com.metsakuur.mangosteen.mangosteendemo.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationRequest {

    private String face ;
    private String liveness ;
    private String minsize ;
    private String multiface ;

}
