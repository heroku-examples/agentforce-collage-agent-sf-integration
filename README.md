Extending the Coral Cloud Agent with Heroku - Heroku Integration Edition
========================================================================

> **NOTE:** This is a version of the [orginal demo](https://github.com/heroku-examples/agentforce-collage-agent) that has been updated to use the [Heroku Integration Pilot](https://www.youtube.com/watch?v=T5kOGNuTCLE).

This demo extends the popular [Coral Cloud demo](https://trailhead.salesforce.com/content/learn/projects/quick-start-explore-the-coral-cloud-sample-app) by extending the Coral Cloud Agent with the ability to dynamically generate a custom collage of the guests stay. You can watch a short demo video [here](https://www.youtube.com/watch?v=yd97A9GLFUA). Also if you missed our other Agentforce demo you can still catchup through [this](https://blog.heroku.com/building-supercharged-agents-heroku-agentforce) blog and associated video and sample code.

![alt text](images/test.png "Collage")

Before you Begin
----------------

Ensure you have access to the following

- Heroku account login
- Heroku CLI installed and logged in
- Salesforce CLI
- Salesforce Org has Agentforce enabled under Agents under Setup
- Salesforce Org with the CoralCloud sample application deployed and configured with data
- Salesforce Org user name and password
- Access to the Heroku Integration Pilot

Complete the following tasks and checks:

1. Ensure your org is authenticated with the Salesforce CLI

    ```
    sf org login web --alias coral-cloud-org
    ```

2. Check the toggle is on to enable **Einstein** by searching for **Einstein Setup** under **Setup**

3. Check the toggle is on to enable **Agentforce** by searching for **Agents** under **Setup**


1 - Heroku Deployment and Configuration
---------------------------------------

Heroku applications that expose HTTP endpoints described by OpenAPI can be imported into a Salesforce org

Heroku applications can then use a preauthenticated connection to the Salesforce API to access the org

Use the following commands to deploy and import this application:

1. Deploy to Heroku and configure AWS S3 storage for the images

    ```
    heroku create agentforce-collage-agent

    heroku config set AWS_ACCESS_KEY_ID=[YOUR_KEY]

    heroku config set AWS_BUCKET_NAME=[YOUR_BUCKET]

    heroku config set AWS_REGION=[YOUR_REGION]

    heroku config set AWS_SECRET_ACCESS_KEY=[YOUR_ACCESS_KEY]

    git push heroku main
    ```

2. Configure the **Heroku Integration** addon, buildpack and import the application into the org

    ```
    heroku addons:create heroku-integration

    heroku buildpacks:add https://github.com/heroku/heroku-buildpack-heroku-integration-service-mesh

    heroku salesforce:connect coral-cloud-org --store-as-run-as-user

    heroku salesforce:import api-docs.yaml --org-name coral-cloud-org --client-name GenerateCollage
    ```

3. Trigger an application rebuild to install the Heroku Integration buildpack

    ```
    git commit --allow-empty -m "empty commit"

    git push heroku main
    ```

4. Use the following command to confirm the application is running without errors

    ```
    heroku logs
    ```

5. Search for **Heroku** under Setup and click **Apps** to confirm the application has been imported

    ```
    sf org open -o coral-cloud-org
    ```

2 - Calling the Heroku Application from Apex and Flow
-----------------------------------------------------

1. Determine a **Contact** name to test with

   1. Navigate to the **CoralCloud** application 
   2. Click the **Bookings** tab and the **All** list
   3. Find a **Booking** record with more than one associated experience 
   4. Take note of the **Contact** name and Id

2. Grant permission to invoke the Heroku application

    ```
    sf org assign permset --name GenerateCollage -o coral-cloud-org
    ```

3. Test the imported Heroku application by calling it from Apex. Use your Contact Id in the script below. Review the output for the `USER_DEBUG` entry.

    ```
    echo \
    "ExternalService.GenerateCollage service = new ExternalService.GenerateCollage();" \
    "ExternalService.GenerateCollage.generate_Request request = new ExternalService.GenerateCollage.generate_Request();" \
    "request.contactId = '';" \
    "request.quote = 'Coding on the beach!';" \
    "System.debug(service.generate(request).Code200);" \
    | sf apex run -o coral-cloud-org
    ```

4. Test the imported Heroku application by calling it from Flow

    1. Deploy a simple Flow to call the Heroku app

    ```
    sf project deploy start --source-dir src-salesforce -o coral-cloud-org
    ```

    2. Search for **Flows** under Setup and open the `GenerateCollage` Flow then click **Debug**
    3. Enter the **Contact** Id retrieved above and `Coding on the beach!` in the quote parameter


3 - Calling the Heroku Application from Agentforce
--------------------------------------------------

1. Create an Agent Action

    1. From **Setup**, open **Agent Actions**.
    2. Click **New Agent Action**.
    3. Configure the action as follows:
       | Field                 | Value              |
       | :-------------------- | :----------------- |
       | Reference Action Type | Flow               |
       | Reference Action      | `Generate Collage` |
       | Agent Action Label    | Keep the default   |
       | Agent Action API Name | Keep the default   |
   4. Click **Next**.
   5. Leave in place the default instructions (obtained from the Flow created above)
   6. Check **Require Input** in the **Input** section for the **quote** input.
   7. Check **Collect data from user** in the **Input** section for the **quote** input.
   8. Check **Show in conversation** in the **Output** section for the **downloadURL** output..
   9. Click **Finish**

2. Add the Agent Action to an Agent

   1. From **Setup**, open **Agents** (under Agent Studio).
   2. Click on **Einstein Copilot** in the list of agents.
   3. Click **Open in Builder**.
   4. Click **Deactivate** to deactivate the agent, so that you can add a new custom action.
   5. In the **Topics** sidebar, click the **CustomerServiceAssistant** tab.
   6. Go to **This Topic's Actions** tab.
   7. Select New and from the dropdown list choose Add from Asset Library.
   8. Check the **Generate Collage** action and click **Finish**.

3. Testing the Agent Action

    1. Refresh the **Conversation Preview** panel click in the top right **Refresh** icon

    2. In the **Conversation Preview** panel, enter the following prompt:

       ```txr
       Can I have a collage of my experience?
       ```

    3. The agent will request the contact name and a quote, enter the following using the contact name you noted earlier:
   
       ```txt
       My name is [Contact Name] and I loved coding on the beach!
       ```

    3. You should now see the agent provide a download link to access the collage. Note that your image may vary as the data in each workshop environment varies.
