Extending the Coral Cloud Agent with Heroku - Workshop Edition
==============================================================

> **DO NOT SHARE:** This is internal version of the public demo, it removes the Heroku Connect element and requires that the list of experience picture URLs are resovled by the caller (Agentforce). This is done for the [Coral Cloud Agentforce workshop](https://docs.google.com/document/d/1pLY6T5xucfwpuhlAHE7Cl0ZQytWkrKyb4GaVc2vz0qs/edit?tab=t.0#heading=h.krqygng1inj8) to simplify setup. The workshop additional resources link to the public version. There is no need to share this version.

This demo extends the popular [Coral Cloud demo](https://trailhead.salesforce.com/content/learn/projects/quick-start-explore-the-coral-cloud-sample-app) by extending the Coral Cloud Agent with the ability to dynamically generate a custom collage of the guests stay. You can watch a short demo video [here](https://www.youtube.com/watch?v=yd97A9GLFUA). Also if you missed our other Agentforce demo you can still catchup through [this](https://blog.heroku.com/building-supercharged-agents-heroku-agentforce) blog and associated video and sample code.


![alt text](downloads/test.png "Collage")

Deploy Instructions
----------------------------
- Deploy the [Coral Cloud sample](https://developer.salesforce.com/sample-apps), including the Service Cloud extension that includes setting up the Experience Cloud site and validate that you can see the Coral Cloud website and access the agent by booking an experience
- This sample stores generated images for future download in an AWS S3 bucket. Ensure you have created an AWS S3 bucket and setup a policy to permit read write access. The default bucket name in this sample is `coralcloudcollagefiles`
- Deploy as you would any [Heroku Java application](https://devcenter.heroku.com/articles/getting-started-with-java) using `git push heroku main`
- Set environment variables `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` locally and/or for Heroku via `heroku config:set` 
- Configure [Heroku Connect](https://www.heroku.com/connect) to the Salesforce Org you are using for the Coral Cloud sample (the free demo plan will work just fine). Configure mappings to the **Booking**, **Contact**, **Experience** and **Session** objects (all fields).
- Lastly configure a new **Agentforce Action** and add it to the **Coral Cloud Agent** (see instructions below)

Configuring the Agentforce Action
---------------------------------
- Complete [this](https://github.com/heroku-examples/heroku-agentforce-tutorial?tab=readme-ov-file#creating-agentforce-custom-actions-with-heroku) tutoriall to learn how to deploy and configure this action.
