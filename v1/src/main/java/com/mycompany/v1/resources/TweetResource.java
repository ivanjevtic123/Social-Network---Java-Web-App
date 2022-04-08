/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.v1.resources;

import entities.*;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 *
 * @author ji180550d
 */
@Path("tweets")
@Stateless
public class TweetResource {
    
    @PersistenceContext
    EntityManager em;
    
    @POST
    public void postNewTweet() {
        
    }
    
    /*@GET
    public Response test(){ //http://localhost:8080/v1/v1/tweets
        return Response
                .ok("tweetTest")
                .build();
    }*/
    
    
    
}
