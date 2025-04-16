package com.metsakuur.mangosteen.mangosteendemo;

import com.metsakuur.uface.mangosteen.UFaceMangosteen;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MkMangosteenDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MkMangosteenDemoApplication.class, args);
    }

    static {

        String depthModel = "models/depthface48x48.yml" ;
        String fasnetModel = "models/fastvit_ma36_20241210.onnx" ;
        UFaceMangosteen.initModel(null, depthModel, fasnetModel);
        UFaceMangosteen.initTotp(null);

    }

    @Bean
    public UFaceMangosteen uFaceMangosteen() {
        return new UFaceMangosteen();
    }

}
