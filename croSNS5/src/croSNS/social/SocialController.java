/*package croSNS.social;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.Media;
import facebook4j.Photo;
import facebook4j.PhotoUpdate;
import facebook4j.Post;
import facebook4j.PostUpdate;
import facebook4j.PrivacyBuilder;
import facebook4j.PrivacyParameter;
import facebook4j.PrivacyType;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.User;
import facebook4j.VideoUpdate;
import facebook4j.FacebookException;

@Controller
public class SocialController
{
	private static Logger logger = Logger.getLogger(SocialController.class);

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private SessionObjects sessionObjects;

	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView initial()
	{
		return new ModelAndView("initial/index");
	}

	@RequestMapping(value="/facebook/", method=RequestMethod.GET)
	public ModelAndView index()
	{
		return new ModelAndView("facebook/index");
	}
	 	
	@RequestMapping(value="/show.do", method=RequestMethod.GET)
	public ModelAndView show(ModelAndView mv, HttpSession session,HttpServletRequest request) throws FacebookException
	{

		//«»«Ã«·«ç«óªÎñéãóª¬ªÊª¤íÞùê
		if(session.getAttribute("userLoginInfo") == null){
			mv.setViewName("/login/login");
			return mv;
		}
		ResponseList<Post> feed =  sessionObjects.getFacebook().getFeed();

		String id = sessionObjects.getFacebook().getId();

		ResponseList<Post> post =sessionObjects.getFacebook().getPosts(id, new Reading().fields("full_picture"));
		User user = sessionObjects.getFacebook().getUser(id);
		mv.addObject("user", user);
		mv.addObject("feed", feed);
		mv.addObject("post", post);



		mv.setViewName("/main/main");

		return mv;

	}

	@RequestMapping(value="/signin.do", method=RequestMethod.GET)
	public RedirectView signin() throws IOException
	{
		Facebook facebook = new FacebookFactory().getInstance();

		// Storing the facebook object for further use
		sessionObjects.setFacebook(facebook);


		// Building the correct URL to return to our application
		StringBuffer callbackURL = request.getRequestURL();
		callbackURL.replace(callbackURL.lastIndexOf("/"), callbackURL.length(), "").append("/callback");

		// URL to ask for the acceptance of permissions
		// It attaches the URL to return to your application
		String authAuthorizationURL = facebook.getOAuthAuthorizationURL(callbackURL.toString());

		return new RedirectView(authAuthorizationURL);
	}

	@RequestMapping(value="/callback", method=RequestMethod.GET)
	public RedirectView callback(String code) throws FacebookException, IOException
	{
		sessionObjects.getFacebook().getOAuthAccessToken(code);	
		StringBuffer url = request.getRequestURL().replace(request.getRequestURL().lastIndexOf("/"), 
				request.getRequestURL().length(), "/main.do");
		System.out.println("CallBack :: " + url);
		return new RedirectView(url.toString());
	}
		
	@RequestMapping(value="/facebook/logout", method=RequestMethod.GET)
	public RedirectView logout() throws IOException
	{
		String accessToken = sessionObjects.getFacebook().getOAuthAccessToken().getToken();

		// Flush up the session
        request.getSession().invalidate();

        // Log out of action
        StringBuffer next = request.getRequestURL().replace((request.getRequestURL().lastIndexOf("/") + 1), request.getRequestURL().length(), "");
        return new RedirectView("http://www.facebook.com/logout.php?next=" + next.toString() + "&access_token=" + accessToken);
	}
	 
	@RequestMapping(value="/post.do", method=RequestMethod.POST)
	public ModelAndView post(@RequestParam String message, @RequestParam String url, @RequestParam String photo, @RequestParam String video) throws IOException, FacebookException
	public ModelAndView post(ModelAndView mv, HttpServletRequest request) throws IOException, FacebookException
	{
		//sessionObjects.getFacebook().postStatusMessage(message);
		String status = "";
		String message = new String(request.getParameter("message").getBytes("8859_1"),"UTF-8" );
		String url = request.getParameter("url");
		String video = request.getParameter("video");
		String photo = request.getParameter("photo");

		//sessionObjects.getFacebook().postPhoto(new Media(new File(photo)));
		try {
			System.out.println("message : " + message);
			if(url.equals("") && photo.equals("") && video.equals("")) {

				status = sessionObjects.getFacebook().postStatusMessage(message);
				if( !status.equals("")) status = "message Upadte";

			} else if(!url.equals("") && photo.equals("") && video.equals("")) {

				status = sessionObjects.getFacebook().postLink(new URL(url), message);
				if( !status.equals("")) status = "url Upadte";

			} else if(url.equals("") && !photo.equals("") && video.equals("")) {

				Media media=new Media(new File(photo));
				PhotoUpdate post= new PhotoUpdate(media).message(message);
				status = sessionObjects.getFacebook().postPhoto(post);
				if( !status.equals("")) status = "post Upadte";

			} else if(url.equals("") && photo.equals("") && !video.equals("")) {

				Media videoUpdate=new Media(new File(video));
				VideoUpdate movie= new VideoUpdate(videoUpdate).title(message);
				status = sessionObjects.getFacebook().postVideo(movie);
				if( !status.equals("")) status = "video Upadte";

			} else {

				status = "error";
			}



			// Photo Upload
			
			Media media=new Media(new File(photo));
			PhotoUpdate post= new PhotoUpdate(media)
			.message(message)
			;
			String idPost = sessionObjects.getFacebook().postPhoto(post);
			System.out.println("idPost : " + idPost);
			 

			// Link Upload
			//String idLink = sessionObjects.getFacebook().postLink(new URL(url), message);

			// VideoUpload
						Media videoUpdate=new Media(new File(video));
			VideoUpdate movie= new VideoUpdate(videoUpdate).title(message);

			System.out.println("video : ");
			String idVideo = sessionObjects.getFacebook().postVideo(movie);
			System.out.println("idVideo : " + idVideo);

		} catch (FacebookException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}




		//sessionObjects.getFacebook().postVideo(videoUpdate);
		//sessionObjects.getFacebook().postFeed(postUpdate);

		//String idPost = facebook.postPhoto(message, photoUpdate);
		ModelAndView modelAndView = new ModelAndView();
		RedirectView rv = new RedirectView();
		rv.setUrl("/main.do");
		rv.setExposePathVariables(false);
		modelAndView.setView(rv);

		return modelAndView;
	}

	@RequestMapping(value="/facebookLogOut.do", method=RequestMethod.GET)
	public RedirectView logout() throws IOException
	{
		String accessToken = sessionObjects.getFacebook().getOAuthAccessToken().getToken();

		// Flush up the Facebook Session
		sessionObjects.setFacebook(null);

		// Log out of action
		StringBuffer next = request.getRequestURL().replace((request.getRequestURL().lastIndexOf("/") + 1), request.getRequestURL().length(), "");
		return new RedirectView("http://www.facebook.com/logout.php?next=" + next.toString() + "&access_token=" + accessToken);
	}

}
*/
package croSNS.social;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.Media;
import facebook4j.PhotoUpdate;
import facebook4j.Post;
import facebook4j.PostUpdate;
import facebook4j.PrivacyBuilder;
import facebook4j.PrivacyParameter;
import facebook4j.PrivacyType;
import facebook4j.ResponseList;
import facebook4j.User;
import facebook4j.VideoUpdate;
import facebook4j.auth.AccessToken;
import facebook4j.FacebookException;
import facebook4j.api.*;

