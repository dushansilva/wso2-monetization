/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wso2.dao;

import com.wso2.dto.CardUpdateDetailsDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.apim.monetization.impl.StripeMonetizationException;
import org.wso2.apim.monetization.impl.model.MonetizationSharedCustomer;
import org.wso2.carbon.apimgt.impl.dao.ApiMgtDAO;
import org.wso2.carbon.apimgt.impl.utils.APIMgtDBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StripeDao {
    private static final Log log = LogFactory.getLog(StripeDao.class);
    private static StripeDao INSTANCE = null;

    public static final String GET_CARD_UPDATE_REQUIRED_DETAILS =
            "SELECT SUB.SUBSCRIBED_API_ID,SC.SHARED_CUSTOMER_ID," +
                    "PC.CUSTOMER_ID AS PARENT_CUSTOMER_ID, PC.SUBSCRIBER_ID FROM AM_MONETIZATION_SUBSCRIPTIONS  SUB " +
                    "JOIN AM_MONETIZATION_SHARED_CUSTOMERS SC ON SUB.SHARED_CUSTOMER_ID = SC.ID " +
                    "JOIN AM_MONETIZATION_PLATFORM_CUSTOMERS PC ON SC.PARENT_CUSTOMER_ID = PC.ID " +
                    "WHERE SUBSCRIBER_ID =?";

    /**
     * Method to get the instance of the StripeMonetizationDAO.
     *
     * @return {@link StripeDao} instance
     */
    public static StripeDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StripeDao();
        }
        return INSTANCE;
    }

    public List<CardUpdateDetailsDTO> getCardUpdateRequiredDetails(int subscriberId)
            throws StripeMonetizationException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet result = null;
        List<CardUpdateDetailsDTO> list = new ArrayList<>();
        String sqlQuery = GET_CARD_UPDATE_REQUIRED_DETAILS;
        try {
            conn = APIMgtDBUtil.getConnection();
            ps = conn.prepareStatement(sqlQuery);
            ps.setInt(1, subscriberId);
            result = ps.executeQuery();
            while (result.next()) {
                CardUpdateDetailsDTO cardUpdateDetailsDTO = new CardUpdateDetailsDTO();
                cardUpdateDetailsDTO.setSubscribedApiId(result.getInt("SUBSCRIBED_API_ID"));
                cardUpdateDetailsDTO.setSharedCustomerId(result.getString("SHARED_CUSTOMER_ID"));
                cardUpdateDetailsDTO.setParentCustomerId(result.getString("PARENT_CUSTOMER_ID"));
                cardUpdateDetailsDTO.setSubscriberId(result.getInt("SUBSCRIBER_ID"));
                list.add(cardUpdateDetailsDTO);
            }
        } catch (SQLException e) {
            String errorMessage = "Failed to get shared customer details ";
            log.error(errorMessage);
            throw new StripeMonetizationException(errorMessage, e);
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, conn, result);
        }
        return list;
    }


    public int getSubscriptionId(String stripeSubscriptionId) throws StripeMonetizationException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int subscriptionId = -1;
        String GET_API_ID_APP_ID_FROM_STRIPE_SUBS =
                "SELECT SUBSCRIBED_API_ID,SUBSCRIBED_APPLICATION_ID " +
                        "FROM AM_MONETIZATION_SUBSCRIPTIONS WHERE SUBSCRIPTION_ID = ?";
        String GET_SUBSCRIPTION =
                "SELECT SUBSCRIPTION_ID FROM AM_SUBSCRIPTION WHERE API_ID=? AND APPLICATION_ID=?";

        try {
            conn = APIMgtDBUtil.getConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(GET_API_ID_APP_ID_FROM_STRIPE_SUBS);
            ps.setString(1, stripeSubscriptionId);
            rs = ps.executeQuery();
            while (rs.next()) {
                int subscribedApiId = rs.getInt("SUBSCRIBED_API_ID");
                int subscribedApplicationId = rs.getInt("SUBSCRIBED_APPLICATION_ID");
                PreparedStatement preparedStatement = conn.prepareStatement(GET_SUBSCRIPTION);
                preparedStatement.setInt(1, subscribedApiId);
                preparedStatement.setInt(2, subscribedApplicationId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    subscriptionId = resultSet.getInt("SUBSCRIPTION_ID");
                }
            }
        } catch (SQLException var11) {
            String errorMessage = "Error while getting id of stripe subscription ID : " + stripeSubscriptionId;
            log.error(errorMessage);
            throw new StripeMonetizationException(errorMessage, var11);
        } finally {
            APIMgtDBUtil.closeAllConnections(ps, conn, rs);
        }
        return subscriptionId;
    }
}
