package com.metsakuur.mangosteen.mangosteendemo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseModel<T> {

    private String code ;
    private String message;

    private T data ;

}
