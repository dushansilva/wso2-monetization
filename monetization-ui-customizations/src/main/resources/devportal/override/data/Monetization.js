/**
 * Copyright (c) 2019, WSO2 Inc. (http://wso2.com) All Rights Reserved.
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
import Utils from 'AppData/Utils';
import APIClientFactory from './MonetizationClientFactory';
/**
 * This class contains tenant related api requests
 */
class Monetization {
    /**
     * @inheritdoc
     */
    constructor() {
        this.client = new APIClientFactory().getAPIClient(Utils.getEnvironment().label).client;
    }

    completeCheckout = (sessionId, workflowReferenceId) => {
        return this.client.then((client) => {
            const requestContent = {
                sessionId,
                workflowReferenceId,
            };
            const payload = { body: requestContent };
            return client.apis.Stripe.post_subscriptions_complete_checkout(payload);
        });
    };

    getUpdateCardSession = (apiId) => {
        return this.client.then((client) => {
            const payload = {
                apiId,
            };
            return client.apis.Stripe.post_stripe_setup_card_session__apiId_(payload);
        });
    };

    completeUpdateCardSession = (sessionId) => {
        return this.client.then((client) => {
            const payload = {
                body: { sessionId },
            };
            return client.apis.Stripe.post_stripe_update_stripe_card(payload);
        });
    };
    getCardDetails = () => {
        return this.client.then((client) => {
            console.log(client.apis.Stripe);
            return client.apis.Stripe.get_stripe_billing_details();
        });
    };
}

export default Monetization;