@Controller
public class SocialController
{
	private static Logger logger = Logger.getLogger(SocialController.class);

	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
	private SessionObjects sessionObjects;

	/*@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView initial()
	{
		return new ModelAndView("initial/index");
	}

	@RequestMapping(value="/facebook/", method=RequestMethod.GET)
	public ModelAndView index()
	{
		return new ModelAndView("facebook/index");
	}*/



	@RequestMapping(value="/show.do", method=RequestMethod.GET)
	public ModelAndView show(HttpSession session,HttpServletRequest request) throws FacebookException
	{
		ModelAndView mav = new ModelAndView();


		//«»«Ã«·«ç«óªÎñéãóª¬ªÊª¤íÞùê
		if(session.getAttribute("userLoginInfo") == null){
			mav.setViewName("/login/login");
			return mav;
		}

		//16:40 update
		RedirectView rv = new RedirectView();
		rv.setUrl("/main.do");
		mav.setView(rv);
		if(sessionObjects.getFacebook()==null){return mav; }
		//16:40 update
		String id = sessionObjects.getFacebook().getId();
		User user = sessionObjects.getFacebook().getUser(id);
		ResponseList<Post> feed =  sessionObjects.getFacebook().getFeed();
		ResponseList<Post> post =  sessionObjects.getFacebook().getPosts();

		mav.addObject("user", user);
		mav.addObject("feed", feed);
		mav.addObject("post", post);
		mav.setViewName("/main/main");

		return mav;
	}

