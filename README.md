### Prerequisite
1. Java 1.8 or higher
2. Maven 3.6 or higher
3. Nodejs 10 or higher 
4. NPM 6 or higher


### Steps to build
1. Clone [https://github.com/wso2-extensions/wso2-am-stripe-plugin](https://github.com/wso2-extensions/wso2-am-stripe-plugin), checkout to tag v1.0.0 and build it. This will add ```org.wso2.apim.monetization.impl-1.0.0.jar``` to the local maven repository.
2. Run `mvn clean install` in monetization-customizations root directory
3. All the resources needed for the deployment will be copied to deployment-resources directory.

### Configuring monetization-workflow
1. Copy the ```monetization-workflow/target/monetization-workflow-1.0.jar``` to ```<APIM_HOME>/repository/components/lib```

2. Download and copy ```https://github.com/stripe/stripe-java/releases/download/v16.5.0/stripe-java-16.5.0.jar``` to ```<APIM_HOME>/repository/components/lib```
2. Configuration for tenant admin

    a. Sign in to the WSO2 API-M Management Console. ```https://<hostname>:9443/carbon```

    b. Click Main, navigate to Resources, and click Browse.

    c. Enter the following path in Location: and click Go. ```/_system/config/apimgt/applicationdata/tenant-conf.json```

    d. Add the following configuration in the tenant-conf.json file
    ```json
    "MonetizationInfo": {
        "StripePlatformAccountPublishableKey": "<STRIPE_PUBLISHABLE_KEY>",
        "BillingEnginePlatformAccountKey": "<STRIPE_SECRET_KEY>"
    }
    ```

    e. Enter the following path in Location: and click Go. ```/_system/governance/apimgt/applicationdata/workflow-extensions.xml``` 
    Remove or comment out the existing ```<SubscriptionCreation ...``` and ```<SubscriptionDeletion ...```  workflow executor. Add the following  
    
    ```xml
    <SubscriptionCreation executor="com.wso2.ExtendedStripeSubscriptionCreationWorkflowExecutor"/>
    <SubscriptionDeletion executor="org.wso2.apim.monetization.impl.workflow.StripeSubscriptionDeletionWorkflowExecutor"/>
    ```
    <br/>

### Configuring monetization-webapp
1. Copy ```monetization-workflow/target/api#wso2#monetization.war``` to ```<APIM_HOME>/repository/deployment/server/webapps```
2. Add the following to <APIM_HOME>/repository/conf/deployment.toml. This will whitelist two rest api endpoints to be accesssed without authentication.
    ```toml
    [[apim.rest_api.whitelisted_uri]]
    uri_path = "/api/wso2/monetization/swagger.yaml"
    http_method = "GET,HEAD"

    [[apim.rest_api.whitelisted_uri]]
    uri_path = "/api/wso2/monetization/stripe/webhook"
    http_method = "POST"
    ```
3. Add the following to tenant admin

    a. Sign in to the WSO2 API-M Management Console. ```https://<hostname>:9443/carbon```

    b. Click Main, navigate to Resources, and click Browse.

    c. Enter the following path in Location: and click Go. ```/_system/config/apimgt/applicationdata/tenant-conf.json```

    d. Add the following configuration in the tenant-conf.json file
    ```json
    "MonetizationInfo": {
        "StripeWebhookKey":"<STRIPE_WEBHOOK_KEY>"
    }
    ```

### Configuring monetization-ui-customizations
1. Copy ```monetization-ui-customizations/src/main/resources/devportal/site``` directory to ```<APIM_HOME>/repository/deployment/server/jaggeryapps/devportal/``` This directory contains all the UI customizations,images/logos for the devportal. 
2. Copy ```monetization-ui-customizations/src/main/resources/devportal/services/login/login_callback.jag``` file to ```<APIM_HOME>/repository/deployment/server/jaggeryapps/devportal/services/login/```. 
    <br/>

    Optionally without replacing the whole file you can add only the following line to ```<APIM_HOME>/repository/deployment/server/jaggeryapps/devportal/services/login/login_callback.jag``` file that already exists. This is  the only differene between the original file and the customized file.
    ```javascript
    cookie = {'name': 'AM_ACC_TOKEN_DEFAULT_P2', 'value': accessTokenPart2, 'path': "/api/wso2/monetization/", "httpOnly": true, "secure": true, "maxAge": Integer(tokenResponse.expires_in)};
        response.addCookie(cookie);
    ```
3. Copy all the .jsp files in ```monetization-ui-customizations/src/main/resources/login/extensions/``` to ```<APIM_HOME>/repository/resources/```
4. Copy all the images in ```monetization-ui-customizations/src/main/resources/login/extensions/images``` to ```<APIM_HOME>/repository/deployment/server/webapps/authenticationendpoint/images/```
