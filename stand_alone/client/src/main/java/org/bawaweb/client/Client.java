/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.bawaweb.client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bawaweb.core.model.Card;
import org.apache.cxf.jaxrs.client.WebClient;

import org.bawaweb.core.model.CustomerCard;



/**
 * @author medhoran
 *
 *
 */
public final class Client {
    //test-eg
// http://localhost:8080/customerCardService/customerCards/2dcf8eab-fe9a-4d84-9e9f-299f89141caf
    //http://10.0.103.115:8080/services/cards/4563784756-6547387465-44
    private static final String CLIENT_CONFIG_FILE = "/ClientConfig.xml";
    //    private static final String BASE_SERVICE_URL = "http://10.0.103.115:8080/services/cards";       //qa
//    private static final String BASE_SERVICE_URL = "http://10.0.103.112:8080/services/cards";       //ci

    private static final String HTTP_PROTOCOL  = "http://";
    private static final String SERVER_IP = "localhost";// "10.0.103.112";//"localhost";//"10.0.104.90";//"10.0.103.205";;
    private static final String PORT = "8080";
    private static final String BASE_SERVICE_URL = HTTP_PROTOCOL + SERVER_IP + ":" + PORT + "/services/cards";       // prod 10.0.103.205
    private static final String CARD_ID = "2dcf8eab-fe9a-4d84-9e9f-299f89141caf";

    private Client() {
    }

    /**
     * Retreives the card information for a card id - using regular Http GET request
     * @param cardId - corresponds to the card_id in customer_card table
     * @return Response will contain the CustomerCard information or an error message
     * @throws Exception
     */
    public Response doGetCardIdRequest(String cardId) throws Exception {
        WebClient wc = WebClient.create(BASE_SERVICE_URL + "/" + cardId);
        wc.accept(MediaType.APPLICATION_JSON_TYPE);
        Response resp = wc.get();
        System.out.println("Resp is " + resp.toString());
        System.out.println("Entity is " + resp.getEntity());
        System.out.println("status is " + resp.getStatus());
        return resp;
    }
    /**
     * Send regular Http POST request to add the customer card
     * @param card - the customer card
     * @return response - will contain the location for the added card or an error messge
     */
    public Response doAddCustomerCardPostRequest(CustomerCard card) {
        WebClient wc = WebClient.create(BASE_SERVICE_URL, CLIENT_CONFIG_FILE);
        wc.type(MediaType.APPLICATION_JSON);
        wc.language(MediaType.APPLICATION_JSON);
        wc.accept(MediaType.APPLICATION_JSON_TYPE);
        System.out.println("\n\n****************\nADDDING CARD\n"+ card.toJSON() + "****************");
//        final String toJSON = card.toJSON();
//        System.out.println("toJson is\n"+ toJSON);
        Card theCard = card.transformToCard(card);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);     //diasble for 2.0 jackson
        String jsonRep = null;
        try {
            jsonRep =  mapper.writeValueAsString(theCard);
            System.out.println("HERE");

            Response resp = wc.post(jsonRep);
            System.out.println("jsonRep is " + jsonRep);
            System.out.println(resp.toString());
            return resp;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String args[]) throws Exception {
        Client cc = new Client();

        // GET
//        cc.doGetCardIdRequest("32faaad7-7285-4968-a2f5-8ad899a64127");//("d169e097-5137-4890-a66e-a80610152ae2");//("2dcf8eab-fe9a-4d84-9e9f-299f89141caf");

        // POST
        CustomerCard aCard = new CustomerCard();
        aCard.setValue("5115915115915118");//("3411-341-1341-1347");//("5405-2222-2222-2226");//("5111005111051128");;
        aCard.setCreatedBy("NOW1BAWA");//("AAAMIIIIII");//("UUUUTUUUMEEEEEE");
        aCard.setModifiedBy("NOW1NOBAWA");//("MISHTIEEEEE");

        cc.doAddCustomerCardPostRequest(aCard);
    }

}