	@RequestMapping(value="/signin.do", method=RequestMethod.GET)
	public RedirectView signin() throws IOException
	{   
		Facebook facebook = new FacebookFactory().getInstance();

		// Storing the facebook object for further use
		sessionObjects.setFacebook(facebook);


		// Building the correct URL to return to our application
		StringBuffer callbackURL = request.getRequestURL();
		callbackURL.replace(callbackURL.lastIndexOf("/"), callbackURL.length(), "").append("/callback");

		// URL to ask for the acceptance of permissions
		// It attaches the URL to return to your application
		String authAuthorizationURL = facebook.getOAuthAuthorizationURL(callbackURL.toString());

		return new RedirectView(authAuthorizationURL);
	}

	@RequestMapping(value="/callback", method=RequestMethod.GET)
	public RedirectView callback(String code) throws FacebookException, IOException
	{   

		//sessionObjects.getFacebook().getOAuthAccessToken(code);

		/*accesscoken test*/
		//AccessToken tocken = new AccessToken("CAAYwZAI22pQcBAPiIoMAJzQZCEkSqrRUhrRNZApNIs10ZA8BDg8YL3z0ZBv7W3RjynwC4C1LzLsP1fzTkM75x5RjTQEROjZCqcZCdD6Du6bZBEHZBzVmSOHciCA8eHnEUcXxCDgcxUVL7B9MxBfP1x36HpZCgSkYTbxqYK7mC5jakzCTzgtQvRBll7fjvZAouuG8Thau7ZB4THMbrQZDZD");
		//sessionObjects.getFacebook().setOAuthAccessToken(tocken);
		//acccessToken 
		//String token = "CAACEdEose0cBAKcopWDRTcdt6m2vXkIGHLZBdcioQML1ZBEgxFi0vVeeZCKRH2yHM8QZBMJDD2vCQgz6sqczyzGvZAA86GLqQNFqxCRVrrT5h8YfwNOdx3wuzZBVcZCbA4qbwb0uAGnwbHn7pbGvZCnbTTEgvbHtf9keaKYctP3IeaEEiCAWH1EX2JmiNhwPwDq6TNemv4cZAZBGgARkeSJm5U20mwgKJRxskZD";
		//acccessToken 
		//AccessToken accessToken = new AccessToken(token);
		/*sessionObjects.getFacebook().getOAuthAccessToken(code);*/	

		StringBuffer url = request.getRequestURL().replace(request.getRequestURL().lastIndexOf("/"), 
				request.getRequestURL().length(), "/main.do");
		System.out.println("CallBack :: " + url);
		return new RedirectView(url.toString());
	}

