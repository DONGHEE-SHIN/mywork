package croSNS.contents;

import org.springframework.web.multipart.MultipartHttpServletRequest;

public interface ContentsService {

		public String fileUpload(MultipartHttpServletRequest mRequest) throws Exception;

}
