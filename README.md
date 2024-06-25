
### Movie Rental Service

The **Movie Rental Service** is a backend-only project developed to manage movie rental operations via REST APIs. It features robust integration of modern Java and Kotlin technologies, including Jakarta EE APIs, WildFly, Arquillian for testing, and Resteasy for building RESTful services. This repository serves as a playground to explore backend development practices, emphasizing scalability, testing, and seamless deployment using Maven and AWS Elastic Beanstalk.

#### Running the Project

To run the Movie Rental Service locally or deploy it to your environment, follow these steps:

1. **Prerequisites:**
   - JDK 11 or higher
   - Maven 3.8.4 or higher
   - Docker (optional for running databases locally)

2. **Clone the Repository:**
   ```bash
   git clone https://github.com/SamzyTechSolutions/movie-rental-service.git
   cd movie-rental
   ```

3. **Build the Project:**
   ```bash
   mvn clean package -DskipTests=true
   ```

4. **Run Integration Tests:**
   ```bash
   mvn verify
   ```

5. **Deploy to AWS Elastic Beanstalk:**
   - Ensure AWS credentials are configured.
   - Update `dist/Procfile` with your application's startup command.
   - Run Bitbucket Pipelines to deploy (configured for AWS Elastic Beanstalk).

6. **API Endpoints:**
   - Once deployed, access the endpoints via `<base_url>/api/movies`, `<base_url>/api/rents`, etc.

#### Continuous Integration and Deployment (CI/CD) with Bitbucket Pipelines

This project integrates with Bitbucket Pipelines for automating the CI/CD process. The pipeline is configured to build, test, and deploy the application upon pushing changes to the repository. Below is a snippet of the Bitbucket pipeline configuration (`bitbucket-pipelines.yml`):

```yaml
image: maven:3.8.4-jdk-11

definitions:
  steps: &deploy
    name: 'Build and Deploy'
    caches:
      - maven
    script:
      - export BUILDTIMESTAMP=$(date +%s)
      - export BITBUCKET_COMMIT_SHORT="${BITBUCKET_COMMIT::8}"
      - export VERSION_ID="$BITBUCKET_BRANCH$BITBUCKET_COMMIT_SHORT"
      - apt-get update && apt-get install -y zip
      - mvn clean package -DskipTests=true
      # Prepare package
      - mkdir dist
      - cp target/movie-rental-bootable.jar dist/
      - cp target/movie-rental.war dist/
      - cp Procfile dist/
      - zip -j dist.zip dist/*
      - pipe: atlassian/aws-elasticbeanstalk-deploy:1.0.2
        variables:
          AWS_ACCESS_KEY_ID: $AWS_ACCESS_KEY_ID
          AWS_SECRET_ACCESS_KEY: $AWS_SECRET_ACCESS_KEY
          AWS_DEFAULT_REGION: $AWS_REGION
          ENVIRONMENT_NAME: $AWS_EB_ENVIRONMENT_NAME
          APPLICATION_NAME: 'samuel-bootstrap'
          ZIP_FILE: 'dist.zip'
          S3_BUCKET: 'movie-rental-deployment'
          VERSION_LABEL: '$BUILDTIMESTAMP-$VERSION_ID'

pipelines:
  default:
    - step:
        name: 'Integration Test'
        caches:
          - maven
        script:
          - mvn -B verify

  branches:
    master:
      - step:
          <<: *deploy
          deployment: dev
```
