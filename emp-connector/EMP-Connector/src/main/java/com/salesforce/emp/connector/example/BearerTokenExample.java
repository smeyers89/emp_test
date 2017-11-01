/*
 * Copyright (c) 2016, salesforce.com, inc. All rights reserved. Licensed under the BSD 3-Clause license. For full
 * license text, see LICENSE.TXT file in the repo root or https://opensource.org/licenses/BSD-3-Clause
 */
package com.salesforce.emp.connector.example;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import com.salesforce.emp.connector.BayeuxParameters;
import com.salesforce.emp.connector.EmpConnector;
import com.salesforce.emp.connector.TopicSubscription;

public class BearerTokenExample {
    public static void main(String[] argv) throws Exception {
    		System.out.println("access token: " + argv[0]);

// use this for Sandbox
    		String url = "https://na79.salesforce.com";
    		String pifCreate = "/topic/pifCreatev003";
    		String therapyCreate = "/topic/therapyCreatev001";
    		String copayUpdate = "/topic/copayUpdatev001";
    		String ivfCreate = "/topic/ivfCreatev001";
    		String caseCreate = "/topic/caseCreatev001";
  
    		
        if (argv.length < 1) {
            System.err.println("pass me the access token, you must");
            System.exit(1);
        }
        if (argv.length > 1) {
        	System.err.println("passed too many arguments you did, connect to salesforce I will not");
            System.exit(1);
        }
        long replayFrom = EmpConnector.REPLAY_FROM_EARLIEST;

        BayeuxParameters params = new BayeuxParameters() {

            @Override
            public String bearerToken() {
                return argv[0];
            }
            	
            @Override
            public URL host() {
                try {
                    return new URL(url);
                } catch (MalformedURLException e) {
                    throw new IllegalArgumentException(String.format("Unable to create url: %s", url), e);
                }
            }
        };
        

        Consumer<Map<String, Object>> consumer = event -> System.out.println(String.format("Received:\n%s", event));
        
		
        EmpConnector connector = new EmpConnector(params);

        connector.start().get(5, TimeUnit.SECONDS);

        TopicSubscription subscription = connector.subscribe(pifCreate, replayFrom, consumer).get(5, TimeUnit.SECONDS);
        TopicSubscription subscriptionOne = connector.subscribe(therapyCreate, replayFrom, consumer).get(5, TimeUnit.SECONDS);
        TopicSubscription subscriptionTwo = connector.subscribe(copayUpdate, replayFrom, consumer).get(5, TimeUnit.SECONDS);
        TopicSubscription subscriptionThree = connector.subscribe(ivfCreate, replayFrom, consumer).get(5, TimeUnit.SECONDS);
        TopicSubscription subscriptionFour = connector.subscribe(caseCreate, replayFrom, consumer).get(5, TimeUnit.SECONDS);

        System.out.println(String.format("Subscribed: %s", subscription));
        System.out.println(String.format("Subscribed: %s", subscriptionOne));
        System.out.println(String.format("Subscribed: %s", subscriptionTwo));
        System.out.println(String.format("Subscribed: %s", subscriptionThree));
        System.out.println(String.format("Subscribed: %s", subscriptionFour));

       
    
	}
  }