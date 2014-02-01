package com.games.dots.ui.controllers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.games.dots.repositories.IRepository;
import com.games.dots.ui.entities.ActionList;
import com.games.dots.ui.entities.UserType;
import com.games.dots.utilities.ConfigurationManager;
import com.restfb.DefaultFacebookClient;
import com.restfb.DefaultWebRequestor;
import com.restfb.FacebookClient;
import com.restfb.WebRequestor;
import com.restfb.types.User;

/**
 * Handles requests for the application home page.
 */
@Controller
public class FacebookController {
	
	private static final Logger logger = LoggerFactory.getLogger(FacebookController.class);
	
	@Resource(name="playersRepository")
	IRepository<com.games.dots.ui.entities.User> m_players;
	
	@Autowired
	private ConfigurationManager m_configurationManger;
	/**
	 * Simply selects the home view to render by returning its name.
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	
	public ResponseEntity<?> authorizationResult(
			@RequestParam(required = false) String code, 
			@RequestParam(required = false) String error,
			Locale locale
			) throws URISyntaxException, IOException {
		if (code != null){
			FacebookClient.AccessToken token = getFacebookUserToken(code, m_configurationManger.getFbCanvasUrl());
			FacebookClient facebookClient = new DefaultFacebookClient(token.getAccessToken());
			User fbuser = facebookClient.fetchObject("me", User.class);
			com.games.dots.ui.entities.User user = new com.games.dots.ui.entities.User();
	        user.id = fbuser.getId();
	        user.setUserType(UserType.FBUser);
	        m_players.add(user);
		}
        
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setLocation(new URI(m_configurationManger.getFbCanvasPage()));
	    
		logger.info("Welcome home! The client locale is {}.", locale);
		
		
		return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ModelAndView fBAuthorization(HttpServletRequest request) throws Exception{
		
			ModelAndView mav = new ModelAndView("home");
		
			Base64 base64 = new Base64(true);
			String[] signedRequest = request.getParameter("signed_request").split("\\.", 2);

			String sig = new String(base64.decode(signedRequest[0].getBytes("UTF-8")));
			JsonFactory factory = new JsonFactory();
			ObjectMapper mapper = new ObjectMapper(factory); 
			TypeReference<HashMap<String,Object>> typeRef = new TypeReference<HashMap<String,Object>>() {};			
			String string = new String(base64.decode(signedRequest[1].getBytes("UTF-8")));
			byte[] array = string.getBytes("UTF-8");
			HashMap<String,String> data = mapper.readValue(array, typeRef); 
			
			//check signature algorithm
            if(!data.get("algorithm").equals("HMAC-SHA256")) {
                //unknown algorithm is used
                return null;
            }

            //check if data is signed correctly
            if(!hmacSHA256(signedRequest[1], m_configurationManger.getFbSecretKey()).equals(sig)) {
                //signature is not correct, possibly the data was tampered with
                return null;
            }
            
            //check if user authorized the app
            if(!data.containsKey("user_id") || !data.containsKey("oauth_token")) {
                //this is guest, create authorization url that will be passed to javascript
                //note that redirect_uri (page the user will be forwarded to after authorization) is set to fbCanvasUrl
                mav.addObject("redirectUrl", "https://www.facebook.com/dialog/oauth?client_id=" + m_configurationManger.getFbAppId() + 
                        "&redirect_uri=" + URLEncoder.encode(m_configurationManger.getFbCanvasUrl(), "UTF-8") + 
                        "&scope=");
            } else {
                //this is authorized user, get their info from Graph API using received access token
                String accessToken = data.get("oauth_token");
                FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
                User user = facebookClient.fetchObject("me", User.class);
                mav.addObject("fbAppId", m_configurationManger.getFbAppId());
                mav.addObject("user", user);
            }
			

            return mav;
		
		
	}
	
	//HmacSHA256 implementation 
    private String hmacSHA256(String data, String key) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);
        byte[] hmacData = mac.doFinal(data.getBytes("UTF-8"));
        return new String(hmacData);
    }
    private FacebookClient.AccessToken getFacebookUserToken(String code, String redirectUrl) throws IOException {
        String appId = m_configurationManger.getFbAppId();
        String secretKey = m_configurationManger.getFbSecretKey();

        WebRequestor wr = new DefaultWebRequestor();
        WebRequestor.Response accessTokenResponse = wr.executeGet(
                "https://graph.facebook.com/oauth/access_token?client_id=" + appId + "&redirect_uri=" + redirectUrl
                + "&client_secret=" + secretKey + "&code=" + code);

        return DefaultFacebookClient.AccessToken.fromQueryString(accessTokenResponse.getBody());
    }
	
}
