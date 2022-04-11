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
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

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
    public Response postNewTweet(@Context HttpHeaders header, PostTweetReq postTweetReq){
        List<String> headerList = header.getRequestHeader("X-Username");
        String username; 
        
        //Username header check:
        if(headerList == null || headerList.size() <= 0) {
            schemas.Error errorHeader = new schemas.Error();
            errorHeader.setHttpCode(401);
            errorHeader.setErrorCode(401);
            errorHeader.setMessage("Username header is missing!");
            
            return Response
                .status(401)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(errorHeader)
                .build();
        }
        
        username = headerList.get(0);
        
        //Username regex check:
        if(!username.matches("^[a-zA-Z0-9_]{4,32}$")) {
            schemas.Error errorRegex = new schemas.Error();
            errorRegex.setHttpCode(400);
            errorRegex.setErrorCode(421); 
            errorRegex.setMessage("Username does not follow the specified pattern!");
            
            return Response
                .status(400)
                .type(MediaType.APPLICATION_JSON_TYPE)    
                .entity(errorRegex)
                .build();
        }

        //Request body check:
        if(postTweetReq == null || postTweetReq.getTweetBody() == null || postTweetReq.getHashTags() == null || postTweetReq.getHashTags().length == 0) {
            schemas.Error errorRequestBody = new schemas.Error();
            errorRequestBody.setHttpCode(400);
            errorRequestBody.setErrorCode(422); 
            errorRequestBody.setMessage("Request body is missing or incomplete!");
            
            return Response
                .status(400)
                .type(MediaType.APPLICATION_JSON_TYPE)    
                .entity(errorRequestBody)
                .build();
        }
        
        //User search, and User does not exists check:
        User user;
        try {
            user = em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username).getSingleResult();
        } catch(javax.persistence.NoResultException exc) {
            schemas.Error errorUserNotExists = new schemas.Error();
            errorUserNotExists.setHttpCode(400);
            errorUserNotExists.setErrorCode(423); 
            errorUserNotExists.setMessage("User does not exists!");
            
            return Response
                .status(400)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(errorUserNotExists)
                .build();
        }
        
        //Tweet body check:
        if(postTweetReq.getTweetBody().length() > 320) {
            schemas.Error errorTweetBody = new schemas.Error();
            errorTweetBody.setHttpCode(400);
            errorTweetBody.setErrorCode(424); 
            errorTweetBody.setMessage("Tweet body is too long!");
            
            return Response
                .status(400)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(errorTweetBody)
                .build();
        }
        
        //Tweet hashtags regex check:
        for (String hashTagElem : postTweetReq.getHashTags()) {
            if(!hashTagElem.matches("^#[a-zA-Z]{2,16}$")) {
                schemas.Error errorTweetHashTag = new schemas.Error();
                errorTweetHashTag.setHttpCode(400);
                errorTweetHashTag.setErrorCode(425); 
                errorTweetHashTag.setMessage("Hashtag does not follow the specified pattern!");

                return Response
                    .status(400)
                    .type(MediaType.APPLICATION_JSON_TYPE)    
                    .entity(errorTweetHashTag)
                    .build();
            }
        }
        
        //Creating new Tweet:
        Tweet tweet = new Tweet();
        tweet.setId(em.createQuery("select max(t.id) from Tweet t", Integer.class).getSingleResult() + 1);
        tweet.setContent(postTweetReq.getTweetBody());
        tweet.setIdUser(user);
        tweet.setCreatedAt(Date.from(Instant.now()));
        em.persist(tweet);

        //Creating new HashTags, if they were not existing:
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
            .status(201)
            .type(MediaType.APPLICATION_JSON_TYPE)   
            .entity(tweetResp)
            .build();
    }
    
    @DELETE
    @Path("{tweetId}")
    public Response deleteTweet(@Context HttpHeaders header, @PathParam("tweetId") String tweetId) {
        List<String> headerList = header.getRequestHeader("X-Username");
        String username;
        
        //Username header check:
        if(headerList == null || headerList.size() <= 0) {
            schemas.Error errorHeader = new schemas.Error();
            errorHeader.setHttpCode(401);
            errorHeader.setErrorCode(401);
            errorHeader.setMessage("Username header is missing!");
            
            return Response
                .status(401)
                .type(MediaType.APPLICATION_JSON_TYPE)    
                .entity(errorHeader)
                .build();
        }
        
        username = headerList.get(0);
        
        //Username Regex check:
        if(!username.matches("^[a-zA-Z0-9_]{4,32}$")) {
            schemas.Error errorRegex = new schemas.Error();
            errorRegex.setHttpCode(403);
            errorRegex.setErrorCode(423); 
            errorRegex.setMessage("Username does not follow the specified pattern!");
            
            return Response
                .status(403)
                .type(MediaType.APPLICATION_JSON_TYPE)    
                .entity(errorRegex)
                .build();
        }
        
        //User search, and User does not exists check:
        User user;
        try {
            user = em.createNamedQuery("User.findByUsername", User.class).setParameter("username", username).getSingleResult();
        } catch(javax.persistence.NoResultException exc) {
            schemas.Error errorUserNotExists = new schemas.Error();
            errorUserNotExists.setHttpCode(403);
            errorUserNotExists.setErrorCode(422); 
            errorUserNotExists.setMessage("User does not exists!");
            
            return Response
                .status(403)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(errorUserNotExists)
                .build();
        }
        
        
        Tweet tweet = em.find(Tweet.class, Integer.parseInt(tweetId));
        if(tweet == null) {
            schemas.Error errorTweetNotFound = new schemas.Error();
            errorTweetNotFound.setHttpCode(404);
            errorTweetNotFound.setErrorCode(404);
            errorTweetNotFound.setMessage("Tweet not found!");
            
            return Response
                .status(404)
                .type(MediaType.APPLICATION_JSON_TYPE)    
                .entity(errorTweetNotFound)
                .build();
        }
        
        //Somebody elses tweet check:
        if(tweet.getIdUser() != user) {
            schemas.Error errorOtherUser = new schemas.Error();
            errorOtherUser.setHttpCode(403);
            errorOtherUser.setErrorCode(421);
            errorOtherUser.setMessage("Somebody elses tweet!");
            
            return Response
                .status(403)
                .type(MediaType.APPLICATION_JSON_TYPE)    
                .entity(errorOtherUser)
                .build();
        }
        
        List<Tweethashtag> thtList = em.createQuery("SELECT tht FROM Tweethashtag tht WHERE tht.idTwe = :id", Tweethashtag.class).setParameter("id", tweet).getResultList();
        
        ArrayList<String> htArr = new ArrayList<>();
        for (Tweethashtag tweethashtag : thtList) {
            Hashtag tag = tweethashtag.getIdTag();
            htArr.add(tag.getHashname());
        }
        
        //Creating tweetResponse:
        TweetResp tweetResp = new TweetResp();
        tweetResp.setTweetId(tweet.getId());
        tweetResp.setTweetBody(tweet.getContent());
        tweetResp.setHashTags(htArr.toArray(new String[0]));
        tweetResp.setCreatedBy(username);
        tweetResp.setCreatedAt(tweet.getCreatedAt().toString());
        
        em.remove(tweet);
        return Response
            .status(200)
            .type(MediaType.APPLICATION_JSON_TYPE)    
            .entity(tweetResp)
            .build();
    }
    
    @GET
    public Response getTweets(@Context HttpHeaders header, @Context UriInfo uriInfo, @QueryParam("hashTag") List<String> hashTagArray,
        @QueryParam("username") List<String> usernameArray, @DefaultValue("50") @QueryParam("limit") int limit, 
        @DefaultValue("0") @QueryParam("offset") int offset) 
    {   
        List<String> headerList = header.getRequestHeader("X-Username");
        //String username;
        
        //Username header check:
        if(headerList == null || headerList.size() <= 0) {
            schemas.Error errorHeader = new schemas.Error();
            errorHeader.setHttpCode(401);
            errorHeader.setErrorCode(401);
            errorHeader.setMessage("Username header is missing!");
            
            return Response
                .status(401)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(errorHeader)
                .build();
        }
        
        //Bad request check:
        if(limit < 1 || limit > 100 || offset < 0) {
             schemas.Error errorLimitOffset = new schemas.Error();
            errorLimitOffset.setHttpCode(401);
            errorLimitOffset.setErrorCode(401);
            errorLimitOffset.setMessage("Bad request, some of the specified parametars are not valid!");

            return Response
                .status(401)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(errorLimitOffset)
                .build();
        }
        
        if (usernameArray == null) {
            usernameArray = new ArrayList<>();
        }
        if (hashTagArray == null) {
            hashTagArray = new ArrayList<>();
        }
        
        //Get tweets:
        String query = "SELECT t FROM Tweet t WHERE 1<2 ";
        
        if (usernameArray.size() > 0) {
            StringBuilder usernamesKeys = new StringBuilder();
            
            for (int i = 0;i<usernameArray.size();i++) {
                usernamesKeys.append(":user").append(i).append(",");
            }
            usernamesKeys.deleteCharAt(usernamesKeys.length() - 1);
            
            String queryUsersFilter = "AND t.idUser.username IN (" + usernamesKeys.toString() + ")";
            query += queryUsersFilter;
        }
        
        if (hashTagArray.size() > 0) {
            StringBuilder hashtagsKeys = new StringBuilder();
            
            for (int i = 0;i<hashTagArray.size();i++) {
                hashtagsKeys.append(":ht").append(i).append(",");
            }
            hashtagsKeys.deleteCharAt(hashtagsKeys.length() - 1);
            
            String queryHashtags = " AND EXISTS (select tht from Tweethashtag tht where tht.idTwe.id=t.id and tht.idTag.hashname IN (" + hashtagsKeys + "))";
            query += queryHashtags;
        }
        
        TypedQuery<Tweet> createQuery = em.createQuery(query, Tweet.class);
        
        for (int i = 0;i < usernameArray.size();i++) {
            createQuery.setParameter("user"+i, usernameArray.get(i));
        }
        for (int i = 0;i < hashTagArray.size();i++) {
            createQuery.setParameter("ht"+i, hashTagArray.get(i));
        }
        createQuery.setMaxResults(limit + 1); //The last element is used for checking if there are more tweets for next page
        createQuery.setFirstResult(offset);
        
        Stream<Tweet> resultStream = createQuery.getResultStream();
        List<TweetResp> resultList = resultStream
                .map(tweet -> 
                    new TweetResp(tweet.getId(), 
                            tweet.getContent(), 
                            tweet.getIdUser().getUsername(), 
                            tweet.getCreatedAt().toString(), 
                            tweet.getTweethashtagList().stream().map(tag -> tag.getIdTag().getHashname()).toArray(s -> new String[s])))
                .collect(Collectors.toList());
        
        TweetsPageResp tweetsPageResp = new TweetsPageResp();
        if (resultList.size() == limit + 1) {
            UriBuilder absolutePathBuilder = uriInfo.getRequestUriBuilder();
            absolutePathBuilder.replaceQueryParam("offset", offset + limit);
            tweetsPageResp.setNextPage(absolutePathBuilder.toString());
            resultList.remove(limit);
        }
        tweetsPageResp.setTweets(resultList.toArray(new TweetResp[0]));
         
        return Response
            .status(200)
            .type(MediaType.APPLICATION_JSON_TYPE)    
            .entity(tweetsPageResp)
            .build();
    }
    
    
}
