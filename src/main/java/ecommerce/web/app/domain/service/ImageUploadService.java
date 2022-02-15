package ecommerce.web.app.domain.service;

import ecommerce.web.app.domain.repository.ImageUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageUploadService {

    public final ImageUploadRepository imageUploadRepository;

    public ImageUploadService(ImageUploadRepository imageUploadRepository){
        this.imageUploadRepository = imageUploadRepository;
    }

    public void deleteImageUploaded(long id){
        imageUploadRepository.deleteById(id);
    }
}
