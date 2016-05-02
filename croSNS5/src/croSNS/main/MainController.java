package croSNS.main;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import croSNS.social.*;
import facebook4j.Facebook;
import facebook4j.Post;
import facebook4j.ResponseList;


@Controller
public class MainController {
	
/*	@RequestMapping(value="/mainin.do")
	public ModelAndView main(SocialController conn, HttpSession session, HttpServletRequest request) throws Exception {
		
		ModelAndView mav = new ModelAndView();
    	String fe = request.getParameter("fe");
    	
		//セッションの中身がない場合
		if(session.getAttribute("userLoginInfo") == null){
			mav.setViewName("/login/login");
			return mav;
		}
		mav.addObject("test", "test111");
		
		mav.setViewName("/main/main");
		return mav;
    }*/
	
	@RequestMapping(value="/main.do")
	public ModelAndView main(ModelAndView mv, HttpSession session, HttpServletRequest request) throws Exception {
    	
		
		if(session.getAttribute("userLoginInfo") == null){
			mv.setViewName("/login/login");
			return mv ;
		}
		
		mv.setViewName("/main/main");
		return mv;
		
    }
}
