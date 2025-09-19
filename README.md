## Welcome to <b>What's for Dinner?</b>

This helpful app contains meals for every day of the week based on daily themes!

This app is hosted at http://whats-for-dinner-lb-2095675956.us-east-2.elb.amazonaws.com 

Check out:
- <b>Themes</b> at `/themes`
- <b> Daily Options </b> at `themes/<day - theme>/meals`
- <b> Meal Details </b> at `meals/<meal ID>` 


Note:
This app is deployed to AWS Fargate using GitHub Actions. The CI/CD pipeline includes SonarCloud security scanning.

TODO
- secrets manager
- database - dynamodb?
- profiles, environments (qa, prod)
- POST options to add new recipes
  - authorizations