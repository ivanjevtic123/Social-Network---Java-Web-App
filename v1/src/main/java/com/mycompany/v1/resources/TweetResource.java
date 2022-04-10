/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.v1.resources;

import entities.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import schemas.*;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.Date;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;

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
    public Response postNewTweet(@Context HttpHeaders header, PostTweetReq postTweetReq){ //http://localhost:8080/v1/v1/tweets
        List<String> headerList = header.getRequestHeader("X-Username");
        String username;
        
        
        //Username header check:
        if(headerList == null || headerList.size() <= 0) {
            schemas.Error error1 = new schemas.Error();
            error1.setHttpCode(401);
            error1.setErrorCode(401);
            error1.setMessage("Username header is missing!");
            
            return Response
                .status(401).entity(error1)
                .build();
        }
        
        username = headerList.get(0);
        
        //Username regex check:
        if(!username.matches("^[a-zA-Z0-9_]{4,32}$")) {
            schemas.Error error2 = new schemas.Error();
            error2.setHttpCode(400);
            error2.setErrorCode(421); 
            error2.setMessage("Username does not follow the specified pattern!");
            
            return Response
                .status(400).entity(error2)
                .build();
        }

        //Request body check:
        if(postTweetReq == null || postTweetReq.getTweetBody() == null || postTweetReq.getHashTags() == null || postTweetReq.getHashTags().length == 0) {
            schemas.Error error3 = new schemas.Error();
            error3.setHttpCode(400);
            error3.setErrorCode(422); 
            error3.setMessage("Request body is missing or incomplete!");
            
            return Response
                .status(400).entity(error3)
                .build();
        }
        
        User user;
        try {
            user = em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username).getSingleResult();
        } catch(javax.persistence.NoResultException exc) {
            schemas.Error error4 = new schemas.Error();
            error4.setHttpCode(400);
            error4.setErrorCode(423); 
            error4.setMessage("User does not exists!");
            
            return Response
                .status(400).entity(error4)
                .build();
        }
        
        //Creating new Tweet
        Tweet tweet = new Tweet();
        tweet.setId(em.createQuery("select max(t.id) from Tweet t", Integer.class).getSingleResult() + 1);
        tweet.setContent(postTweetReq.getTweetBody());
        tweet.setIdUser(user);
        tweet.setCreatedAt(Date.from(Instant.now()));
        em.persist(tweet);

        //Creating new HashTags, if they were not existing
        for (String hashTag : postTweetReq.getHashTags()) {
            try {
                em.createNamedQuery("Hashtag.findByHashname").setParameter("hashname", hashTag).getSingleResult();
            } catch(javax.persistence.NoResultException exc) {
                Hashtag newHashTag = new Hashtag();
                newHashTag.setId(em.createQuery("select max(ht.id) from Hashtag ht", Integer.class).getSingleResult() + 1);
                newHashTag.setHashname(hashTag);
                em.persist(newHashTag);
            }
        }

        //Creating Tweet-Hashtag Relationship table:
        for(String hashTag : postTweetReq.getHashTags()) {
            Hashtag ht = (Hashtag)em.createNamedQuery("Hashtag.findByHashname").setParameter("hashname", hashTag).getSingleResult();

            Tweethashtag newTweetHashTag = new Tweethashtag();
            newTweetHashTag.setId(em.createQuery("select max(tht.id) from Tweethashtag tht", Integer.class).getSingleResult() + 1);
            newTweetHashTag.setIdTag(ht);
            newTweetHashTag.setIdTwe(tweet);
            em.persist(newTweetHashTag);
        }

        //TweetResponse object:
        TweetResp tweetResp = new TweetResp();
        tweetResp.setTweetId(tweet.getId());
        tweetResp.setTweetBody(tweet.getContent());
        tweetResp.setHashTags(postTweetReq.getHashTags());
        tweetResp.setCreatedBy(username);
        tweetResp.setCreatedAt(tweet.getCreatedAt().toString());

        return Response
            .status(201).entity(tweetResp)
            .build();
    }
    
    
    
    
    
    @DELETE
    @Path("{tweetId}")
    public Response deleteTweet(@Context HttpHeaders header, @PathParam("tweetId") String tweetId) {
        List<String> headerList = header.getRequestHeader("X-Username");
        String username;
        
        //Username header check:
        if(headerList == null || headerList.size() <= 0) {
            schemas.Error error1 = new schemas.Error();
            error1.setHttpCode(401);
            error1.setErrorCode(401);
            error1.setMessage("Username header is missing!");
            
            return Response
                .status(401).entity(error1)
                .build();
        }
        
        username = headerList.get(0);
        User user = em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username).getSingleResult();         
        
        Tweet tweet = em.find(Tweet.class, Integer.parseInt(tweetId));
        if(tweet == null) {
            schemas.Error error3 = new schemas.Error();
            error3.setHttpCode(404);
            error3.setErrorCode(404);
            error3.setMessage("Tweet not found!");
            
            return Response
                .status(404).entity(error3)
                .build();
        }
        
        //Somebody elses tweet check:
        if(tweet.getIdUser() != user) {
            schemas.Error error2 = new schemas.Error();
            error2.setHttpCode(403);
            error2.setErrorCode(403);
            error2.setMessage("Somebody elses tweet!");
            
            return Response
                .status(403).entity(error2)
                .build();
        }
        
        List<Tweethashtag> thtList = em.createQuery("SELECT tht FROM Tweethashtag tht WHERE tht.idTwe = :id", Tweethashtag.class).setParameter("id", tweet).getResultList();
        
        String []htArr = new String[5];
        int i = 0;
        for (Tweethashtag tweethashtag : thtList) {
            Hashtag tag = tweethashtag.getIdTag();
            htArr[i] = tag.getHashname();
            i++;
        }
        
        
        TweetResp tweetResp = new TweetResp();
        tweetResp.setTweetId(tweet.getId());
        tweetResp.setTweetBody(tweet.getContent());
        tweetResp.setHashTags(htArr);
        tweetResp.setCreatedBy(username);
        tweetResp.setCreatedAt(tweet.getCreatedAt().toString());
        
        em.remove(tweet);
        return Response
            .status(200).entity(tweetResp)
            .build();
        
    }
    
    
    
    
    
}