	/*	@RequestMapping(value="/facebook/logout", method=RequestMethod.GET)
	public RedirectView logout() throws IOException
	{
		String accessToken = sessionObjects.getFacebook().getOAuthAccessToken().getToken();

		// Flush up the session
        request.getSession().invalidate();

        // Log out of action
        StringBuffer next = request.getRequestURL().replace((request.getRequestURL().lastIndexOf("/") + 1), request.getRequestURL().length(), "");
        return new RedirectView("http://www.facebook.com/logout.php?next=" + next.toString() + "&access_token=" + accessToken);
	}
	 */
	@RequestMapping(value="/post.do", method=RequestMethod.POST)
	public ModelAndView post(ModelAndView mv, HttpServletRequest request) throws IOException, FacebookException
	{
		//sessionObjects.getFacebook().postStatusMessage(message);
		String status = "";
	
		String message = new String(request.getParameter("message").getBytes("8859_1"),"UTF-8" );
		String url = new String(request.getParameter("url").getBytes("8859_1"),"UTF-8" );
		String video = new String(request.getParameter("video").getBytes("8859_1"),"UTF-8" );
		String photo = new String(request.getParameter("photo").getBytes("8859_1"),"UTF-8" );
		
		
		try {
			System.out.println("message : " + message);
			if(url.equals("") && photo.equals("") && video.equals("")) {

				status = sessionObjects.getFacebook().postStatusMessage(message);
				if( !status.equals("")) status = "message Upadte";

			} else if(!url.equals("") && photo.equals("") && video.equals("")) {

				status = sessionObjects.getFacebook().postLink(new URL(url), message);
				if( !status.equals("")) status = "url Upadte";

			} else if(url.equals("") && !photo.equals("") && video.equals("")) {

				Media media=new Media(new File(photo));
				PhotoUpdate post= new PhotoUpdate(media).message(message);
				status = sessionObjects.getFacebook().postPhoto(post);
				if( !status.equals("")) status = "post Upadte";

			} else if(url.equals("") && photo.equals("") && !video.equals("")) {

				Media videoUpdate=new Media(new File(video));
				VideoUpdate movie= new VideoUpdate(videoUpdate).title(message);
				status = sessionObjects.getFacebook().postVideo(movie);
				if( !status.equals("")) status = "video Upadte";

			} else {

				status = "error";
			}


			// Photo Upload
			/*
			Media media=new Media(new File(photo));
			PhotoUpdate post= new PhotoUpdate(media)
			.message(message)
			;
			String idPost = sessionObjects.getFacebook().postPhoto(post);
			System.out.println("idPost : " + idPost);
			 */

			// Link Upload
			//String idLink = sessionObjects.getFacebook().postLink(new URL(url), message);

			// VideoUpload
			/*			Media videoUpdate=new Media(new File(video));
			VideoUpdate movie= new VideoUpdate(videoUpdate).title(message);

			System.out.println("video : ");
			String idVideo = sessionObjects.getFacebook().postVideo(movie);
			System.out.println("idVideo : " + idVideo);*/

		} catch (FacebookException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}


		System.out.println("photo"+url);

		//sessionObjects.getFacebook().postVideo(videoUpdate);
		//sessionObjects.getFacebook().postFeed(postUpdate);

		//String idPost = facebook.postPhoto(message, photoUpdate);
		ModelAndView modelAndView = new ModelAndView();
		RedirectView rv = new RedirectView();
		rv.setUrl("/show.do");
		rv.setExposePathVariables(false);
		modelAndView.setView(rv);
		/*ModelAndView modelAndView = new ModelAndView("/main/main");*/
		modelAndView.addObject("status", status);

		return modelAndView;
	}
	@RequestMapping(value="/facebookLogOut.do", method=RequestMethod.GET)
	public RedirectView logout() throws IOException
	{
		//16:40 update
		if(sessionObjects.getFacebook()==null){return new RedirectView("/signin.do");}
		//16:40 update

		String accessToken = sessionObjects.getFacebook().getOAuthAccessToken().getToken();

		// Flush up the Facebook Session
		sessionObjects.setFacebook(null);

		// Log out of action
		StringBuffer next = request.getRequestURL().replace((request.getRequestURL().lastIndexOf("/") + 1), request.getRequestURL().length(), "");
		return new RedirectView("http://www.facebook.com/logout.php?next=" + next.toString() + "&access_token=" + accessToken);
	}
}
