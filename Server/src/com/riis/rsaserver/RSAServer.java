package com.riis.rsaserver;

import javax.ws.rs.*;

import org.json.JSONObject;
import org.keyczar.Crypter;
 
@Path("/decrypt")
public class RSAServer {
    @Path("havepublic")
    @GET
    @Produces("application/json")
    public String doDecrypt(@QueryParam("text") String text) {
    	try {
	    	return (new JSONObject().put("result", new Crypter(new MyKeyczarReader()).decrypt(text))).toString();
    	} catch (Exception ex) {
    		return ex.getMessage();
    	}
    }
}