package croSNS.contents;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ContentsController {

	@RequestMapping(value="/contents.do")
	public ModelAndView contents(ModelAndView mv, HttpSession session, HttpServletRequest request) throws Exception {

		//«»«Ã«·«ç«óªÎñéãóª¬ªÊª¤íÞùê
		if(session.getAttribute("userLoginInfo") == null){
			mv.setViewName("/login/login");
			return mv;
		}
		mv.setViewName("/contents/totalRelease");
		return mv;
    }
}
