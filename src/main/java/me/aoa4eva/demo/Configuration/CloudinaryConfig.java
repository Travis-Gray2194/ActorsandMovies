
package me.aoa4eva.demo.Configuration;
import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.Transformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CloudinaryConfig {

    //    private Cloudinary cloudinary
//    This is an object that will be used to apply your image tranfomations, and generate a URL with those
//    transfomlations so that you can use the image later.
//    In the class, you will see the following:
    private Cloudinary cloudinary;

    //    public CloudinaryConfig
//    This constructor for the class you have created. It takes the values of the cloudinary settings you entered in
//    your application.properties file, and assigns those to the variables that the Singleton class will use to connect to
//            Cloudinary.
    @Autowired

    public CloudinaryConfig(
            @Value("${cloudinary.apikey}") String key,
            @Value("${cloudinary.apisecret}") String secret,
            @Value("${cloudinary.cloudname}") String cloud
    ) {
        cloudinary = Singleton.getCloudinary();
        cloudinary.config.cloudName=cloud;
        cloudinary.config.apiSecret=secret;
        cloudinary.config.apiKey=key;
    }

    //    public Map upload (Object file, Map options)
//    This passes the file from the user's computer to Cloudinary, returning an object which contains all of the
//    properties for the transformed image.
    public Map upload(Object file, Map options){
        try{
            return cloudinary.uploader().upload(file, options);}catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public String createUrl(String name,int width, int height,String action){
        return cloudinary.url()
                .transformation(new Transformation()
                .width(width).height(height)
                .border("2px_solid_black").crop(action))
                .imageTag(name);
    }
}
